package com.critters.sockets;

import com.critters.Utilities.Serializer;
import com.critters.dal.dto.entity.User;
import com.critters.games.GameController;


import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by Jeremy on 7/19/2017.
 */
@ServerEndpoint("/session/{client-id}")
public class Player {

	User user;
	public Session websocket;
	public String clientID;
	GameController game;

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
				//game.addUserSocket(clientId, session);
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
		return "";
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
