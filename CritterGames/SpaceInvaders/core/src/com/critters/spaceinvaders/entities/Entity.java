package com.critters.spaceinvaders.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.math.Vector2f;

public abstract class Entity {

	protected Level level;

	public Vector2f pos;

	public Entity(Level level) {
		this(level, new Vector2f(0, 0));
	}

	public Entity(Level level, Vector2f pos) {
		this.level = level;
		this.pos = pos;
	}

	public abstract void update();

	public abstract void render(SpriteBatch render);

}
