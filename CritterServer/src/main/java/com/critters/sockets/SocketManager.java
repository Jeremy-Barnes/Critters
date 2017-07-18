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

	private static Map<String,Threeple<User, Session, GameController>> clientIDToSocketUser = Collections.synchronizedMap(new HashMap<String,Threeple<User, Session, GameController>>());
	private static Map<Integer,List<GameController>> gameTypeToRunningGames = Collections.synchronizedMap(new HashMap<Integer, List<GameController>>());
	private static Map<String,GameController> gameIDToGame = Collections.synchronizedMap(new HashMap<String, GameController>());

	private GameController currentGame;
	private User user;
	private boolean hosting;

	@OnMessage
	public String onMessage(String message, Session session, @PathParam("client-id") String clientId) throws Exception {
		SocketGameRequest request = Serializer.fromJSON(message, SocketGameRequest.class);
//			Tuple<User, Session, GameController> tuple = clientIDToSocketUser.get(clientId);
//			System.out.println(tuple.z.title);
//			tuple.z.respondForConnectPermission(message);
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
		System.out.println("Socket Closed for " + clientId);
		clientIDToSocketUser.remove(clientId);
	}

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
			gameIDToGame.put(gameInstanceID, gameInstance);
			return gameInstanceID;
		}
		return null;
	}

	public static void tryToConnect(String gameInstanceID, String clientID, AsyncResponse asyncResponse){
		if(gameIDToGame.containsKey(gameInstanceID) && clientIDToSocketUser.containsKey(clientID)) {
			User user = clientIDToSocketUser.get(clientID).x;
			GameController controller = gameIDToGame.get(gameInstanceID);
			controller.hostSession.getAsyncRemote().sendText("Can " + user.getUserName() + " play???");

			controller.askForConnectPermission(clientID, user, asyncResponse);
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


