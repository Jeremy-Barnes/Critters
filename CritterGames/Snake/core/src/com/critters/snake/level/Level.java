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

	public final int FREE = 0;
	public final int WALL = -1;
	public final int FOOD = -2;
	public final int FOOD_DOUBLE = -3;
	public final int FOOD_TRIPLE = -4;
	public final int FOOD_QUADRUPLE = -5;

	public final int SPECIAL = -6;

	public final int OBSTACLE_MIN = -1000;
	public final int OBSTACLE_MAX = -600;

	public int tiles[][];

	public final float LEVEL_WIDTH;
	public final float LEVEL_HEIGHT;

	// In seconds
	public float time;
	public float STEP_TIME_DEFAULT = 0.25f;
	public float STEP_TIME;
	private float special_time = 0;

	private ArrayList<UIElement> ui = new ArrayList<UIElement>();

	public int score;
	private int difficulty = 1;

	public Level(Game game, int difficulty) {
		this.game = game;
		this.difficulty = difficulty;
		STEP_TIME_DEFAULT /= difficulty;
		STEP_TIME = STEP_TIME_DEFAULT;

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

		for (int i = 0; i < difficulty; i++) {
			tiles[10 + i * 5][10 + i * 5] = OBSTACLE_MIN + (OBSTACLE_MAX - OBSTACLE_MIN) * i / difficulty;
		}
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
							getPoints(x + dx, y + dy);
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
			return tiles[x][y] == FOOD || tiles[x][y] == FOOD_DOUBLE || tiles[x][y] == FOOD_TRIPLE
					|| tiles[x][y] == FOOD_QUADRUPLE || tiles[x][y] == SPECIAL;
		else
			return false;
	}

	private void getPoints(int x, int y) {
		int value = 0;
		if (x >= 0 && y >= 0 && y < TILES_Y && x < TILES_X)
			value = tiles[x][y];
		else
			return;

		// sp_max is the number of seconds the special effect is taking place.
		final float SP_TIME = 5;
		switch (value) {
		case FOOD:
			value = 1;
			break;
		case FOOD_DOUBLE:
			value = 2;
			break;
		case FOOD_TRIPLE:
			value = 3;
			break;
		case FOOD_QUADRUPLE:
			value = 4;
			break;
		case SPECIAL:
			special_time += SP_TIME;
			value = 5;
			break;
		}

		// Calculate the score for the eaten food for and all modifiers
		score += value * difficulty * (special_time > 0 && special_time != SP_TIME ? 3 : 1);
	}

	private boolean canMove(int x, int y) {
		if (x >= 0 && y >= 0 && y < TILES_Y && x < TILES_X)
			return tiles[x][y] != WALL && tiles[x][y] < 1 && tiles[x][y] > OBSTACLE_MAX;
		else
			return false;
	}

	private boolean isFree(int x, int y) {
		if (x >= 0 && y >= 0 && y < TILES_Y && x < TILES_X)
			return tiles[x][y] == FREE;
		else
			return false;
	}

	public void placeFood() {
		boolean placed = false;
		do {
			int x = random.nextInt(TILES_X);
			int y = random.nextInt(TILES_Y);
			if (isFree(x, y)) {
				placed = true;

				{
					// Find the food item to place
					int number = random.nextInt(100);
					if (number < 75) {
						tiles[x][y] = FOOD;
					} else if (number < 90) {
						tiles[x][y] = FOOD_DOUBLE;
					} else if (number < 98) {
						tiles[x][y] = FOOD_TRIPLE;
					} else if (number < 99) {
						tiles[x][y] = FOOD_QUADRUPLE;
					} else
						tiles[x][y] = SPECIAL;

				}
			}
		} while (!placed);
	}

	/**
	 * Update all the entities and process input
	 */
	public void update() {
		processInput();

		time += Gdx.graphics.getDeltaTime();
		if (state != State.LOST && time >= STEP_TIME) {
			time = 0;
			updatePlayer();
		}
		updateObstacle();
		updateUI();

		// update special
		special_time-= Gdx.graphics.getDeltaTime();
		if (special_time > 0)
			STEP_TIME = STEP_TIME_DEFAULT / 2;
		else
			STEP_TIME = STEP_TIME_DEFAULT;
	}

	private void updateObstacle() {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y] < OBSTACLE_MAX && tiles[x][y] >= OBSTACLE_MIN) {
					tiles[x][y]++;
				} else if (tiles[x][y] == OBSTACLE_MAX) {
					tiles[x][y] = FREE;

					// Place a new one
					boolean placed = false;
					do {
						int xx = random.nextInt(TILES_X);
						int yy = random.nextInt(TILES_Y);
						if (isFree(xx, yy)) {
							placed = true;
							tiles[xx][yy] = OBSTACLE_MIN;
						}
					} while (!placed);
				}
			}
		}
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
				boolean shouldRender = true;
				Color c = Color.WHITE;

				switch (id) {
				case WALL:
					c = Color.BLACK;
					break;
				case FREE:
					// Dont't render empty tiles
					shouldRender = false;
					break;

				case FOOD:
					c = Color.GREEN;
					break;
				case FOOD_DOUBLE:
					c = Color.FOREST;
					break;
				case FOOD_TRIPLE:
					c = Color.RED;
					break;
				case FOOD_QUADRUPLE:
					c = Color.CORAL;
					break;
				case SPECIAL:
					c = Color.PINK;
					break;

				default:
					// Must be a player then, unless it's an obstacle
					if (id >= OBSTACLE_MIN && id <= OBSTACLE_MAX)
						c = Color.BLACK;
					else
						c = Color.YELLOW;
					break;
				}

				if (shouldRender)
					render.drawRect(x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, c);
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
