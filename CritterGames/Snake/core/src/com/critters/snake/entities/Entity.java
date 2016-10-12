package com.critters.snake.entities;

import com.critters.snake.graphics.Render;
import com.critters.snake.level.Level;
import com.critters.snake.math.Vector2f;

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

	public abstract void render(Render render);

}
