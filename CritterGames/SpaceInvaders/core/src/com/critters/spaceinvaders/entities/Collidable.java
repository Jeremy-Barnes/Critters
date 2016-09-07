package com.critters.spaceinvaders.entities;

import com.critters.spaceinvaders.entities.projectiles.Projectile;
import com.critters.spaceinvaders.math.Rectangle;
import com.critters.spaceinvaders.math.Vector2f;
import static com.critters.spaceinvaders.level.Level.level;

public abstract class Collidable extends Entity {

	protected Vector2f size;
	protected boolean hit = false;

	protected Rectangle rectangle;
	public boolean checked;

	public Collidable(Vector2f pos, Vector2f size) {
		super(pos);
		this.size = size;
		rectangle = new Rectangle(pos, pos.add(size));
	}

	public void hit(Projectile projectile) {
		hit = true;
		level.removeEntity(this);
		level.removeEntity(projectile);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void destroy(Projectile projectile) {
		hit(projectile);
	}

}
