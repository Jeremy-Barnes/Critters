package com.critters.breakout.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.math.Rectangle;
import com.critters.breakout.math.Vector2f;

public class Block extends Entity {
	private Vector2f size;

	private Rectangle rectangle;

	public Block(Vector2f pos, Vector2f size) {
		super(pos);
		this.size = size;

		rectangle = new Rectangle(pos, pos.add(size));
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch render) {
		ShapeRenderer sr = new ShapeRenderer();
		sr.begin(ShapeType.Filled);
		sr.setColor(1, 0, 0, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
		sr.end();
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
}
