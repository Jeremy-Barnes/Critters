package com.critters.spaceinvaders.entities.projectiles;

import static com.critters.spaceinvaders.graphics.Render.sr;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Laser extends Projectile {

	protected boolean hit;
	private int time = 20;

	public Laser(Level level, Entity owner, Vector2f pos, Vector2f size, Vector2f vel) {
		super(level, owner, pos, size, vel);
	}

	@Override
	protected void checkCollisions() {
		if (hit)
			return;

		ArrayList<Collidable> collidables = level.getCollidables();

		for (Collidable c : collidables) {
			if (c.getRectangle().intersectsRect(this.rectangle) && c != owner && c != this) {
				c.hit(this);
				hit = true;
			}
		}
	}

	@Override
	public void update() {
		super.update();

		if (--time < 0) {
			level.removeEntity(this);
		}
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.15f, 0.15f, 0.80f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}
	
}
