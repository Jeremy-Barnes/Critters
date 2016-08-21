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

		entities.add(new Ball(new Vector2f(100, 100), 20));
		entities.add(new Block(new Vector2f(200, 200), new Vector2f(100, 50)));
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
