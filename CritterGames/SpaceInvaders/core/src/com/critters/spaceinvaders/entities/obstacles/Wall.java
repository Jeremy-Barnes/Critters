package com.critters.spaceinvaders.entities.obstacles;

import static com.critters.spaceinvaders.graphics.Render.sr;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public class Wall extends Collidable {

	public Wall(Level level, Vector2f pos, Vector2f size) {
		super(level, pos, size);
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
