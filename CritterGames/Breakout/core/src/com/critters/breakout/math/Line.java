package com.critters.breakout.math;

public class Line {

	public Vector2f v1;
	public Vector2f v2;

	private Vector2f direction;
	private float length;

	public Line(Vector2f v1, Vector2f v2) {
		this.v1 = v1;
		this.v2 = v2;

		direction = v2.sub(v1);
		length = direction.length();

		direction = direction.normal();
	}

	public boolean intersectsCircle(Circle c) {

		// Calculate the normal of the vector from point 1 to circle center. And
		// the distance
		Vector2f v = c.center.sub(v1).normal();
		float distance = c.center.sub(v1).length();

		// Calculate the angles
		float theta = (float) Math.acos((v.x * direction.x + v.y * direction.y) / (v.length() * direction.length()));

		// Closest point
		Vector2f cp = v1.add(direction.mul((float) Math.cos(theta) * distance));

		// Distance from circle center to the line ends
		Vector2f d1 = c.center.sub(v1);
		Vector2f d2 = c.center.sub(v2);

		// Calculate the distance from circle center to the closest point on the
		// line
		float d = distance * (float) Math.sin(theta);
		return d < c.radius && (isOnTheLine(cp) || d1.length() < c.radius || d2.length() < c.radius);
	}

	private boolean isOnTheLine(Vector2f cp) {
		if (v1.sub(cp).length() < length && v2.sub(cp).length() < length) {
			return true;
		}
		return false;
	}

}
