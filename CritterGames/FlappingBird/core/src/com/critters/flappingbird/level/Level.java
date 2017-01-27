package com.critters.flappingbird.level;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.critters.flappingbird.Game;
import com.critters.flappingbird.entities.Bird;
import com.critters.flappingbird.entities.Collidable;
import com.critters.flappingbird.entities.Entity;
import com.critters.flappingbird.entities.Terrain;
import com.critters.flappingbird.entities.Wall;
import com.critters.flappingbird.entities.powerup.Powerup;
import com.critters.flappingbird.entities.ui.GameOverDisplay;
import com.critters.flappingbird.entities.ui.ScoreDisplay;
import com.critters.flappingbird.entities.ui.UIElement;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.input.Input;
import com.critters.flappingbird.math.Vector2f;

public class Level {

	public enum State {
		NOT_STARTED, PLAY, WON, LOST;
	}

	public static final Random random = new Random();

	// Dirty work around
	public static Level level;

	public int score;

	public State state;

	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<UIElement> uiElements = new ArrayList<UIElement>();
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	private Bird bird;
	private Terrain terrain;

	public final int LEVEL_WIDTH;
	public final int LEVEL_HEIGHT;

	public Level(Game game) {
		level = this;
		state = State.NOT_STARTED;

		LEVEL_WIDTH = Gdx.graphics.getWidth();
		LEVEL_HEIGHT = Gdx.graphics.getHeight();

		LevelLoader.loadLevel(this);

		// Add entities
		bird = new Bird(this, new Vector2f(0, 300));
		entities.add(bird);
		terrain = new Terrain(level);
		entities.add(terrain);

		uiElements.add(new ScoreDisplay(this, score));
		GameOverDisplay gameOver = new GameOverDisplay(this, score);
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
		if (bird.isDead()) {
			// The game has been lost
			state = State.LOST;
		}

		// TODO
		if (false) {
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

				// START GAME
			}
			return;
		}
		updateEntities();
		terrain.pos.x = bird.pos.x;

		// Check winning or losing
		checkState();
	}

	/**
	 * Render the level.
	 */
	public void render(Render render) {
		render.translate(bird.pos.x - 300, 0);

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

	public ArrayList<Entity> getEntities() {
		return entities;
	}

}