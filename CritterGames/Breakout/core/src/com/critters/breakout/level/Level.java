package com.critters.breakout.level;

import java.util.ArrayList;
import java.util.Random;

import com.critters.breakout.entities.Ball;
import com.critters.breakout.entities.Collidable;
import com.critters.breakout.entities.Entity;
import com.critters.breakout.entities.Pad;
import com.critters.breakout.entities.Wall;
import com.critters.breakout.entities.blocks.Block;
import com.critters.breakout.entities.blocks.BlockMulti;
import com.critters.breakout.entities.powerup.Powerup;
import com.critters.breakout.entities.ui.GameOverDisplay;
import com.critters.breakout.entities.ui.ScoreDisplay;
import com.critters.breakout.entities.ui.UIElement;
import com.critters.breakout.graphics.Render;
import com.critters.breakout.input.Input;
import com.critters.breakout.math.Vector2f;

public class Level {

	public enum State {
		NOT_STARTED, PLAY, WON, LOST;
	}

	public static final Random random = new Random();

	// Dirty work around
	public static Level level;

	public int score;

	public State state;

	private ArrayList<UIElement> uiElements = new ArrayList<UIElement>();
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	private Ball ball;

	public final int LEVEL_WIDTH;
	public final int LEVEL_HEIGHT;

	public final int WALL_SIZE = 35;

	// private final Pattern pattern;

	public Level(int score) {
		level = this;
		state = State.NOT_STARTED;

		// Start with the score from the previous level
		this.score = score;

		LEVEL_WIDTH = Render.WIDTH;
		LEVEL_HEIGHT = Render.HEIGHT;

		// Add the ball and the paddle
		ball = new Ball(new Vector2f(LEVEL_WIDTH / 2 - 8, 100), 15);
		entities.add(ball);
		entities.add(new Pad(new Vector2f(LEVEL_WIDTH / 2 - 200 / 2, 30), new Vector2f(200, 15)));

		// Create the level
		LevelLoader.loadLevel(this);
		// pattern = Pattern.getRandom();
		// Pattern.generateLevel(entities, pattern);

		// Add the level walls
		entities.add(new Wall(new Vector2f(0, 0), new Vector2f(WALL_SIZE, LEVEL_HEIGHT)));
		entities.add(new Wall(new Vector2f(0, LEVEL_HEIGHT - WALL_SIZE), new Vector2f(LEVEL_WIDTH, WALL_SIZE)));
		entities.add(new Wall(new Vector2f(LEVEL_WIDTH - WALL_SIZE, 0), new Vector2f(WALL_SIZE, LEVEL_HEIGHT)));

		uiElements.add(new ScoreDisplay(score));
		GameOverDisplay gameOver = new GameOverDisplay(score);
		gameOver.setVisible(false);
		uiElements.add(gameOver);

		// Remove all inputs before the start of the game since a new one will
		// start it.
		Input.inputs.clear();
	}

	/**
	 * Check the state of the level, it can either have been won or lost.
	 */
	private void checkState() {
		if (ball.pos.y < -50) {
			// The game has been lost
			state = State.LOST;
		}

		if (getDestructableBlocksCount() == 0) {
			// The game has been won
			state = State.WON;
		}
	}

	/**
	 * Update all the entities
	 */
	private void updateEntities() {
		for (int i = 0; i < uiElements.size(); i++) {
			uiElements.get(i).update();
		}

		// Do not update entities and powerups if its lost
		if (state == State.LOST)
			return;

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}

		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).update();
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
	public void render(Render render) {
		for (Entity e : entities) {
			e.render(render);
		}

		for (UIElement e : uiElements) {
			e.render(render);
		}
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	public void addPowerup(Powerup powerup) {
		powerups.add(powerup);
	}

	public void removePowerup(Powerup powerup) {
		powerups.remove(powerup);
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

	/**
	 * @return powerups in Entity form
	 */
	public ArrayList<Powerup> getPowerups() {
		ArrayList<Powerup> p = new ArrayList<Powerup>();
		for (Entity e : entities) {
			if (e instanceof Powerup)
				p.add((Powerup) e);
		}
		return p;
	}

	public ArrayList<Powerup> getActivePowerups() {
		return powerups;
	}

	private int getDestructableBlocksCount() {
		int count = 0;
		ArrayList<Block> blocks = getBlocks();
		for (Block b : blocks) {
			if (b instanceof BlockMulti) {
				count += ((BlockMulti) b).hitsLeft();
			}
		}
		return count;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

}
