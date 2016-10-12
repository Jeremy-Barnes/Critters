package com.critters.snake.math;

public class Vector2f {
	public float x, y;

	public Vector2f() {
		this(0, 0);
	}

	public Vector2f(Vector2f v) {
		this(v.x, v.y);
	}

	public Vector2f(Vector2i v) {
		this(v.x, v.y);
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public Vector2f normal() {
		float l = length();
		return new Vector2f(x / l, y / l);
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

	public Vector2f sub(Vector2f dv) {
		return new Vector2f(x - dv.x, y - dv.y);
	}

	public Vector2f sub(float dx, float dy) {
		return new Vector2f(x - dx, y - dy);
	}

	public Vector2f mul(float d) {
		return new Vector2f(x * d, y * d);
	}

	public Vector2f mul(float dx, float dy) {
		return new Vector2f(x * dx, y * dy);
	}

	public float getAngle() {
		return (float) Math.atan(y / x);
	}

}
