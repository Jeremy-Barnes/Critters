package com.critters.spaceinvaders.entities.mobs;

import static com.critters.spaceinvaders.graphics.Render.sr;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Alien extends Enemy {

	public Alien(Level level, Vector2f pos, Vector2f size) {
		super(level, pos, size);
	}

	@Override
	public void update() {
		rectangle.update(pos, pos.add(size));
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.25f, 0.75f, 0.25f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
