package com.critters.games.sockets;

import com.critters.games.GameObject;

import java.util.List;

/**
 * Created by Jeremy on 7/17/2017.
 */
public class SocketGameRequest {

	/*** Host Specific Elements, ignored for normal players ***/
	public List<Integer>  acceptedUsers;
	public List<Integer>  rejectedUsers;
	public boolean startGame;
	public boolean endGame;
	public boolean endLobby;
	public int tickCount;

	/*** Everybody Elements! ***/
	public String broadCastMessage;
	public boolean leaveLobby;
	/*** Movement components ***/
	public  String[] commands;
	public GameObject clientState;
	public boolean ping;

	public String notificationID;
	public boolean notificationResponse;
	//todo figure out how to transmit game data
}
