package com.critters.minesweeper.math;

public class Vector2f {
	public float x, y;

	public Vector2f() {
		this(0, 0);
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public Vector2f add(float d) {
		return new Vector2f(x + d, y + d);
	}

	public Vector2f add(float dx, float dy) {
		return new Vector2f(x + dx, y + dy);
	}

	public Vector2f add(Vector2f dv) {
		return new Vector2f(x + dv.x, y + dv.y);
	}

}
