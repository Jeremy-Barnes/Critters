package com.critters.spaceinvaders.entities;

import static com.critters.spaceinvaders.graphics.Render.sr;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.math.Vector2f;

public class Wall extends Collidable {

	public Wall(Vector2f pos, Vector2f size) {
		super(pos, size);
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
