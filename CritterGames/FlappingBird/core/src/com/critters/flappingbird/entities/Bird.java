package com.critters.flappingbird.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.critters.flappingbird.entities.powerup.Powerup;
import com.critters.flappingbird.entities.powerup.PowerupShield;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.input.Input;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Rectangle;
import com.critters.flappingbird.math.Vector2f;

public class Bird extends Entity {

	public static final float G = -25;

	private Vector2f vel;
	private Vector2f size;

	private Rectangle rect;

	private boolean shielded;
	private boolean dead;
	private float invincible;

	public Bird(Level level, Vector2f pos) {
		super(level, pos);
		vel = new Vector2f(3, 0);
		size = new Vector2f(50, 50);
		rect = new Rectangle(pos, pos.add(size));
	}

	private void checkPowerups() {
		ArrayList<Powerup> powerups = level.getPowerups();
		for (Powerup p : powerups) {
			if (rect.intersectsRect(p.rect)) {
				p.pickUp();
			}
		}
	}

	private void checkActivePowerups() {
		shielded = Powerup.exists(PowerupShield.class, level);
	}

	private void checkCollisions() {
		invincible -= Gdx.graphics.getDeltaTime();

		ArrayList<Collidable> collidables = level.getCollidables();
		for (Collidable c : collidables) {

			Vector2f intersetcion = null;

			if (c.intersectsPoint(pos.add(size))) {
				dead = true;
				intersetcion = pos.add(size);
			}
			if (c.intersectsPoint(pos)) {
				dead = true;
				intersetcion = pos;
			}
			if (c.intersectsPoint(pos.add(size.mul(0, 1)))) {
				dead = true;
				intersetcion = pos.add(size.mul(0, 1));
			}
			if (c.intersectsPoint(pos.add(size.mul(1, 0)))) {
				dead = true;
				intersetcion = pos.add(size.mul(1, 0));
			}

			if (dead && (shielded || invincible > 0)) {
				dead = false;

				// Remove all shield powerups
				Powerup.removeAll(PowerupShield.class, level);

				if (shielded)
					invincible = 1;

				Vector2f direction = c.getDirection(intersetcion);
				if (direction.y < 0)
					jump();
			}
		}
	}

	private void jump() {
		vel = new Vector2f(vel.x, 10);
	}

	private void move() {
		vel = vel.add(new Vector2f(0, G).mul(Gdx.graphics.getDeltaTime()));
		pos = pos.add(vel);
		rect.update(pos, pos.add(size));

		if (Input.ready()) {
			Input.inputs.remove(0);
			jump();
		}
	}

	@Override
	public void update() {
		move();
		checkCollisions();
		checkPowerups();
		checkActivePowerups();
	}

	@Override
	public void render(Render render) {
		Color c = Color.GREEN;
		if (shielded || (invincible > 0 && invincible * 2 % 1 < 0.5))
			c = Color.ORANGE;

		render.drawRectangle(c, pos.x, pos.y, size.x, size.y);
	}

	public boolean isDead() {
		return dead;
	}

}
