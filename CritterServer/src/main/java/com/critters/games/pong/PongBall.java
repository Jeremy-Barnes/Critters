package com.critters.games.pong;

import com.critters.games.GameObject;

/**
 * Created by Jeremy on 10/3/2017.
 */
public class PongBall extends GameObject {

	public PongBall() {
		super.ENTITY_TYPE_ID = 0;
	}
	public final int BALL_DIAMETER = 2;
	public final int BALL_VELOCITY = 50;

}
