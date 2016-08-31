package com.critters.breakout.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.critters.breakout.entities.BlockMulti;
import com.critters.breakout.entities.BlockVoid;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.math.Vector2f;

public enum Pattern {
	WALL, MESH, COLUMNS, CENTER;

	private static final List<Pattern> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static Pattern getRandom() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}

	public static void generateLevel(ArrayList<Entity> entities, Pattern p) {

		final int OFFSET_X = 20;
		final int OFFSET_Y = 260;

		final int BLOCKS_X = 9;
		int BLOCKS_Y = 5;
		final int BLOCK_WIDTH = (Gdx.graphics.getWidth() - OFFSET_X * 2) / BLOCKS_X;
		final int BLOCK_HEIGHT = 25;

		switch (p) {
		case WALL:
			for (int x = 0; x < BLOCKS_X; x++) {
				for (int y = 0; y < BLOCKS_Y; y++) {
					entities.add(new BlockMulti(new Vector2f(OFFSET_X + x * BLOCK_WIDTH, OFFSET_Y + y * BLOCK_HEIGHT), new Vector2f(BLOCK_WIDTH - 2, BLOCK_HEIGHT - 2)));
				}
			}
			break;

		case MESH:
			for (int x = 0; x < BLOCKS_X; x++) {
				for (int y = 0; y < BLOCKS_Y; y++) {
					if ((x + y) % 2 == 0)
						entities.add(new BlockMulti(new Vector2f(OFFSET_X + x * BLOCK_WIDTH, OFFSET_Y + y * BLOCK_HEIGHT), new Vector2f(BLOCK_WIDTH - 2, BLOCK_HEIGHT - 2)));
				}
			}
			break;

		case COLUMNS:
			for (int x = 0; x < BLOCKS_X; x++) {
				for (int y = 0; y < BLOCKS_Y; y++) {
					if (x % 2 == 0)
						entities.add(new BlockMulti(new Vector2f(OFFSET_X + x * BLOCK_WIDTH, OFFSET_Y + y * BLOCK_HEIGHT), new Vector2f(BLOCK_WIDTH - 2, BLOCK_HEIGHT - 2)));
				}
			}
			break;

		case CENTER:
			for (int x = 0; x < BLOCKS_X; x++) {
				for (int y = 0; y < BLOCKS_Y; y++) {
					boolean spawn = (x > 1) && (x < 7);
					if (y == 0 || y == BLOCKS_Y - 1)
						spawn = ((x > 2) && (x < 6));

					if (spawn)
						entities.add(new BlockMulti(new Vector2f(OFFSET_X + x * BLOCK_WIDTH, OFFSET_Y + y * BLOCK_HEIGHT), new Vector2f(BLOCK_WIDTH - 2, BLOCK_HEIGHT - 2)));
				}
			}
			break;
		default:
			break;
		}

	}

}
