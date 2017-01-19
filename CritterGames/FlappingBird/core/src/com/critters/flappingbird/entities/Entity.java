package com.critters.flappingbird.entities;

import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

public abstract class Entity {

	public Vector2f pos;
	protected Level level;

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
