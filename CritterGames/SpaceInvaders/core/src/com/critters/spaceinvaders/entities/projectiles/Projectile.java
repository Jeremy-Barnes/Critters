package com.critters.spaceinvaders.entities.projectiles;

import static com.critters.spaceinvaders.graphics.Render.sr;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.math.Vector2f;

public class Projectile extends Collidable {

	protected Vector2f vel;

	public Projectile(Vector2f pos, Vector2f size, Vector2f vel) {
		super(pos, size);
		this.vel = vel;
	}

	@Override
	public void update() {
		pos = pos.add(vel);
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.75f, 0.75f, 0.75f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}