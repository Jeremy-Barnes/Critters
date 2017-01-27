package com.critters.flappingbird.entities.powerup;

import com.badlogic.gdx.graphics.Color;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

public class PowerupShield extends Powerup {

	public PowerupShield(Level level, Vector2f pos, int length) {
		super(level, pos, length);
		color = Color.ORANGE;
	}

}
