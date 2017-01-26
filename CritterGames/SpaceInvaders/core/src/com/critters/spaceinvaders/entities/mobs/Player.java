package com.critters.spaceinvaders.entities.mobs;

import static com.critters.spaceinvaders.graphics.Render.sr;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Collidable;
<<<<<<< HEAD
=======
import com.critters.spaceinvaders.entities.Entity;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
import com.critters.spaceinvaders.entities.powerup.Powerup;
import com.critters.spaceinvaders.entities.powerup.PowerupLaser;
import com.critters.spaceinvaders.entities.powerup.PowerupLife;
import com.critters.spaceinvaders.entities.projectiles.Laser;
import com.critters.spaceinvaders.entities.projectiles.Projectile;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Player extends Collidable {

	enum InputMode {
		POINTER, KEYBOARD;
	}

	// Movement of the pad
	protected float goalX;
<<<<<<< HEAD
	protected final float MAX_VEL = 5;
	protected final float DEFAULT_SIZE;

	protected int SHOOT_TIME = 20;
	protected int currentShootTime = 0;

	protected InputMode mode;

	protected int laserShots = 50;
=======
	protected final float MAX_VEL = 300; // Per seconds
	protected final float DEFAULT_SIZE;

	protected float SHOOT_TIME = 0.4f; // In seconds
	protected float currentShootTime = 0;

	protected InputMode mode;

	protected int laserShots = 2;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

	protected int healthPoints = 5;

	public Player(Level level, Vector2f pos, Vector2f size) {
		super(level, pos, size);
		rectangle.onlyBottom = true;

		mode = InputMode.KEYBOARD;
		goalX = pos.x;
		DEFAULT_SIZE = size.x;
	}

	protected void processInput() {

		// Switching between modes
		if (Gdx.input.isTouched() || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			mode = InputMode.POINTER;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)
				|| Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			mode = InputMode.KEYBOARD;
		}

		switch (mode) {
		case POINTER:
			float xRatio = Gdx.graphics.getWidth() / 640f;

			goalX = Gdx.input.getX() / xRatio - size.x / 2;
			break;

		case KEYBOARD:
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
				goalX += MAX_VEL;
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
				goalX -= MAX_VEL;
			break;
		}

		// Shoot if there was a click
		boolean input = Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
		if (input && currentShootTime <= 0) {
			if (laserShots <= 0)
				level.addEntity(new Projectile(level, this, new Vector2f(rectangle.getCenter()), new Vector2f(5, 15),
<<<<<<< HEAD
						new Vector2f(0, 3)));
=======
						new Vector2f(0, 180)));
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
			else {
				laserShots--;
				level.addEntity(new Laser(level, this, new Vector2f(rectangle.getCenter()), new Vector2f(2, 3000),
						new Vector2f()));
			}
<<<<<<< HEAD
=======

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
			currentShootTime = SHOOT_TIME;
		}
	}

	protected void checkPowerups() {
		ArrayList<Powerup> powerups = level.getPowerups();
		for (Powerup p : powerups) {
			if (rectangle.intersectsRect(p.rect)) {
				p.pickUp();
			}
		}

	}

	@Override
	public void hit(Projectile projectile) {
<<<<<<< HEAD
=======
		System.out.println(healthPoints);
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
		healthPoints--;

		if (healthPoints <= 0) {
			// You lose - too bad
			level.removeEntity(this);
		}
	}

	protected void checkActivePowerups() {
		if (Powerup.exists(level, PowerupLife.class)) {
			healthPoints++;

			// Remove the powerup that was used to give hp
			level.getPowerup(PowerupLife.class).deactivate();
<<<<<<< HEAD
		}

		if (Powerup.exists(level, PowerupLaser.class)) {
			laserShots = 2;
=======
			;
		}

		if (Powerup.exists(level, PowerupLaser.class)) {

			// TODO Activate laser

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
			// Remove the powerup that was used to give hp
			level.getPowerup(PowerupLaser.class).deactivate();
		}
	}

	@Override
	public void update() {
		processInput();

		// Speed limitation
		float deltaX = Math.abs(pos.x - goalX);
<<<<<<< HEAD
		if (deltaX < MAX_VEL) {
			pos.x = goalX;
		} else {
			pos.x += (pos.x - goalX) > 0 ? -MAX_VEL : MAX_VEL;
		}

		// Shoot timer
		currentShootTime--;
=======
		if (deltaX < MAX_VEL * Gdx.graphics.getDeltaTime()) {
			pos.x = goalX;
		} else {
			float dx = (pos.x - goalX) > 0 ? -MAX_VEL : MAX_VEL;
			pos.x += dx * Gdx.graphics.getDeltaTime();
		}

		// Shoot timer
		currentShootTime -= Gdx.graphics.getDeltaTime();
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

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

<<<<<<< HEAD
=======
		for (int i = 0; i < level.getEntities().size(); i++) {
			Entity e = level.getEntities().get(i);
			if (e instanceof Alien && ((Alien) e).getRectangle().intersectsRect(rectangle))
				this.hit(null);
		}

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
		// Check for intersecting of entity powerups
		checkPowerups();
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.75f, 0.25f, 0.25f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);

		sr.setColor(0.75f, 0f, 0f, 1);
		for (int i = 0; i < healthPoints; i++) {
			sr.rect(i * 12 + 10, 10, 10, 10);
		}
	}

<<<<<<< HEAD
}
=======
}
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
