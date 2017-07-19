package com.critters.games;

import com.critters.dal.dto.entity.User;
import com.critters.sockets.Player;
import com.critters.sockets.SocketGameRequest;
import com.critters.sockets.SocketManager;
import com.critters.sockets.Threeple;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

/**
 * Created by Jeremy on 7/17/2017.
 */
public abstract class GameController implements Runnable {

	public volatile String gameID;
	public volatile String title;
	public volatile int gameType;

	public volatile String hostID;
	public volatile Player host;
	Thread gameThread;

	//game lifetime variables
	private volatile boolean tickGame = false;
	private volatile boolean shutDown = false;
	private long lastTime;

	@XmlTransient
	public volatile Map<String,Player> clientIDToPlayer = Collections.synchronizedMap(new HashMap<String, Player>());
	public HashMap<Integer,Threeple<AsyncResponse, String, User>> wantsToConnectUserIDsToAsyncResponseAndClientID = new HashMap<>();

	public GameController(String gameID, String title, String hostClientID, int gameType) {
		this.gameID = gameID;
		this.title = title;
		this.hostID = hostClientID;
		this.gameType = gameType;
		gameThread = new Thread(this);
	}

	private void start(){
		List<Player> playerList = new ArrayList<Player>();
		playerList.addAll(clientIDToPlayer.values());
		if(!wantsToConnectUserIDsToAsyncResponseAndClientID.keySet().isEmpty() || clientIDToPlayer.values().contains(null)) {
			return; //todo warn user someone still wants to join
		}

		this.tickGame = true;//there probably needs to be some kind of special game initialization stuff here, not sure.
		gameThread.start();
	}

	/***
	 * Game logic tick, advance gamestate
	 */
	public abstract void tick(long dT);

	public void run(){
		lastTime = System.currentTimeMillis();
		long tickMS;
		while(tickGame) { //todo calculate dT and limit this to only update on a reasonable timescale
			tickMS = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			tick(tickMS);
			try {
				Thread.sleep(100);
			} catch(Exception e){}
		}
		if(shutDown){
			disconnectAllParties();
		}
	}

	public void addPlayer(Player player){
		if(player.clientID.equalsIgnoreCase(hostID)) {
			host = player;
		}
		if(host != null && clientIDToPlayer.containsKey(player.clientID)) {
			clientIDToPlayer.put(player.clientID,player);
		}
	}

	public void askForConnectPermission(String clientID, User user, AsyncResponse asyncResponse) {
		wantsToConnectUserIDsToAsyncResponseAndClientID.put(user.getUserID(), new Threeple<AsyncResponse, String, User>(asyncResponse, clientID, user));
		//host.websocket.getAsyncRemote().sendText("Can " + user.getUserName() + " play???"); //todo put this into a real thing
		host.websocket.getAsyncRemote().sendText(user.getUserID()+""); //todo put this into a real thing

	}

	public void resolveCommand(SocketGameRequest request, Player player){
		if(player.clientID.equalsIgnoreCase(hostID)) {
			resolveHostCommand(request);
		} else {
			resolvePlayerCommand(request);
		}
	}

	public void resolveHostCommand(SocketGameRequest request){
		if((request.acceptedUsers != null && request.acceptedUsers.size() > 0) || request.rejectedUsers != null && request.rejectedUsers.size() > 0) {
			respondForConnectPermission(request.acceptedUsers, request.rejectedUsers);
		}
		if(request.endGame || request.endLobby){
			this.tickGame = false;
			this.shutDown = request.endLobby; //handled on next tick
		} else if(request.startGame && !this.tickGame){
			start();
		}
		resolvePlayerCommand(request);
	}

	public void resolvePlayerCommand(SocketGameRequest request){
//todo broadcast message
	}

	private void disconnectAllParties(){
		//todo kick everyone out, close sessions, remove from hashmaps
	}

	private void respondForConnectPermission(List<Integer> accepted, List<Integer> rejected){
		if(accepted != null)
			for(int i : accepted){
				if(wantsToConnectUserIDsToAsyncResponseAndClientID.containsKey(i)){
					Threeple<AsyncResponse, String, User> user = wantsToConnectUserIDsToAsyncResponseAndClientID.get(i);
					user.x.resume(Response.status(Response.Status.OK).build());
					clientIDToPlayer.put(user.y, null);
					wantsToConnectUserIDsToAsyncResponseAndClientID.remove(user.z.getUserID());
					SocketManager.clientIDToPlayer.get(user.y).y = this;
				}
			}
		if(rejected != null)
			for(int i : rejected){
				if(wantsToConnectUserIDsToAsyncResponseAndClientID.containsKey(i)) {
					Threeple<AsyncResponse, String, User> user = wantsToConnectUserIDsToAsyncResponseAndClientID.get(i);
					user.x.resume(Response.status(Response.Status.UNAUTHORIZED).build());
					wantsToConnectUserIDsToAsyncResponseAndClientID.remove(user.z.getUserID());
				}
			}
	}
}
