package com.critters.spaceinvaders.entities.mobs;

import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.entities.projectiles.Projectile;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public abstract class Enemy extends Collidable {

	public Enemy(Level level, Vector2f pos, Vector2f size) {
		super(level, pos, size);
	}
	
	@Override
	public void hit(Projectile projectile) {
		super.hit(projectile);
		level.removeEntity(this);
	}

}
