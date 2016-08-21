package com.critters.breakout.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.math.Vector2f;
import com.critters.breakout.math.Circle;

import static com.critters.breakout.Level.level;

import java.util.ArrayList;

public class Ball extends Entity {

	private float radius;

	private Circle circle;

	public Ball(Vector2f pos, float radius) {
		super(pos);
		this.radius = radius;

		circle = new Circle(pos, radius);
	}

	private void checkIntersections() {
		ArrayList<Block> blocks = level.getBlocks();
		for (Block b : blocks) {
			if(b.getRectangle().intersectsCircle(circle)){
				System.out.println("Intersection!!!");
			}
		}
	}

	@Override
	public void update() {
		pos.x = Gdx.input.getX();
		pos.y = Gdx.graphics.getHeight() - Gdx.input.getY();

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
