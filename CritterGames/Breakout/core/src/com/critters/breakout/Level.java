package com.critters.breakout;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.entities.Ball;
import com.critters.breakout.entities.Block;
import com.critters.breakout.entities.BlockMulti;
import com.critters.breakout.entities.Collidable;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.entities.Pad;
import com.critters.breakout.entities.Wall;
import com.critters.breakout.input.Input;
import com.critters.breakout.math.Vector2f;
import static com.critters.breakout.graphics.Render.sr;

public class Level {

	// Dirty work around
	public static Level level;

	private boolean started = false;

	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Ball ball;

	public final int LEVEL_WIDTH;
	public final int LEVEL_HEIGHT;

	public final int WALL_SIZE = 17;
	private final int OFFSET_X = 20;
	private final int OFFSET_Y = 260;
	private final int BLOCK_WIDTH;
	private final int BLOCK_HEIGHT;
	private final int BLOCKS_X = 10;
	private final int BLOCKS_Y = 5;

	public Level() {
		level = this;

		LEVEL_WIDTH = Gdx.graphics.getWidth();
		LEVEL_HEIGHT = Gdx.graphics.getHeight();

		BLOCK_WIDTH = (Gdx.graphics.getWidth() - OFFSET_X * 2) / BLOCKS_X;
		BLOCK_HEIGHT = 25;

		ball = new Ball(new Vector2f(316, 100), 8);
		entities.add(ball);
		entities.add(new Pad(new Vector2f(320 - 75 / 2, 30), new Vector2f(75, 10)));

		for (int x = 0; x < BLOCKS_X; x++) {
			for (int y = 0; y < BLOCKS_Y; y++) {
				entities.add(new BlockMulti(new Vector2f(OFFSET_X + x * BLOCK_WIDTH, OFFSET_Y + y * BLOCK_HEIGHT), new Vector2f(BLOCK_WIDTH - 2, BLOCK_HEIGHT - 2), y + 1));
			}
		}

		entities.add(new Wall(new Vector2f(0, 0), new Vector2f(WALL_SIZE, 480)));
		// entities.add(new Wall(new Vector2f(0, 0), new Vector2f(640, WALL_SIZE))); /* Bottom debug wall*/
		entities.add(new Wall(new Vector2f(0, 480 - WALL_SIZE), new Vector2f(640, WALL_SIZE)));
		entities.add(new Wall(new Vector2f(640 - WALL_SIZE, 0), new Vector2f(WALL_SIZE, 480)));

		// Remove all inputs before the start of the game since a new one will start it.
		Input.inputs.clear();
	}

	private void updateEntities() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}

	/**
	 * Update all the entities and process input
	 */
	public void update() {
		if (!started) {
			if (Input.ready()) {
				Input.inputs.remove(0);
				started = true;
				ball.launch();
			}
			return;
		}
		updateEntities();
	}

	/**
	 * Render the level.
	 */
	public void render(SpriteBatch render) {
		sr.begin(ShapeType.Filled);

		for (Entity e : entities) {
			e.render(render);
		}

		sr.end();
	}

	public ArrayList<Collidable> getCollidables() {
		ArrayList<Collidable> blocks = new ArrayList<Collidable>();
		for (Entity e : entities) {
			if (e instanceof Collidable)
				blocks.add((Collidable) e);
		}
		return blocks;
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}
}
