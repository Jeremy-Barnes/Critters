package com.critters.breakout.entities;

import static com.critters.breakout.level.Level.level;
import static com.critters.breakout.graphics.Render.sr;
import static com.critters.breakout.math.Rectangle.HORRIZONTAL;
import static com.critters.breakout.math.Rectangle.NO_INTERSECTION;
import static com.critters.breakout.math.Rectangle.VERTICAL;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.math.Circle;
import com.critters.breakout.math.Vector2f;

public class Ball extends Entity {

	private float radius;
	private Circle circle;

	private Vector2f vel;

	// Timers for bounce
	private int h_time;
	private int v_time;

	public Ball(Vector2f pos, float radius) {
		super(pos);
		this.radius = radius;

		circle = new Circle(pos, radius);

		vel = new Vector2f();
	}

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
				b.hit();
				hitBlock(b);

				if (b instanceof BlockVoid) {
					continue;
				}

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
		if (b instanceof Block)
			level.score++;
	}

	public void launch() {
		Random random = new Random();
		vel = new Vector2f(random.nextFloat() - 0.5f, 1);
		vel = vel.normal().mul(3);
	}

	public void normalVel() {
		vel = vel.normal().mul(3);
	}

	@Override
	public void update() {
		if (h_time > 0)
			h_time--;
		if (v_time > 0)
			v_time--;

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
