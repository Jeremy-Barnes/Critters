package com.critters.flappingbird.entities;

import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Rectangle;
import com.critters.flappingbird.math.Vector2f;

public abstract class Collidable extends Entity {

	protected Vector2f size;
	protected boolean hit = false;

	protected Rectangle rectangle;
	public boolean checked;

	public Collidable(Level level, Vector2f pos, Vector2f size) {
		super(level, pos);
		this.size = size;
		rectangle = new Rectangle(pos, pos.add(size));
	}

	public boolean intersectsPoint(Vector2f point) {
		return rectangle.intersectsPoint(point);
	}

	public void hit() {
		hit = true;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

}