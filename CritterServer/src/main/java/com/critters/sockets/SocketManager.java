package com.critters.sockets;

import com.critters.dal.dto.entity.User;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jeremy on 8/15/2016.
 */
@ServerEndpoint("/session/{client-id}")
public class SocketManager {
	private static Map<String,Session> peers = Collections.synchronizedMap(new HashMap<String, Session>());
	private static Map<String,User> peerObjects = Collections.synchronizedMap(new HashMap<String, User>());

	public static void addPeer(String jsessionID, User user){
		peerObjects.put(jsessionID, user);
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		return "Socket has received message: " + message;
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("client-id") String clientId) {
		if(peerObjects.containsKey(clientId))
			peers.put(clientId, session);

		try {
			session.getBasicRemote().sendText("Socket Opened!");
		} catch (IOException e) {
		}
	}

	@OnClose
	public void onClose(Session session, @PathParam("client-id") String clientId) {
		System.out.println("Socket Closed for " + clientId);
		peers.remove(clientId);
	}



}
