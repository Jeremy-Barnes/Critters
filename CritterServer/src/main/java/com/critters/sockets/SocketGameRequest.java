package com.critters.sockets;

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

	/*** Everybody Elements! ***/
	public String broadCastMessage;
	public boolean leaveLobby;

	//todo figure out how to transmit game data
}
