package com.critters.games.sockets;


import com.critters.games.GameObject;

import java.util.List;

/**
 * Created by Jeremy on 7/17/2017.
 */
public class SocketGameResponse {

	public boolean startTickingNow;
	public int tickNumber;

	public List<GameObject> deltaObjects;
	public List<Player> deltaPlayers;

	public String broadCastMessage;
	public String broadCaster;

}
