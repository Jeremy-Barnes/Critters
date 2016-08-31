package com.critters.breakout.entities;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.math.Vector2f;

/**
 * Block void is a special type of block that lets the ball pass through. Disappears on "collision" (Takes 1 hit as a BlockMulti)
 */
public class BlockVoid extends BlockMulti {

	public BlockVoid(Vector2f pos, Vector2f size) {
		super(pos, size, 1);
		color = new Color(0.8f, 0.8f, 0.8f, 1);
	}

}
