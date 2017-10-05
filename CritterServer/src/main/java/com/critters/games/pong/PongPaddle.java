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
	public final int PADDLE_HEIGHT = 12;
	public final int PADDLE_VELOCITY = 25;

	public Rectangle2D boundingBox;

}
