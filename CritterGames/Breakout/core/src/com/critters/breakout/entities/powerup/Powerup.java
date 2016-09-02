package com.critters.breakout.entities.powerup;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.math.Vector2f;

import static com.critters.breakout.graphics.Render.sr;;

public class Powerup extends Entity {

	public Powerup(Vector2f pos) {
		super(pos);
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch render) {
		sr.triangle(pos.x, pos.y, pos.x + 15, pos.y, pos.x + 7.5f, pos.y + 13f);
	}

}
