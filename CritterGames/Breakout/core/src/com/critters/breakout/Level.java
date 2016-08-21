package com.critters.breakout;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.entities.Ball;
import com.critters.breakout.entities.Block;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.math.Vector2f;

public class Level {

	// Dirty work around
	public static Level level;

	private ArrayList<Entity> entities = new ArrayList<Entity>();

	public Level() {
		level = this;

		entities.add(new Ball(new Vector2f(100, 100), 5));

		for (int x = 0; x < 12; x++) {
			for (int y = 0; y < 5; y++) {
				entities.add(new Block(new Vector2f(30 + x * 50, 300 + y * 20), new Vector2f(49, 19)));
			}
		}
	}

	/**
	 * Update all the entities and process input
	 */
	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}

	/**
	 * Render the level.
	 */
	public void render(SpriteBatch render) {
		for (Entity e : entities) {
			e.render(render);
		}
	}

	public ArrayList<Block> getBlocks() {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (Entity e : entities) {
			if (e instanceof Block)
				blocks.add((Block) e);
		}
		return blocks;
	}
}
