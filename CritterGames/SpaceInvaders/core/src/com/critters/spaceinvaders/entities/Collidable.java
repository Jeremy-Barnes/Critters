package com.critters.spaceinvaders.entities;

import com.critters.spaceinvaders.math.Rectangle;
import com.critters.spaceinvaders.math.Vector2f;

public abstract class Collidable extends Entity {

	protected Vector2f size;
	protected boolean hit = false;

	protected Rectangle rectangle;
	public boolean checked;

	public Collidable(Vector2f pos, Vector2f size) {
		super(pos);
		this.size = size;
		rectangle = new Rectangle(pos, pos.add(size));
	}

	public void hit() {
		hit = true;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void destroy() {
		hit();
	}

}
