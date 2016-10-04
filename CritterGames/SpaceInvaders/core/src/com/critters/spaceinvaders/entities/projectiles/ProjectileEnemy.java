package com.critters.spaceinvaders.entities.projectiles;

import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

/**
 * This class might be obsolete .. We shall see.
 */
public class ProjectileEnemy extends Projectile {

	public ProjectileEnemy(Level level, Entity owner, Vector2f pos, Vector2f size, Vector2f vel) {
		super(level, owner, pos, size, vel);
	}

}
