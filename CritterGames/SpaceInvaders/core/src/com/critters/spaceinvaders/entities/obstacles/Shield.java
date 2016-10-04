package com.critters.spaceinvaders.entities.obstacles;

import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Shield extends Wall {

	public Shield(Level level, Vector2f pos, Vector2f size) {
		super(level, pos, size);
	}

	@Override
	public void update() {

		if (hitCount >= 5) {
			level.removeEntity(this);
		}
	}

}
