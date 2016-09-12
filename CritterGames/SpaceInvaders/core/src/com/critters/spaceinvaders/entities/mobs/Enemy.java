package com.critters.spaceinvaders.entities.mobs;

import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.entities.projectiles.Projectile;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public abstract class Enemy extends Collidable {

	protected int hitPoints;

	public Enemy(Level level, Vector2f pos, Vector2f size) {
		this(level, pos, size, 1);
	}

	public Enemy(Level level, Vector2f pos, Vector2f size, int hp) {
		super(level, pos, size);
		hitPoints = hp;
	}

	@Override
	public void hit(Projectile projectile) {
		super.hit(projectile);

		if (hitCount >= hitPoints) {
			level.removeEntity(this);
		}
	}

}
