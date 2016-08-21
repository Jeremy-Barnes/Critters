package com.critters.breakout.entities;
import static com.critters.breakout.graphics.Render.sr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.math.Vector2f;

public class Pad extends Collidable {

	public Pad(Vector2f pos, Vector2f size) {
		super(pos, size);
	}

	@Override
	public void update() {
		pos.x = Gdx.input.getX() - size.x / 2;
		rectangle.update(pos, pos.add(size));
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.75f, 0.5f, 0.25f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
