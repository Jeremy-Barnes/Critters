package com.critters.games.pong;

import com.critters.games.GameObject;

/**
 * Created by Jeremy on 10/3/2017.
 */
public class PongPaddle extends GameObject {

	public static final int ENTITY_TYPE_ID = 1;

	public static final int PADDLE_HEIGHT = 12;
	public static final int PADDLE_VELOCITY = 25;

	public int getEntityID() {
		return ENTITY_TYPE_ID;
	}

}
