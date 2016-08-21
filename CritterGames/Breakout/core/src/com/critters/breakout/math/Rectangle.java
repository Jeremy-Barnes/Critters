package com.critters.breakout.math;

public class Rectangle {

	/**
	 * v1 ----- | | ------v2
	 */

	public Vector2f v1;
	public Vector2f v2;

	public Line top;
	public Line bottom;
	public Line left;
	public Line right;

	public Rectangle(Vector2f v1, Vector2f v2) {
		this.v1 = v1;
		this.v2 = v2;

		// Create the lines
		float width = v2.y - v1.y;
		float height = v2.y - v1.y;

		top = new Line(v1, v1.add(width, 0));
		bottom = new Line(v2.add(-width, 0), v2);
		left = new Line(v1, v1.add(0, height));
		right = new Line(v2.add(0, -height), v2);
	}

	private boolean intersectsPoint(Vector2f v) {
		if (v.x > v1.x && v.x < v2.x && v.y > v1.x && v.y < v2.y)
			return true;
		return false;
	}

	public boolean intersectsCircle(Circle c) {
		/*
		 * If any of the sided of the rectangle are intersected by the circle or
		 * the center of the circle is inside the rectangle
		 */
		return intersectsPoint(c.center) || top.intersectsCircle(c) || bottom.intersectsCircle(c) || left.intersectsCircle(c) || right.intersectsCircle(c);
	}
}
