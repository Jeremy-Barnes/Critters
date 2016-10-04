package com.critters.spaceinvaders.level;

import static com.critters.spaceinvaders.graphics.Render.sr;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.spaceinvaders.entities.Collidable;
import com.critters.spaceinvaders.entities.Entity;
import com.critters.spaceinvaders.entities.mobs.Alien;
import com.critters.spaceinvaders.entities.mobs.Enemy;
import com.critters.spaceinvaders.entities.mobs.Player;
import com.critters.spaceinvaders.entities.obstacles.Shield;
import com.critters.spaceinvaders.entities.powerup.Powerup;
import com.critters.spaceinvaders.entities.powerup.PowerupLife;
import com.critters.spaceinvaders.entities.ui.UIElement;
import com.critters.spaceinvaders.input.Input;
import com.critters.spaceinvaders.math.Vector2f;

public class Level {

	public enum State {
		NOT_STARTED, PLAY, WON, LOST;
	}

	public static final Random random = new Random();

	public int score;

	public State state;

	private ArrayList<UIElement> uiElements = new ArrayList<UIElement>();
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	private Player player;

	public final int LEVEL_WIDTH;
	public final int LEVEL_HEIGHT;

	public Level() {
		state = State.NOT_STARTED;

		LEVEL_WIDTH = Gdx.graphics.getWidth();
		LEVEL_HEIGHT = Gdx.graphics.getHeight();

		player = new Player(this, new Vector2f(50, 25), new Vector2f(50, 25));
		addEntity(player);

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 4; y++) {
				addEntity(new Alien(this, new Vector2f(x * 50 + 50, y * 50 + 250), new Vector2f(40, 40), y, x));
			}

		}

		for (int x = 0; x < 4; x++) {
			addEntity(new Shield(this, new Vector2f(x * 150 + 50, 95), new Vector2f(75, 20)));

		}

		// uiElements.add(new ScoreDisplay());

		// Remove all inputs before the start of the game since a new one will
		// start it.
		Input.inputs.clear();
	}

	/**
	 * Check the state of the level, it can either have been won or lost.
	 */
	private void checkState() {
		if (!entities.contains(player)) {
			// The game has been lost
			state = State.LOST;
		}

		if (!Enemy.areAnyLeft(this)) {
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

				// TODO START GAME
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
		 * I have absolutely no idea why this is needed, but it doesn't render
		 * the score without it, when putting the same code used to render the
		 * score inside this render method does render it.
		 */
		render.flush();

		sr.end();
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

	/**
	 * @return collidables
	 */
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

	/**
	 * @return powerups that are active
	 */
	public ArrayList<Powerup> getActivePowerups() {
		return powerups;
	}

	/**
	 * @return get the first powerup of a certain type
	 */
	public Powerup getPowerup(Class<? extends Powerup> pClass) {
		for (Powerup p : powerups) {
			if (p.getClass() == pClass)
				return p;
		}
		return null;
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<Alien> getAliensInColumn(int column) {
		ArrayList<Alien> aliens = new ArrayList<Alien>();
		for (Entity e : entities)
			if (e instanceof Alien)
				if (((Alien) e).column == column)
					aliens.add((Alien) e);
		return aliens;
	}

}
