package com.critters.spaceinvaders.entities.mobs;

import static com.critters.spaceinvaders.graphics.Render.sr;
import static com.critters.spaceinvaders.level.Level.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.entities.projectiles.Projectile;
import com.critters.spaceinvaders.math.Vector2f;

public class Player extends Collidable {

	enum InputMode {
		POINTER, KEYBOARD;
	}

	// Movement of the pad
	private float goalX;
	private final float MAX_VEL = 5;
	private final float DEFAULT_SIZE;

	private InputMode mode;

	public Player(Vector2f pos, Vector2f size) {
		super(pos, size);
		rectangle.onlyBottom = true;

		mode = InputMode.KEYBOARD;
		goalX = pos.x;
		DEFAULT_SIZE = size.x;
	}

	private void processInput() {

		// Switching between modes
		if (Gdx.input.isTouched() || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			mode = InputMode.POINTER;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			mode = InputMode.KEYBOARD;
		}

		switch (mode) {
		case POINTER:
			goalX = Gdx.input.getX() - size.x / 2;
			break;

		case KEYBOARD:
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
				goalX += MAX_VEL;
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
				goalX -= MAX_VEL;
			break;
		}

		// Shoot if there was a click
		if (Gdx.input.justTouched()) {
			level.addEntity(new Projectile(this, new Vector2f(rectangle.getCenter()), new Vector2f(5, 15), new Vector2f(0, 3)));
		}
	}

	private void checkPowerups() {
		/*
		 * ArrayList<Powerup> powerups = level.getPowerups(); for (Powerup p : powerups) { if (rectangle.intersectsRect(p.rect)) { p.pickUp(); } }
		 */
	}

	private void checkActivePowerups() {

	}

	@Override
	public void update() {
		processInput();

		// Speed limitation
		float deltaX = Math.abs(pos.x - goalX);
		if (deltaX < MAX_VEL) {
			pos.x = goalX;
		} else {
			pos.x += (pos.x - goalX) > 0 ? -MAX_VEL : MAX_VEL;
		}

		// Update the size with the active effects
		checkActivePowerups();

		// Position limitation
		if (pos.x < 0)
			pos.x = 0;
		if (pos.x + size.x > level.LEVEL_WIDTH)
			pos.x = level.LEVEL_WIDTH - size.x;

		// Goal limitation
		if (goalX < 0)
			goalX = 0;
		if (goalX + size.x > level.LEVEL_WIDTH)
			goalX = level.LEVEL_WIDTH - size.x;

		// Collision update
		rectangle.update(pos, pos.add(size));

		// Check for intersecting of entity powerups
		checkPowerups();
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.75f, 0.25f, 0.25f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
