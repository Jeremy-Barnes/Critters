package com.critters.games;

import com.critters.dal.dto.entity.User;
import com.critters.sockets.SocketGameRequest;
import com.critters.sockets.Threeple;
import com.critters.sockets.Twople;

import javax.websocket.Session;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy on 7/17/2017.
 */
public abstract class GameController implements Runnable {

	public volatile String gameID;
	public volatile String title;
	public volatile User host;
	public volatile String hostID;
	public volatile Session hostSession;
	@XmlTransient
	public volatile Map<String,Threeple<User, Session, GameController>> clientIDToSocketAndUser = Collections.synchronizedMap(new HashMap<String, Threeple<User, Session, GameController>>());
	public volatile int gameType;
	Thread gameThread;
	public volatile boolean notExiting = true;
	public HashMap<Integer,Twople<AsyncResponse, String>> wantsToConnectUserIDsToAsyncResponseAndClientID = new HashMap<>();
	public GameController(){
		gameThread = new Thread(this);
		gameThread.run();
	}

	public GameController(String gameID, String title, User host, String hostClientID, int gameType) {
		this.gameID = gameID;
		this.title = title;
		this.host = host;
		this.hostID = hostClientID;
		this.gameType = gameType;
		gameThread = new Thread(this);
	}

	public void start(){
		gameThread.start();
	}

	/***
	 * Game logic tick, advance gamestate
	 */
	public abstract void tick();

	public void run(){
		while(notExiting) {
			System.out.println("thread running");
			tick();
			try {
				Thread.sleep(200);
			} catch(Exception e){}
		}
	}

	public void addUserSocket(String clientID, Session session){
		if(clientID.equalsIgnoreCase(hostID)) {
			hostSession = session;
		}
		if(hostSession != null && clientIDToSocketAndUser.containsKey(clientID)) {
			clientIDToSocketAndUser.get(clientID).y = session;
		}
	}

	public void askForConnectPermission(String clientID, User user, AsyncResponse asyncResponse) {
		wantsToConnectUserIDsToAsyncResponseAndClientID.put(user.getUserID(), new Twople<AsyncResponse, String>(asyncResponse, clientID));
	}

	public void respondForConnectPermission(List<Integer> accepted, List<Integer> rejected){
		if(accepted != null)
		for(int i : accepted){
			if(wantsToConnectUserIDsToAsyncResponseAndClientID.containsKey(i))
				wantsToConnectUserIDsToAsyncResponseAndClientID.get(i).x.resume(Response.status(Response.Status.OK).build());
		}
		if(rejected != null)
		for(int i : rejected){
			if(wantsToConnectUserIDsToAsyncResponseAndClientID.containsKey(i))
				wantsToConnectUserIDsToAsyncResponseAndClientID.get(i).x.resume(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}

	public void resolveHostCommand(SocketGameRequest request){
		if((request.acceptedUsers != null && request.acceptedUsers.size() > 0) || request.rejectedUsers != null && request.rejectedUsers.size() > 0) {
			respondForConnectPermission(request.acceptedUsers, request.rejectedUsers);
		}
	}

	public void resolvePlayerCommand(SocketGameRequest request){

	}

}
