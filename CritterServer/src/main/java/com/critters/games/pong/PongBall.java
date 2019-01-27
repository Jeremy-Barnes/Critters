package com.critters.games.pong;

import com.critters.games.GameObject;

/**
 * Created by Jeremy on 10/3/2017.
 */
public class PongBall extends GameObject {

	public PongBall() {
		super.ENTITY_TYPE_ID = PongController.PongEntityTypes.Ball.ordinal();
	}
	public final float BALL_DIAMETER = 20;
	public final float BALL_VELOCITY = .75f;//750 world units per second

}
