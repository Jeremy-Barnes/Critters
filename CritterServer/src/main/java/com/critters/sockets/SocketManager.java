package com.critters.sockets;

import com.critters.Utilities.Serializer;
import com.critters.dal.dto.entity.User;
import com.critters.games.GameController;
import com.critters.games.PongController;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.*;


/**
 * Created by Jeremy on 8/15/2016.
 */
@ServerEndpoint("/session/{client-id}")
public class SocketManager {

	/***
	 * These are the various associations of running multiplayer games. They all represent the same sets of data, but organized differently for faster access based on
	 * intention. Indexed on clientID, gameType, and gameID.
	 */
	private static Map<String,Threeple<User, Session, GameController>> clientIDToSocketUser = Collections.synchronizedMap(new HashMap<String,Threeple<User, Session, GameController>>());
	private static Map<Integer,List<GameController>> gameTypeToRunningGames = Collections.synchronizedMap(new HashMap<Integer, List<GameController>>());
	private static Map<String,GameController> gameIDToGame = Collections.synchronizedMap(new HashMap<String, GameController>());

	/***
	 * Message handler instance variables that are unique to each connected player.
	 */
	private GameController currentGame;
	private User user;
	private boolean hosting;

	@OnMessage
	public String onMessage(String message, Session session, @PathParam("client-id") String clientId) throws Exception {
		SocketGameRequest request = Serializer.fromJSON(message, SocketGameRequest.class);
		if(hosting) {
			currentGame.resolveHostCommand(request);
		} else {
			currentGame.resolvePlayerCommand(request);
		}
		return "";
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("client-id") String clientId) {
		if(clientIDToSocketUser.containsKey(clientId)) {
			Threeple<User, Session, GameController> tuple = clientIDToSocketUser.get(clientId);
			if (tuple.z != null) {
				user = tuple.x;
				currentGame = tuple.z;
				currentGame.addUserSocket(clientId, session);
				hosting = currentGame.hostID.equalsIgnoreCase(clientId);
				return;
			}
		}

		//no game and no id
		try {
			session.close();
		} catch (Exception e) {
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("client-id") String clientId) {
		if(clientIDToSocketUser.containsKey(clientId)) { //clear them out of the active lists
			if(currentGame != null){
				gameIDToGame.remove(currentGame.gameID);
				gameTypeToRunningGames.get(currentGame.gameType).remove(currentGame);
			}
			clientIDToSocketUser.remove(clientId);
		}
	}

	/**
	 * AJAX called, not websocketed
	 * @param gameType - Integer ID for game type
	 * @param clientID - host client ID, NOT THE USER ID.
	 * @param gameName - 2v2 NR 30 Min
	 * @return The unique gameID that other people can use to connect to your game
	 */
	public static String createNewGame(int gameType, String clientID, String gameName){
		if(clientIDToSocketUser.containsKey(clientID) && gameType >= 0) {
			Threeple<User, Session, GameController> tuple = clientIDToSocketUser.get(clientID);
			User user = tuple.x;

			String gameInstanceID = System.currentTimeMillis() + ""; //todo real id here
			GameController gameInstance = getGame(gameType, gameInstanceID, gameName, user, clientID);
			gameIDToGame.put(gameInstanceID, gameInstance);
			tuple.z = gameInstance;
			if (gameTypeToRunningGames.containsKey(gameType)) {
				gameTypeToRunningGames.get(gameType).add(gameInstance);
			} else {
				ArrayList gameList = new ArrayList();
				gameList.add(gameInstance);
				gameTypeToRunningGames.put(gameType, gameList);
			}
			return gameInstanceID;
		}
		return null;
	}

	/***
	 * AJAX called, not websocketed
	 * @param gameInstanceID - Unique game lobby ID to join
	 * @param clientID - NOT USER ID, client ID of person trying to join game
	 * @param asyncResponse - AJAX response that will be resumed once host decides if client can join (or not)
	 */
	public static void tryToConnect(String gameInstanceID, String clientID, AsyncResponse asyncResponse){
		if(gameIDToGame.containsKey(gameInstanceID) && clientIDToSocketUser.containsKey(clientID)) {
			User user = clientIDToSocketUser.get(clientID).x;
			GameController controller = gameIDToGame.get(gameInstanceID);
			controller.askForConnectPermission(clientID, user, asyncResponse);

			controller.hostSession.getAsyncRemote().sendText("Can " + user.getUserName() + " play???"); //todo put this into a real thing

		} else {
			asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
		}
	}

	public static String findGames(int gameID, String userName){
		return (String) gameIDToGame.keySet().toArray()[0];
	}

	public static List<GameController> findGames(int gameID, int userID){
		return null;
	}

	public static List<GameController> findAllGames(int gameID){
		return null;
	}

	public static void  setUserID(String secure, User user) {
		clientIDToSocketUser.put(secure, new Threeple<User, Session, GameController>(user, null, null));
	}

	private static GameController getGame(int gameType, String gameInstanceID, String title, User host, String hostClientId) {
		switch(gameType){
			case 0: return new PongController(gameInstanceID, title, host, hostClientId, gameType);
		}
		return null;
	}
}


