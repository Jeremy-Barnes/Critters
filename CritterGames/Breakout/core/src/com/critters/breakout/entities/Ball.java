package com.critters.breakout.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.math.Vector2f;
import com.critters.breakout.math.Circle;

import static com.critters.breakout.Level.level;
import static com.critters.breakout.math.Rectangle.HORRIZONTAL;
import static com.critters.breakout.math.Rectangle.NO_INTERSECTION;
import static com.critters.breakout.math.Rectangle.VERTICAL;

import java.util.ArrayList;
import java.util.Random;

public class Ball extends Entity {

	private float radius;

	private Circle circle;

	private Vector2f vel;

	public Ball(Vector2f pos, float radius) {
		super(pos);
		this.radius = radius;

		circle = new Circle(pos, radius);

		vel = new Vector2f();
	}

	private void checkIntersections() {
		ArrayList<Collidable> blocks = level.getCollidables();
		for (Collidable b : blocks) {
					
			int result = b.getRectangle().intersectsCircle(circle);
			if (result != NO_INTERSECTION) {
				b.hit();

				// Bounce
				if (result == HORRIZONTAL) {
					vel = vel.mul(1, -1);
					return;
				} else if (result == VERTICAL) {
					vel = vel.mul(-1, 1);
					return;
				}

			}
		}
	}

	public void launch() {
		Random random = new Random();
		vel = new Vector2f(random.nextFloat() - 0.5f, random.nextFloat());
		vel = vel.normal().mul(3);
	}

	@Override
	public void update() {
		// pos.x = Gdx.input.getX();
		// pos.y = Gdx.graphics.getHeight() - Gdx.input.getY();

		pos = pos.add(vel);

		circle.updateCenter(pos);

		checkIntersections();
	}

	@Override
	public void render(SpriteBatch render) {
		ShapeRenderer sr = new ShapeRenderer();
		sr.begin(ShapeType.Filled);
		sr.setColor(0, 1, 0, 1);
		sr.circle(pos.x, pos.y, radius);
		sr.end();
	}

}
