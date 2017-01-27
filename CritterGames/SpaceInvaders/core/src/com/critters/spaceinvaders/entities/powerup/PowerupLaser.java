package com.critters.spaceinvaders.entities.powerup;

import com.badlogic.gdx.graphics.Color;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class PowerupLaser extends Powerup{

	public PowerupLaser(Level level, Vector2f pos) {
		super(level, pos);
		color = Color.BLUE;
	}

}
