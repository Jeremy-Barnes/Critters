package com.critters.spaceinvaders.entities.mobs;

import static com.critters.spaceinvaders.graphics.Render.sr;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.math.Vector2f;

public class Alien extends Enemy {

	public Alien(Vector2f pos, Vector2f size) {
		super(pos, size);
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.25f, 0.75f, 0.25f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
