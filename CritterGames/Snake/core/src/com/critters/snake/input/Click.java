package com.critters.snake.input;

import com.critters.snake.math.Vector2f;

public class Click {
	public int x;
	public int y;
	public int button;

	public Click(int x, int y, int button) {
		this.x = x;
		this.y = y;
		this.button = button;
	}

	public Vector2f getPos() {
		return new Vector2f(x, y);
	}
}
