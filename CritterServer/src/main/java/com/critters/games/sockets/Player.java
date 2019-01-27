package com.critters.games.sockets;

import com.critters.Utilities.Serializer;
import com.critters.dto.Twople;
import com.critters.dal.entity.User;
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
import javax.xml.bind.annotation.XmlTransient;
import java.io.IOException;

/**
 * Created by Jeremy on 7/19/2017.
 */
@ServerEndpoint("/session/{client-id}")
public class Player {

	static final Logger logger = LoggerFactory.getLogger("application");

	public User user;
	@XmlTransient public Session websocket;
	@XmlTransient public String clientID;
	@XmlTransient GameController game;
	public GameObject physicsComponent;
	public int score;//will probably need to become a ScoreObject some day (Points/Assists/etc)

	public void sendMessage(SocketGameResponse response) {
		String responseSer = Serializer.toJSON(false, response, SocketGameResponse.class);
		try {
			if(websocket.isOpen())
				websocket.getBasicRemote().sendText(responseSer);
			else {
				this.onClose(null, this.clientID);
				websocket.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}


	@OnOpen
	public String onOpen(Session session, @PathParam("client-id") String clientId) {
		if(SocketManager.clientIDToPlayer.containsKey(clientId)) {
			Twople<User, GameController> tuple = SocketManager.clientIDToPlayer.get(clientId);
			if (tuple.y != null) {
				this.user = tuple.x;
				this.game = tuple.y;
				this.clientID = clientId;
				this.websocket = session;
				game.addPlayer(this);
				SocketGameResponse ping = new SocketGameResponse();
				ping.ping = true;
				return Serializer.toJSON(false, ping, SocketGameResponse.class);
			}
		}
		//no game and no id
		this.onClose(session, clientId);
		return null;
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
				this.game.forceKill();
			}
			SocketManager.clientIDToPlayer.remove(clientId);
			if(session.isOpen()) {
				try {
					session.close();
				}catch(Exception ex) {
					logger.error(ex.getMessage(), ex);
				}
			}
		}
	}
}
