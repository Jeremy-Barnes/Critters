package com.critters.breakout.entities;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.math.Vector2f;

/**
 * Block Indestructible is a special type of block that cannot be destroyed.
 */
public class BlockIndestructible extends Block {

	public BlockIndestructible(Vector2f pos, Vector2f size) {
		super(pos, size);

		color = new Color(0.3f, 0.3f, 0.3f, 1);
	}

	@Override
	public void update() {
	}

	@Override
	public void hit() {
	}

}
