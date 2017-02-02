package com.critters.breakout.level;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.entities.blocks.BlockIndestructible;
import com.critters.breakout.entities.blocks.BlockMulti;
import com.critters.breakout.entities.blocks.BlockVoid;
import com.critters.breakout.math.Vector2f;

public class LevelLoader {

	/**
	 * This method spawns the blocks
	 */
	public static void loadLevel(Level level) {
		
		// TODO Load different levels
		FileHandle handle = Gdx.files.internal("data/test_level.txt");

		String contents = handle.readString();
		String rows[] = contents.split("\n");
		ArrayList<Entity> entities = level.getEntities();

		// Level variables
		final int OFFSET_X = 20;
		final int OFFSET_Y = level.LEVEL_HEIGHT / 2;

		int BLOCKS_Y = rows.length;
		int BLOCK_HEIGHT = (int) (level.LEVEL_HEIGHT / 3.5f / BLOCKS_Y);
		int BLOCK_WIDTH = 0;

		for (int i = 0; i < BLOCKS_Y; i++) {
			int y = BLOCKS_Y - i - 1; // Reverse the y since it's generated from
										// bottom up

			int BLOCKS_X = rows[i].length() / 3;// Each block uses 3 chars
			BLOCK_WIDTH = (level.LEVEL_WIDTH - OFFSET_X * 2) / BLOCKS_X;

			// Spawn the blocks for each row
			for (int x = 0; x < BLOCKS_X; x++) {
				spawnBlock(entities, new Vector2f(OFFSET_X + x * BLOCK_WIDTH, OFFSET_Y + y * BLOCK_HEIGHT),
						new Vector2f(BLOCK_WIDTH - 3, BLOCK_HEIGHT - 3), rows[i].charAt(x * 3 + 1));
			}
		}

	}

	private static void spawnBlock(ArrayList<Entity> entities, Vector2f pos, Vector2f size, char type) {

		switch (type) {
		case '-': // No block
			break;

		case 'V': // No block
			entities.add(new BlockVoid(pos, size));
			break;
		case 'O': // No block
			entities.add(new BlockIndestructible(pos, size));
			break;
		default: // Should be a number otherwise
			entities.add(new BlockMulti(pos, size, Integer.parseInt(type + "")));
			break;
		}

	}

}
