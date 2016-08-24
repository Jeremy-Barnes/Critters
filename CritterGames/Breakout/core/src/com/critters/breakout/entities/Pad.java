package com.critters.breakout.entities;

import static com.critters.breakout.graphics.Render.sr;
import static com.critters.breakout.Level.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.Level;
import com.critters.breakout.math.Vector2f;

public class Pad extends Collidable {

	// Movement of the pad
	private float goalX;
	private float velX;
	private final float MAX_VEL = 5;

	public Pad(Vector2f pos, Vector2f size) {
		super(pos, size);
		rectangle.onlyBottom = true;
	}

	@Override
	public void update() {
		// Input collection
		goalX = Gdx.input.getX() - size.x / 2;

		// Speed limitation
		float deltaX = Math.abs(pos.x - goalX);
		if (deltaX < MAX_VEL) {
			pos.x = goalX;
		} else {
			pos.x += (pos.x - goalX) > 0 ? -MAX_VEL : MAX_VEL;
		}

		// Position limitation
		if (pos.x < level.WALL_SIZE)
			pos.x = level.WALL_SIZE;
		if (pos.x + size.x > level.LEVEL_WIDTH - level.WALL_SIZE)
			pos.x = level.LEVEL_WIDTH - level.WALL_SIZE - size.x;

		// Collision update
		rectangle.update(pos, pos.add(size));
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.75f, 0.5f, 0.25f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
