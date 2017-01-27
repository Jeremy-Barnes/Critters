package com.critters.breakout.entities;

import static com.critters.breakout.graphics.Render.sr;
import static com.critters.breakout.level.Level.level;
import static com.critters.breakout.math.Rectangle.HORRIZONTAL;
import static com.critters.breakout.math.Rectangle.NO_INTERSECTION;
import static com.critters.breakout.math.Rectangle.VERTICAL;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.entities.blocks.Block;
import com.critters.breakout.entities.blocks.BlockIndestructible;
import com.critters.breakout.entities.blocks.BlockMulti;
import com.critters.breakout.entities.blocks.BlockVoid;
import com.critters.breakout.entities.powerup.Powerup;
import com.critters.breakout.entities.powerup.PowerupBigPaddle;
import com.critters.breakout.entities.powerup.PowerupFireBall;
import com.critters.breakout.entities.powerup.PowerupSlowBall;
import com.critters.breakout.math.Circle;
import com.critters.breakout.math.Vector2f;

public class Ball extends Entity {

	private float radius;
	private Circle circle;

	private Vector2f vel;
<<<<<<< HEAD
	private final float MAX_VEL_DEFAULT = 4;
=======
	private final float MAX_VEL_DEFAULT = 250; // Per sec
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	private float maxVel;

	private boolean fireBall = false;

	// Timers for bounce
	private int h_time;
	private int v_time;

	public Ball(Vector2f pos, float radius) {
		super(pos);
		this.radius = radius;

		circle = new Circle(pos, radius);

		vel = new Vector2f();

		maxVel = MAX_VEL_DEFAULT;
	}

	private void checkActivePowerups() {
		if (Powerup.exists(PowerupSlowBall.class)) {
			int count = Powerup.count(PowerupSlowBall.class);
			maxVel = MAX_VEL_DEFAULT * (float) Math.pow(0.5f, count);
		} else {
			maxVel = MAX_VEL_DEFAULT;
		}

		if (Powerup.exists(PowerupFireBall.class)) {
			fireBall = true;
		} else {
			fireBall = false;
		}
	}

	/**
	 * Checks if the ball is intersecting anything and bounces in the correct direction.
	 * 
	 * Also makes sure not to bounce if fireball is active or it's passing through a block
	 */
	private void checkIntersections() {
		ArrayList<Collidable> blocks = level.getCollidables();
		for (Collidable b : blocks) {
			if ((b.pos.x - pos.x) * (b.pos.x - pos.x) > b.size.x * b.size.x * 2)
				continue;

			if ((b.pos.y - pos.y) * (b.pos.y - pos.y) > b.size.y * b.size.y * 2)
				continue;
			b.checked = true;

			int result = b.getRectangle().intersectsCircle(circle);

			if (result != NO_INTERSECTION) {

				// Destroy the block if it's a void block or if fireball is active
				if (b instanceof BlockVoid || fireBall) {
					destroyBlock(b);
					b.destroy();
				} else {
					// Else just hit the block
					b.hit();
					hitBlock(b);
				}

				// If fireball active and blockmulti was hit, don't bounce. Also don't bounce on void blocks
				if ((b instanceof BlockMulti && fireBall) || b instanceof BlockVoid)
					continue;

				// Bounce
				if (result == HORRIZONTAL && h_time == 0) {
					h_time = 3;

					if (b instanceof Pad) {
						float x = (((pos.x - b.pos.x) / b.size.x) - 0.5f);
						vel = vel.mul(1, -1);
						vel.x = x * 3;
						normalVel();
					} else {
						vel = vel.mul(1, -1);
					}

					return;
				} else if (result == VERTICAL && v_time == 0) {
					v_time = 3;
					vel = vel.mul(-1, 1);
					return;
				}

			}
		}
	}

	/**
	 * Method that gets triggered when the ball collides with a collidable. Increases the score.
	 * 
	 * @param b
	 *            the collidable that was hit
	 */
	private void hitBlock(Collidable b) {
		if (b instanceof Block && !(b instanceof BlockIndestructible))
			level.score++;
	}

	/**
	 * Method that gets triggered when the ball collides with a collidable AND fireball effect is active.
	 * 
	 * @param b
	 *            the collidable that is to be destroyed
	 */
	private void destroyBlock(Collidable b) {
		if (b instanceof BlockMulti)
			level.score += ((BlockMulti) b).hitsLeft();
	}

	/**
	 * Method gets called with first click on the screen. The ball launches in a random direction from the start position
	 */
	public void launch() {
		Random random = new Random();
		vel = new Vector2f(random.nextFloat() - 0.5f, 1);
		normalVel();
	}

	public void normalVel() {
<<<<<<< HEAD
		vel = vel.normal().mul(maxVel);
=======
		vel = vel.normal().mul(maxVel * Gdx.graphics.getDeltaTime());
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	}

	@Override
	public void update() {
		if (h_time > 0)
			h_time--;
		if (v_time > 0)
			v_time--;

		// Check for active powerups
		checkActivePowerups();

		normalVel();
		pos = pos.add(vel);

		circle.updateCenter(pos);

		checkIntersections();
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0, 1, 0, 1);
		sr.circle(pos.x, pos.y, radius);
	}

}
