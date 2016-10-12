package com.critters.snake.math;

public class Vector2i {
	public int x, y;

	public Vector2i() {
		this(0, 0);
	}

	public Vector2i(Vector2i v) {
		this(v.x, v.y);
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f add(int d) {
		return new Vector2f(x + d, y + d);
	}

	public Vector2f add(int dx, int dy) {
		return new Vector2f(x + dx, y + dy);
	}

	public Vector2f add(Vector2f dv) {
		return new Vector2f(x + dv.x, y + dv.y);
	}

	public Vector2f sub(Vector2f dv) {
		return new Vector2f(x - dv.x, y - dv.y);
	}

	public Vector2f sub(int dx, int dy) {
		return new Vector2f(x - dx, y - dy);
	}

	public Vector2f mul(int d) {
		return new Vector2f(x * d, y * d);
	}

	public Vector2f mul(int dx, int dy) {
		return new Vector2f(x * dx, y * dy);
	}

	public Vector2f toVector2f() {
		return new Vector2f(x, y);
	}
}
