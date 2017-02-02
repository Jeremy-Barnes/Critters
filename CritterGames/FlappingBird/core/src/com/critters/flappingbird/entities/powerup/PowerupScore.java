package com.critters.flappingbird.entities.powerup;

import com.badlogic.gdx.graphics.Color;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

public class PowerupScore extends Powerup {

	public PowerupScore(Level level, Vector2f pos) {
		super(level, pos, 0);
		color = Color.MAGENTA;
	}

	@Override
	public void pickUp() {
		super.pickUp();

		level.addScore(5);
	}
}
