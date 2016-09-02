package com.critters.breakout.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.math.Vector2f;
import static com.critters.breakout.graphics.Render.sr;

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
		sr.setColor(new Color(0x2C3E50ff));
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
