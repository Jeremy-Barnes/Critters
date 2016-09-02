package com.critters.breakout.entities.powerup;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.math.Vector2f;

import static com.critters.breakout.graphics.Render.sr;
import static com.critters.breakout.level.Level.level;

public class Powerup extends Entity {

	public Powerup(Vector2f pos) {
		super(pos);
	}

	@Override
	public void update() {
		pos.y--;

		if (pos.y < -20)
			level.removeEntity(this);
	}

	@Override
	public void render(SpriteBatch render) {
		sr.triangle(pos.x - 7.5f, pos.y - +6.5f, pos.x + 7.5f, pos.y - +6.5f, pos.x, pos.y + 6.5f);
	}

}
