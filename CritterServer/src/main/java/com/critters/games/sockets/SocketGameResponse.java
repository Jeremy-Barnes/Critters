package com.critters.games.sockets;


import com.critters.games.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 7/17/2017.
 */
public class SocketGameResponse {

	List<Player> connectToGameRequesters;

	/*** Initialization variables ***/
	public boolean startTickingNow;
	public boolean tickingPaused;
	public boolean gameOver;

	public int assignedInstanceId;

	public String notificationBody;
	public String notificationTitle;
	public String notificationHTML;
	public String dangerButtonText;
	public String noButtonText;
	public String notificationID;

	/*** Game state variables ***/
	public int tickNumber;
	public long startTime;
	public long currentTime;

	public List<GameObject> deltaObjects= new ArrayList();
	public List<Player> deltaPlayers = new ArrayList();

	public String broadCastMessage;
	public String broadCaster;

	public boolean ping;

}
