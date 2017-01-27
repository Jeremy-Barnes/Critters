package com.critters.flappingbird.entities;

import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

public class Ceiling extends Ground {

	public Ceiling(Level level) {
		super(level);
		pos.y = Render.HEIGHT;
	}

	@Override
	public boolean intersectsPoint(Vector2f point) {
		return !super.intersectsPoint(point);
	}

	@Override
	public float getHeight(float x) {
		return super.getHeight(x) + Render.HEIGHT / 2 + 100;
	}

}
