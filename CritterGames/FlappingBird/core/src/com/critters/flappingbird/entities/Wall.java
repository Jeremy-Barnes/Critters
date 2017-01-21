package com.critters.flappingbird.entities;

import com.badlogic.gdx.graphics.Color;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

public class Wall extends Collidable {

	public Wall(Level level, Vector2f pos, Vector2f size) {
		super(level, pos, size);
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
