package com.critters.flappingbird.graphics;

public class Polygon {

	public final float[] VERTICES;
	public final int VERTICES_COUNT;

	public Polygon(float[] verts) {
		VERTICES = verts;
		VERTICES_COUNT = verts.length / 2;
	}

	public Polygon(int verts) {
		this(verts, 1);
	}

	public Polygon(int verts, float size) {
		// /generate the polygon coorinates
		float[] v = new float[verts * 2];
		for (int i = 0; i < verts; i += 1) {
			float angle = ((2 * 3.1416f) / verts) * i;
			v[2 * i] = (float) Math.cos(angle) * size;
			v[2 * i + 1] = (float) Math.sin(angle) * size;
		}

		VERTICES = v;
		VERTICES_COUNT = verts;
	}
}
