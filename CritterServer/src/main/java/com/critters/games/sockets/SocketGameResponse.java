package com.critters.games.sockets;


import com.critters.games.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 7/17/2017.
 */
public class SocketGameResponse {

	/*** Initialization variables ***/
	public boolean startTickingNow;
	public int assignedInstanceId;

	/*** Game state variables ***/
	public int tickNumber;

	public List<GameObject> deltaObjects= new ArrayList();
	public List<Player> deltaPlayers = new ArrayList();

	public String broadCastMessage;
	public String broadCaster;

}
