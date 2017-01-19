package com.critters.flappingbird.entities;

import com.badlogic.gdx.graphics.Color;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

public class Ground extends Wall {

	// Variables for rendering
	private float offset;

	public Ground(Level level) {
		super(level, new Vector2f(), new Vector2f());
		offset = 0;
	}

	@Override
	public boolean intersectsPoint(Vector2f point) {
		return point.y <= getHeight(point.x);
	}

	public void setOffset(float offset) {
		this.offset = offset - 300;
	}

	public float getHeight(float x) {
		return (float) Math.sin(x / 100) * 100 + 150;
	}

	public void render(Render r) {
		float step = 10f;
		float x = this.offset - 50;
		while (x <= offset + Render.WIDTH) {
			x += step;

			float h = (float) Math.abs(pos.y - getHeight(x));
			r.drawRectangle(Color.RED, x, Math.min(pos.y, getHeight(x)), step, h);
		}
	}
}
