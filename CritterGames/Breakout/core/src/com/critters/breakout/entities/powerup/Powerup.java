package com.critters.breakout.entities.powerup;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.math.Vector2f;

import static com.critters.breakout.graphics.Render.sr;
import static com.critters.breakout.level.Level.level;

public class Powerup extends Entity {

	// Probably a temp. color variable. Should get some sprites some time.
	protected Color color;

	public Powerup(Vector2f pos) {
		super(pos);
		color = new Color(0xff00ffff);
	}

	@Override
	public void update() {
		pos.y -= 3;

		if (pos.y < -20)
			level.removeEntity(this);
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(color);
		sr.triangle(pos.x - 7.5f, pos.y - +6.5f, pos.x + 7.5f, pos.y - +6.5f, pos.x, pos.y + 6.5f);
	}

}
