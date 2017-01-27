package com.critters.spaceinvaders.entities.obstacles;

import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.entities.mobs.Alien;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Shield extends Wall {

	public Shield(Level level, Vector2f pos, Vector2f size) {
		super(level, pos, size);
	}

	@Override
	public void update() {
		if (hitCount >= 1) {
			level.removeEntity(this);
		}

		for (int i = 0; i < level.getEntities().size(); i++) {
			Entity e = level.getEntities().get(i);
			if (e instanceof Alien && ((Alien) e).getRectangle().intersectsRect(rectangle))
				level.removeEntity(this);
		}
	}

}
