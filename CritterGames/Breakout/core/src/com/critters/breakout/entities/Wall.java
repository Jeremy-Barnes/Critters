package com.critters.breakout.entities;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.graphics.Render;
import com.critters.breakout.math.Vector2f;

public class Wall extends Collidable {

	public Wall(Vector2f pos, Vector2f size) {
		super(pos, size);
	}

	@Override
	public void hit() {

	}

	@Override
	public void update() {

	}

	@Override
	public void render(Render render) {
		render.drawRectangle(new Color(0x2C3E50ff), pos.x, pos.y, size.x, size.y);
	}

}
