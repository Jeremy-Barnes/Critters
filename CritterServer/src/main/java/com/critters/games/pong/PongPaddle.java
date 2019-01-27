package com.critters.games.pong;

import com.critters.games.GameObject;

import java.awt.geom.Rectangle2D;

/**
 * Created by Jeremy on 10/3/2017.
 */
public class PongPaddle extends GameObject {

	public PongPaddle() {
		super.ENTITY_TYPE_ID = PongController.PongEntityTypes.Paddle.ordinal();
	}
	public final float PADDLE_HEIGHT = 120;
	public final float PADDLE_VELOCITY = 0.75f;//250 world units per second

	public Rectangle2D boundingBox;

}
