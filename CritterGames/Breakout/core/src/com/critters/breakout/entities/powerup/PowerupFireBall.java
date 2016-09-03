package com.critters.breakout.entities.powerup;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.math.Vector2f;

public class PowerupFireBall extends Powerup {

	public PowerupFireBall(Vector2f pos) {
		super(pos);
		color = new Color(0xC0392Bff);
	}

}
