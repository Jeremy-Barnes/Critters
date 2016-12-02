package com.critters.breakout.entities.powerup;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.math.Vector2f;

public class PowerupSlowBall extends Powerup {

	public PowerupSlowBall(Vector2f pos) {
		super(pos, 15);
		color = new Color(0x3498DBff);
	}

}
