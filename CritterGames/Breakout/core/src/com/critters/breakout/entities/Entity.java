package com.critters.breakout.entities;

import com.critters.breakout.graphics.Render;
import com.critters.breakout.math.Vector2f;

public abstract class Entity {

	public Vector2f pos;

	public Entity() {
		this(new Vector2f(0, 0));
	}

	public Entity(Vector2f pos) {
		this.pos = pos;
	}

	public abstract void update();

	public abstract void render(Render render);

}
