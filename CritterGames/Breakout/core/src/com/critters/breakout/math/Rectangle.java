package com.critters.breakout.math;

public class Rectangle {

	public static final int NO_INTERSECTION = 0x00;
	public static final int HORRIZONTAL = 0x01;
	public static final int VERTICAL = 0x02;

	/**
	 * v1 ----- | | ------v2
	 */

	public Vector2f v1;
	public Vector2f v2;

	private float width;
	private float height;

	public Line top;
	public Line bottom;
	public Line left;
	public Line right;

	public Rectangle(Vector2f v1, Vector2f v2) {
		this.v1 = v1;
		this.v2 = v2;

		// Create the lines
		width = v2.x - v1.x;
		height = v2.y - v1.y;

		top = new Line(v1, v1.add(width, 0));
		bottom = new Line(v2.add(-width, 0), v2);
		left = new Line(v1, v1.add(0, height));
		right = new Line(v2.add(0, -height), v2);
	}

	public void update(Vector2f v1, Vector2f v2) {
		this.v1 = v1;
		this.v2 = v2;

		top = new Line(v1, v1.add(width, 0));
		bottom = new Line(v2.add(-width, 0), v2);
		left = new Line(v1, v1.add(0, height));
		right = new Line(v2.add(0, -height), v2);
	}

	private boolean intersectsPoint(Vector2f v) {
		if (v.x > v1.x && v.x < v2.x && v.y > v1.y && v.y < v2.y)
			return true;
		return false;
	}

	public int intersectsCircle(Circle c) {
		/*
		 * Return the line the circle is intersecting
		 */
		if (top.intersectsCircle(c))
			return HORRIZONTAL;
		if (bottom.intersectsCircle(c))
			return HORRIZONTAL;
		if (left.intersectsCircle(c))
			return VERTICAL;
		if (right.intersectsCircle(c))
			return VERTICAL;

		return NO_INTERSECTION;
	}
}
