package com.critters.spaceinvaders.math;

public class Circle {
	// TODO
	public Vector2f center;
	public float radius;

	public Circle(Vector2f center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	public void updateCenter(Vector2f center) {
		update(center, radius);
	}

	public void updateRadius(float radius) {
		update(center, radius);
	}

	public void update(Vector2f center, float radius) {
		this.center = center;
		this.radius = radius;
	}

}
