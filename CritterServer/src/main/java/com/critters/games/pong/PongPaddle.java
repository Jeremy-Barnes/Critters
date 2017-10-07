package com.critters.games.pong;

import com.critters.games.GameObject;

import java.awt.geom.Rectangle2D;

/**
 * Created by Jeremy on 10/3/2017.
 */
public class PongPaddle extends GameObject {

	public PongPaddle() {
		super.ENTITY_TYPE_ID = 1;
	}
	public final float PADDLE_HEIGHT = 12;
	public final float PADDLE_VELOCITY = 0.025f;//25 world units per second

	public Rectangle2D boundingBox;

}
