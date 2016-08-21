package com.critters.breakout.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.math.Rectangle;
import com.critters.breakout.math.Vector2f;

import static com.critters.breakout.Level.level;

public class Block extends Collidable {

	public Block(Vector2f pos, Vector2f size) {
		super(pos, size);
		this.size = size;

		rectangle = new Rectangle(pos, pos.add(size));
	}

	@Override
	public void update() {
		if (hit)
			level.removeEntity(this);
	}

	@Override
	public void render(SpriteBatch render) {
		sr.begin(ShapeType.Filled);
		sr.setColor(1, 0, 0, 1);
		if (checked) {
			sr.setColor(0, 0, 1, 1);
			checked = false;
		}
		sr.rect(pos.x, pos.y, size.x, size.y);
		sr.end();
	}

	@Override
	public void hit() {
		hit = true;
	}
}
