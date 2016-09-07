package com.critters.spaceinvaders.entities.mobs;

import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.math.Vector2f;

public abstract class Enemy extends Collidable {

	public Enemy(Vector2f pos, Vector2f size) {
		super(pos, size);
	}

}
