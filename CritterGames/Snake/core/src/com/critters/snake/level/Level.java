package com.critters.snake.level;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.critters.snake.Game;
import com.critters.snake.entities.ui.GameOverDisplay;
import com.critters.snake.entities.ui.ScoreDisplay;
import com.critters.snake.entities.ui.UIElement;
import com.critters.snake.graphics.Render;

public class Level {

	public enum State {
		NOT_STARTED, PLAY, WON, LOST;
	}

	private Game game;
	public State state;

	public Random random;

	public final float TILE_WIDTH;
	public final float TILE_HEIGHT;
	public final int TILES_X;
	public final int TILES_Y;

	public final int WALL = -1;
	public final int FOOD = -2;
	public final int FREE = 0;

	public int tiles[][];

	public final float LEVEL_WIDTH;
	public final float LEVEL_HEIGHT;

	public int time;
	public int STEP_TIME = 7;

	private ArrayList<UIElement> ui = new ArrayList<UIElement>();

	public int score;

	public Level(Game game) {
		this.game = game;

		random = new Random();

		ui.add(new ScoreDisplay(this, 0));
		ui.add(new GameOverDisplay(this));

		TILES_X = (int) (16 * 3.0f);
		TILES_Y = (int) (9 * 3.0f);

		tiles = new int[TILES_X][TILES_Y];

		LEVEL_WIDTH = Render.WIDTH;
		LEVEL_HEIGHT = Render.HEIGHT;

		TILE_WIDTH = LEVEL_WIDTH / TILES_X;
		TILE_HEIGHT = LEVEL_HEIGHT / TILES_Y;

		for (int x = 0; x < TILES_X; x++) {
			for (int y = 0; y < TILES_Y; y++) {
				if (x == 0 || y == 0 || x == TILES_X - 1 || y == TILES_Y - 1)
					tiles[x][y] = WALL;
			}
		}

		tiles[5][TILES_Y / 2] = 1;
		tiles[4][TILES_Y / 2] = 2;

		placeFood();
	}

	/**
	 * Pass the input to different parts of the game and see if they accept it
	 */
	private void processInput() {
		// TODO INPUT
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			move = Move.RIGHT;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			move = Move.LEFT;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			move = Move.UP;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			move = Move.DOWN;
		}
	}

	private void updateUI() {
		for (int i = 0; i < ui.size(); i++) {
			ui.get(i).update();
		}
	}

	enum Move {
		UP, DOWN, LEFT, RIGHT;

		public static boolean isOposite(Move m1, Move m2) {
			if ((m1 == Move.UP && m2 == Move.DOWN) || (m1 == Move.DOWN && m2 == Move.UP))
				return true;
			if ((m1 == Move.LEFT && m2 == Move.RIGHT) || (m1 == Move.RIGHT && m2 == Move.LEFT))
				return true;
			return false;
		}
	}

	Move move = Move.RIGHT;
	Move last = Move.RIGHT;
	int length = 2;

	private void updatePlayer() {
		boolean ate = false;
		boolean moved = false;

		// Check if the player is trying to go into themselves
		if (Move.isOposite(move, last)) {
			move = last;
		} else {
			last = move;
		}

		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				int id = tiles[x][y];
				if (id > 0) {
					// Move the player
					// Move the head in the input direction
					if (id == 1 && !moved) {
						moved = true;

						int dx = 0, dy = 0;
						switch (move) {
						case UP:
							dy = 1;
							break;
						case DOWN:
							dy = -1;
							break;
						case LEFT:
							dx = -1;
							break;
						case RIGHT:
							dx = 1;
							break;
						}

						boolean valid = canMove(x + dx, y + dy);
						if (valid) {
							ate = isFood(x + dx, y + dy);
							if (ate) {
								length++;
								placeFood();
							}
							tiles[x + dx][y + dy] = 1;
							tiles[x][y]++;
						} else {
							state = State.LOST;
						}
					} else if (id > 1) {
						// Increase the index of the tile
						tiles[x][y]++;
					}
				}
			}
		}

		// If it didn't eat, cut the tail off
		if (!ate) {
			for (int x = 0; x < tiles.length; x++) {
				for (int y = 0; y < tiles[x].length; y++) {
					if (tiles[x][y] == length + 1) {
						tiles[x][y] = 0;
					}
				}
			}
		}
	}

	private boolean isFood(int x, int y) {
		if (x >= 0 && y >= 0 && y < TILES_Y && x < TILES_X)
			return tiles[x][y] == FOOD;
		else
			return false;
	}

	private boolean canMove(int x, int y) {
		if (x >= 0 && y >= 0 && y < TILES_Y && x < TILES_X)
			return tiles[x][y] != WALL && tiles[x][y] < 1;
		else
			return false;
	}

	public void placeFood() {
		boolean placed = false;
		do {
			int x = random.nextInt(TILES_X);
			int y = random.nextInt(TILES_Y);
			if (canMove(x, y)) {
				placed = true;
				tiles[x][y] = FOOD;
			}
		} while (!placed);
	}

	/**
	 * Update all the entities and process input
	 */
	public void update() {
		processInput();

		time++;
		if (state != State.LOST && time % STEP_TIME == 0) {
			updatePlayer();
		}

		score = length - 2;

		updateUI();
	}

	private void renderUI(Render render) {
		for (int i = 0; i < ui.size(); i++) {
			ui.get(i).render(render);
		}
	}

	private void renderTiles(Render render) {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				int id = tiles[x][y];
				switch (id) {
				case WALL:
					render.drawRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Color.BLACK);
					break;
				case FREE:

					break;
				case FOOD:
					render.drawRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Color.GREEN);
					break;

				default:
					// Must be a player then
					render.drawRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, Color.YELLOW);
					break;
				}
			}
		}
	}

	/**
	 * Render the level.
	 */
	public void render(Render render) {
		renderTiles(render);
		renderUI(render);
	}
}
