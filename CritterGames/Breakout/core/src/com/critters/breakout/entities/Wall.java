package com.critters.breakout.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.math.Vector2f;

public class Wall extends Collidable {

	public Wall(Vector2f pos, Vector2f size) {
		super(pos, size);
	}

	@Override
	public void hit() {

	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch render) {
		sr.begin(ShapeType.Filled);
		sr.setColor(0.5f, 0.5f, 0.5f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
		sr.end();
	}

}
