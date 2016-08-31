package com.critters.breakout.entities;

import static com.critters.breakout.graphics.Render.sr;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.math.Rectangle;
import com.critters.breakout.math.Vector2f;

public abstract class Block extends Collidable {

	protected Color color;

	public Block(Vector2f pos, Vector2f size) {
		super(pos, size);
		this.size = size;

		rectangle = new Rectangle(pos, pos.add(size));

		color = new Color(1, 0, 0, 1);
	}

	@Override
	public abstract void update();

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(color);
		/*
		 * Debug code for ball proximity if (checked) { sr.setColor(0, 0, 1, 1); checked = false; }
		 */
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

	@Override
	public abstract void hit();
}
