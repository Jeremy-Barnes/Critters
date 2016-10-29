package com.critters.snake.graphics;

import com.badlogic.gdx.graphics.Texture;

public class SpriteLoader {

	// Array of sprites generated from the sprites.png file
	// sprites[ROW][COLUMN]
	//private static TextureRegion[][] sprites;
	private static Texture test;

	public static void loadSprites() {
		test = new Texture("badlogic.jpg");
		// sprites = TextureRegion.split(test, 32, 32);
	}

	public static Texture getTest() {
		return test;
	}
}
