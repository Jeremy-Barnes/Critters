package com.critters.games.sockets;

import com.critters.Utilities.Serializer;
import com.critters.dal.dto.Twople;
import com.critters.dal.dto.entity.User;
import com.critters.games.GameController;
import com.critters.games.GameObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by Jeremy on 7/19/2017.
 */
@ServerEndpoint("/session/{client-id}")
public class Player {

	static final Logger logger = LoggerFactory.getLogger("application");

	User user;
	public Session websocket;
	public String clientID;
	GameController game;
	public GameObject physicsComponent;

	public void sendMessage(SocketGameResponse response) {
		String responseSer = Serializer.toJSON(false, response, SocketGameResponse.class);
		try {
			websocket.getBasicRemote().sendText(responseSer);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}


	@OnOpen
	public void onOpen(Session session, @PathParam("client-id") String clientId) {
		if(SocketManager.clientIDToPlayer.containsKey(clientId)) {
			Twople<User, GameController> tuple = SocketManager.clientIDToPlayer.get(clientId);
			if (tuple.y != null) {
				user = tuple.x;
				game = tuple.y;
				this.clientID = clientId;
				websocket = session;
				game.addPlayer(this);
				return; //TODO some kind of response to let the client know they are connected
			}
		}

		//no game and no id
		try {
			session.close();
		} catch (Exception e) {
		}
	}

	@OnMessage
	public String onMessage(String message, Session session, @PathParam("client-id") String clientId) throws Exception {
		SocketGameRequest request = Serializer.fromJSON(message, SocketGameRequest.class);
		game.resolveCommand(request, this);
		return null;
	}

	@OnClose
	public void onClose(Session session, @PathParam("client-id") String clientId) {
		if(SocketManager.clientIDToPlayer.containsKey(clientId)) { //clear them out of the active lists
			if(game != null){
				SocketManager.gameIDToGame.remove(game.gameID);
				SocketManager.gameTypeToRunningGames.get(game.gameType).remove(game);
			}
			SocketManager.clientIDToPlayer.remove(clientId);
		}
	}



}
