package com.critters.breakout.entities.blocks;

import static com.critters.breakout.level.Level.level;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.math.Vector2f;

/**
 * Block multi is a special type of block that takes multiple hits to be destroyed
 */
public class BlockMulti extends Block {

	protected Color[] colors = { new Color(0xE74C3Cff), new Color(0xE67E22ff), new Color(0xF1C40Fff), new Color(0x2ECC71ff), new Color(0x3498DBff), new Color(0xAF7AC4ff),
			new Color(0x8E44ADff) };

	protected int numberOfHits;
	protected final int HITS_NEEDED;

	public BlockMulti(Vector2f pos, Vector2f size) {
		this(pos, size, 1);
	}

	public BlockMulti(Vector2f pos, Vector2f size, int hitsNeeded) {
		super(pos, size);
		HITS_NEEDED = hitsNeeded;

		// Pick a color
		color = colors[hitsNeeded - 1];
	}

	@Override
	public void update() {
		if (numberOfHits == HITS_NEEDED)
			level.removeEntity(this);
	}

	@Override
	public void hit() {
		numberOfHits++;

		// Switch the color
		if ((HITS_NEEDED - numberOfHits) > 0)
			color = colors[(HITS_NEEDED - numberOfHits) - 1];
	}

}
