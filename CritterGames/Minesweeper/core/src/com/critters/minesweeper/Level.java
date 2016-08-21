package com.critters.minesweeper;

import static com.critters.minesweeper.graphics.SpriteLoader.getFlag;
import static com.critters.minesweeper.graphics.SpriteLoader.getBomb;
import static com.critters.minesweeper.graphics.SpriteLoader.getCross;
import static com.critters.minesweeper.graphics.SpriteLoader.getNumber;
import static com.critters.minesweeper.graphics.SpriteLoader.getTile;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.critters.minesweeper.input.Click;
import com.critters.minesweeper.input.Input;

public class Level {

	private final int SIZE_X = 8;
	private final int SIZE_Y = 8;
	private final int BOMBS = 10;

	private int[][] values;
	private boolean[][] open;
	private boolean[][] flagged;
	private boolean[][] crossed;

	private boolean started;
	private boolean ended;

	public Level() {
		values = new int[SIZE_X][SIZE_Y];
		open = new boolean[SIZE_X][SIZE_Y];
		flagged = new boolean[SIZE_X][SIZE_Y];
		crossed = new boolean[SIZE_X][SIZE_Y];
	}

	/**
	 * @param ix
	 *            - ignore x, first click there
	 * @param iy
	 *            - ignore y, first click there
	 */
	public void generateLevel(int ix, int iy) {
		Random random = new Random();
		// Place random bombs. A bomb is -1 in the values array
		int toPlace = BOMBS;
		do {
			int rX = random.nextInt(SIZE_X);
			int rY = random.nextInt(SIZE_Y);
			if (values[rX][rY] != -1 && (rX != ix && rY != iy)) {
				values[rX][rY] = -1;
				toPlace--;
			}
		} while (toPlace > 0);

		// Calculate the other numbers
		for (int x = 0; x < SIZE_X; x++) {
			for (int y = 0; y < SIZE_Y; y++) {
				// Continue if it's a bomb
				if (values[x][y] == -1)
					continue;

				// Count the bombs
				int n = 0;
				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = -1; dy <= 1; dy++) {
						int nx = x + dx;
						int ny = y + dy;
						if (nx < 0 || ny < 0 || nx >= SIZE_X || ny >= SIZE_Y)
							continue;

						if (values[nx][ny] == -1)
							n++;
					}
				}
				values[x][y] = n;
			}
		}
	}

	/**
	 * Open the clicked tile or adjacent if need be.
	 */
	public void openTile(int x, int y) {
		if (x < 0 || y < 0 || x >= SIZE_X || y >= SIZE_Y)
			return;

		// Check if it's the first tile
		if (!started) {
			started = true;
			generateLevel(x, y);
		}

		// Open the tile
		open[x][y] = true;

		// Check if empty area was opened and open all adjacent
		if (values[x][y] == 0) {
			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					int nx = x + dx;
					int ny = y + dy;

					if (nx < 0 || ny < 0 || nx >= SIZE_X || ny >= SIZE_Y)
						continue;

					if (values[nx][ny] == 0 && !open[nx][ny]) {
						openTile(nx, ny);
					}
					open[nx][ny] = true;
				}
			}
		}

		// Check if a bomb was clicked. End the game and show all the bombs
		if (values[x][y] == -1) {
			ended = true;
			for (int xx = 0; xx < SIZE_X; xx++) {
				for (int yy = 0; yy < SIZE_Y; yy++) {
					// Show all the remaining bombs
					if (values[xx][yy] == -1 && !flagged[xx][yy]) {
						open[xx][yy] = true;
					}

					if (values[xx][yy] != -1 && flagged[xx][yy]) {
						crossed[xx][yy] = true;
						open[xx][yy] = true;
					}
				}
			}
		}

		// Check if after opening, all the non-bomb tiles have been opened.
		boolean won = true;
		for (int xx = 0; xx < SIZE_X; xx++) {
			for (int yy = 0; yy < SIZE_Y; yy++) {
				if (!open[xx][yy] && values[xx][yy] != -1) {
					won = false;
				}
				if (open[xx][yy] && values[xx][yy] == -1) {
					won = false;
				}
			}
		}
		// Flag all bombs if won
		if (won) {
			ended = true;
			for (int xx = 0; xx < SIZE_X; xx++) {
				for (int yy = 0; yy < SIZE_Y; yy++) {
					if (values[xx][yy] == -1) {
						flagged[xx][yy] = true;
					}
				}
			}
		}
	}

	/**
	 * Receive and process the input
	 */
	public void update() {
		if (Input.ready() && !ended) {
			Click input = Input.inputs.get(0);
			Input.inputs.remove(0);

			int tileX = (int) input.x / 32;
			int tileY = (int) input.y / 32;

			if (tileX < 0 || tileY < 0 || tileX >= SIZE_X || tileY >= SIZE_Y)
				return;

			// Left click
			if (input.button == 0) {

				if (!open[tileX][tileY] && !flagged[tileX][tileY]) {
					// Open a closed tile
					openTile(tileX, tileY);

				} else {
					// Click on an open tile
					int value = values[tileX][tileY];
					// Count the flags around the tile. And open around it if
					// the number matches the value.
					int bombs = 0;
					for (int dx = -1; dx <= 1; dx++) {
						for (int dy = -1; dy <= 1; dy++) {
							int nx = tileX + dx;
							int ny = tileY + dy;
							if (nx < 0 || ny < 0 || nx >= SIZE_X || ny >= SIZE_Y)
								continue;
							if (flagged[nx][ny])
								bombs++;
						}
					}

					if (bombs == value)
						for (int dx = -1; dx <= 1; dx++) {
							for (int dy = -1; dy <= 1; dy++) {
								int nx = tileX + dx;
								int ny = tileY + dy;
								if (nx < 0 || ny < 0 || nx >= SIZE_X || ny >= SIZE_Y)
									continue;
								if (!flagged[nx][ny])
									openTile(nx, ny);
							}
						}
				}

			} else {
				// RightClick - toggle the flag
				if (!open[tileX][tileY]) {
					flagged[tileX][tileY] = !flagged[tileX][tileY];
				}
			}
		}
	}

	/**
	 * Render the level.
	 */
	public void render(SpriteBatch render) {
		for (int x = 0; x < SIZE_X; x++) {
			for (int y = 0; y < SIZE_Y; y++) {

				TextureRegion toDraw = null;
				if (open[x][y]) {
					int value = values[x][y];
					if (value == 0)
						toDraw = getNumber(-1);
					else if (value == -1)
						toDraw = getBomb();
					else
						toDraw = getNumber(value);
				} else
					toDraw = getTile(true);

				render.draw(getTile(false), x * 32, y * 32, 32, 32);
				render.draw(toDraw, x * 32, y * 32, 32, 32);

				if (flagged[x][y])
					render.draw(getFlag(), x * 32, y * 32, 32, 32);
				if (crossed[x][y])
					render.draw(getCross(), x * 32, y * 32, 32, 32);
			}
		}
	}

}
