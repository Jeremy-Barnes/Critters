package com.critters.snake.level;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.critters.snake.Game;
import com.critters.snake.entities.ui.GameOverDisplay;
import com.critters.snake.entities.ui.ScoreDisplay;
import com.critters.snake.entities.ui.UIElement;
import com.critters.snake.graphics.Render;
import com.critters.snake.graphics.SpriteLoader;
import com.critters.snake.input.Input;
import com.critters.snake.math.Vector2f;

public class Level {

	public enum State {
		NOT_STARTED, PLAY, WON, LOST;
	}

	private Game game;
	public State state;

	public final float TILE_WIDTH;
	public final float TILE_HEIGHT;
	public final int TILES_X;
	public final int TILES_Y;

	public final float LEVEL_WIDTH;
	public final float LEVEL_HEIGHT;

	private ArrayList<UIElement> ui = new ArrayList<UIElement>();

	public int score;

	public Level(Game game) {
		this.game = game;

		ui.add(new ScoreDisplay(this, 0));
		ui.add(new GameOverDisplay(this));

		TILES_X = (int) (16 * 1.0f);
		TILES_Y = (int) (9 * 1.0f);

		LEVEL_WIDTH = Render.WIDTH;
		LEVEL_HEIGHT = Render.HEIGHT;

		TILE_WIDTH = LEVEL_WIDTH / TILES_X;
		TILE_HEIGHT = LEVEL_HEIGHT / TILES_Y;

		for (int x = 0; x < TILES_X; x++) {
			for (int y = 0; y < TILES_Y; y++) {

			}
		}
	}

	/**
	 * Pass the input to different parts of the game and see if they accept it
	 */
	private void processInput() {
		if (Input.ready()) {
			Vector2f input = Input.inputs.get(0).getPos();
			Input.inputs.remove(0);

			// TODO INPUT
		}
	}

	private void updateUI() {
		for (int i = 0; i < ui.size(); i++) {
			ui.get(i).update();
		}
	}

	/**
	 * Update all the entities and process input
	 */
	public void update() {
		processInput();

		updateUI();
	}

	private void renderUI(Render render) {
		for (int i = 0; i < ui.size(); i++) {
			ui.get(i).render(render);
		}
	}

	private void renderTiles(Render render) {

	}

	/**
	 * Render the level.
	 */
	public void render(Render render) {
		renderTiles(render);
		renderUI(render);
	}
}
