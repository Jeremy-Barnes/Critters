package com.critters.breakout.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteLoader {

	// Array of sprites generated from the sprites.png file
	// sprites[ROW][COLUMN]
	private static TextureRegion[][] sprites;

	public static void loadSprites() {
		Texture test = new Texture("sprites.png");
		sprites = TextureRegion.split(test, 32, 32);
	}

	public static TextureRegion getNumber(int number) {
		if (number >= 0 && number < 9) {
			return sprites[number / 4][number % 4];
		}

		// If the number isn't in range, return the last sprite, should be an
		// empty one.
		return sprites[3][3];
	}

	public static TextureRegion getTile(boolean closed) {
		return sprites[2][closed ? 1 : 2];
	}

	public static TextureRegion getBomb() {
		return sprites[2][3];
	}

	public static TextureRegion getFlag() {
		return sprites[3][0];
	}

	public static TextureRegion getCross() {
		return sprites[3][1];
	}
}
