package com.critters.breakout.entities;

import static com.critters.breakout.level.Level.level;

import com.critters.breakout.math.Vector2f;

/**
 * Block multi is a special type of block that takes multiple hits to be destroyed
 */
public class BlockMulti extends Block {

	protected int numberOfHits;
	protected final int HITS_NEEDED;

	public BlockMulti(Vector2f pos, Vector2f size) {
		this(pos, size, 1);
	}

	public BlockMulti(Vector2f pos, Vector2f size, int hitsNeeded) {
		super(pos, size);
		HITS_NEEDED = hitsNeeded;
	}

	@Override
	public void update() {
		if (numberOfHits == HITS_NEEDED)
			level.removeEntity(this);
	}

	@Override
	public void hit() {
		numberOfHits++;
	}

}
