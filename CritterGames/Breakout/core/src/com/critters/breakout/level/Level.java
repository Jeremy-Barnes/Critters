package com.critters.breakout.level;

import static com.critters.breakout.graphics.Render.sr;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.entities.Ball;
import com.critters.breakout.entities.Collidable;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.entities.Pad;
import com.critters.breakout.entities.Wall;
import com.critters.breakout.entities.blocks.Block;
import com.critters.breakout.entities.powerup.Powerup;
import com.critters.breakout.entities.ui.ScoreDisplay;
import com.critters.breakout.entities.ui.UIElement;
import com.critters.breakout.input.Input;
import com.critters.breakout.math.Vector2f;

public class Level {

	public enum State {
		NOT_STARTED, PLAY, WON, LOST;
	}

	// Dirty work around
	public static Level level;

	public int score;

	public State state;

	private ArrayList<UIElement> uiElements = new ArrayList<UIElement>();
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Ball ball;

	public final int LEVEL_WIDTH;
	public final int LEVEL_HEIGHT;

	public final int WALL_SIZE = 17;

	private final Pattern pattern;

	public Level() {
		level = this;
		state = State.NOT_STARTED;

		LEVEL_WIDTH = Gdx.graphics.getWidth();
		LEVEL_HEIGHT = Gdx.graphics.getHeight();

		// Add the ball and the paddle
		ball = new Ball(new Vector2f(316, 100), 8);
		entities.add(ball);
		entities.add(new Pad(new Vector2f(320 - 75 / 2, 30), new Vector2f(75, 10)));

		// Create the level
		pattern = Pattern.getRandom();
		Pattern.generateLevel(entities, pattern);

		// Add the level walls
		entities.add(new Wall(new Vector2f(0, 0), new Vector2f(WALL_SIZE, 480)));
		// entities.add(new Wall(new Vector2f(0, 0), new Vector2f(640, WALL_SIZE))); /* Bottom debug wall*/
		entities.add(new Wall(new Vector2f(0, 480 - WALL_SIZE), new Vector2f(640, WALL_SIZE)));
		entities.add(new Wall(new Vector2f(640 - WALL_SIZE, 0), new Vector2f(WALL_SIZE, 480)));

		uiElements.add(new ScoreDisplay());

		entities.add(new Powerup(new Vector2f(100, 100)));

		// Remove all inputs before the start of the game since a new one will start it.
		Input.inputs.clear();
	}

	/**
	 * Check the state of the level, it can either have been won or lost.
	 */
	private void checkState() {
		if (ball.pos.y < 0) {
			// The game has been lost
			state = State.LOST;
		}

		if (getBlocks().size() == 0) {
			// The game has been won
			state = State.WON;
		}
	}

	/**
	 * Update all the entities
	 */
	private void updateEntities() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}

		for (int i = 0; i < uiElements.size(); i++) {
			uiElements.get(i).update();
		}

	}

	/**
	 * Update all the entities and process input
	 */
	public void update() {
		if (state == State.NOT_STARTED) {
			if (Input.ready()) {
				Input.inputs.remove(0);
				state = State.PLAY;
				ball.launch();
			}
			return;
		}
		updateEntities();

		// Check winning or losing
		checkState();
	}

	/**
	 * Render the level.
	 */
	public void render(SpriteBatch render) {
		sr.begin(ShapeType.Filled);

		for (Entity e : entities) {
			e.render(render);
		}

		for (UIElement e : uiElements) {
			e.render(render);
		}

		/*
		 * I have absolutely no idea why this is needed, but it doesn't render the score without it, when putting the same code used to render the score inside this render method
		 * does render it.
		 */
		render.flush();

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

	public ArrayList<Block> getBlocks() {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (Entity e : entities) {
			if (e instanceof Block)
				blocks.add((Block) e);
		}
		return blocks;
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}
}
