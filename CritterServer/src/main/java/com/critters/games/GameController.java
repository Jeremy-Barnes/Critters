package com.critters.games;

import com.critters.Utilities.Serializer;
import com.critters.dal.entity.User;
import com.critters.games.sockets.Player;
import com.critters.games.sockets.SocketGameRequest;
import com.critters.games.sockets.SocketGameResponse;
import com.critters.games.sockets.SocketManager;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeremy on 7/17/2017.
 */
public abstract class GameController implements Runnable {

	public volatile String gameID;
	public volatile String title;
	public volatile int gameType;

	@XmlTransient public volatile String hostID;
	public volatile Player host;
	private Thread gameThread;

	//game lifetime variables
	private volatile boolean tickGame = false;
	private volatile boolean shutDown = false;
	public long lastTime;

	@XmlTransient public volatile Map<String,Player> clientIDToPlayer = Collections.synchronizedMap(new HashMap<String, Player>());
	@XmlTransient public HashMap<String, RequestHandler> outstandingRequestToHandler = new HashMap<String, RequestHandler>();

	public GameController(){};

	public GameController(String gameID, String title, String hostClientID, int gameType) {
		this.gameID = gameID;
		this.title = title;
		this.hostID = hostClientID;
		this.gameType = gameType;
		this.gameThread = new Thread(this);
	}

	private void start(){
		try {
			Thread.sleep(1000);
		} catch(Exception e){}
		this.initializeGame();
		try {
			Thread.sleep(1000);
		} catch(Exception e){}
		this.tickGame = true;
		gameThread.start();
	}

	/***
	 * Game logic tick, advance gamestate
	 */
	public abstract void tick(int dT);

	public void run(){
		lastTime = System.currentTimeMillis();
		long tickMS;
		while(tickGame) {
			tickMS = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			tick((int)tickMS);
		}
		if(shutDown){
			disconnectAllParties();
		}
	}

	public void addPlayer(Player player){
		if(player.clientID.equalsIgnoreCase(hostID)) {
			host = player;
		}
		if(host != null && !clientIDToPlayer.containsKey(player.clientID)) {
			clientIDToPlayer.put(player.clientID,player);
		}
		SocketGameResponse updateLobby = new SocketGameResponse();
		updateLobby.deltaPlayers.add(player);
		updateLobby.broadCastMessage = player.user.getUserName() + " has joined.";
		clientIDToPlayer.values().forEach(p -> p.sendMessage(updateLobby));
		//todo add player in ongoing game??
	}

	public void askForConnectPermission(String clientID, User user, AsyncResponse asyncResponse) {
		SocketGameResponse connectionRequest = new SocketGameResponse();
		connectionRequest.notificationBody = "Can " + user.getUserName() + " play???";
		connectionRequest.noButtonText = "No";
		connectionRequest.notificationID = System.currentTimeMillis() + "";
		try {
			host.websocket.getBasicRemote().sendText(Serializer.toJSON(false, connectionRequest, SocketGameResponse.class));
		} catch (Exception e){
			host.websocket.getAsyncRemote().sendText(Serializer.toJSON(false, connectionRequest, SocketGameResponse.class));
		}

		outstandingRequestToHandler.put(connectionRequest.notificationID, new RequestHandler(this) {
			public void handle(SocketGameRequest request) {
				if (request.notificationResponse) {
					asyncResponse.resume(Response.status(Response.Status.OK).build());
					SocketManager.clientIDToPlayer.get(clientID).y = this.controller;
				} else {
					asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED).build());
				}
				outstandingRequestToHandler.remove(request.notificationID);
			}
		});
	}

	public void resolveCommand(SocketGameRequest request, Player player){
		if(request.notificationID != null) outstandingRequestToHandler.get(request.notificationID).handle(request);
		if(player.clientID.equalsIgnoreCase(hostID)) {
			resolveHostCommand(request, player);
		} else {
			resolvePlayerCommand(request, player);
		}
	}

	public void gameIsOver() {
		this.tickGame = false;
		this.handlePostGameEvents();
	}

	public void resolveHostCommand(SocketGameRequest request, Player player){
		if(request.endGame || request.endLobby){
			this.tickGame = false;
			this.shutDown = request.endLobby; //handled on next tick
		} else if(request.startGame && !this.tickGame){
			start();
		}
		resolvePlayerCommand(request, player);
	}

	public abstract void resolvePlayerCommand(SocketGameRequest request, Player player);

	private void disconnectAllParties(){
		//todo kick everyone out, close sessions, remove from hashmaps
	}

	public void forceKill(){
		this.tickGame = false;
		this.disconnectAllParties();
	}

	public abstract void initializeGame();

	public abstract void handlePostGameEvents();

	public static class RequestHandler {

		public RequestHandler() {}

		@XmlTransient protected GameController controller;
		RequestHandler(GameController closure){ this.controller = closure;}
		void handle(SocketGameRequest request){}
	}

}
