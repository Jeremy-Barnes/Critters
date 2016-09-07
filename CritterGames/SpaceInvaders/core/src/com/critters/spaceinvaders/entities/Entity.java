package com.critters.spaceinvaders.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.math.Vector2f;

public abstract class Entity {

	public Vector2f pos;

	public Entity() {
		this(new Vector2f(0, 0));
	}

	public Entity(Vector2f pos) {
		this.pos = pos;
	}

	public abstract void update();

	public abstract void render(SpriteBatch render);

}
