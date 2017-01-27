package com.critters.flappingbird.entities;

import java.util.ArrayList;

import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

public class Terrain extends Collidable {

	private ArrayList<Collidable> objects = new ArrayList<Collidable>();
	private Ground ground;
	private Ceiling ceiling;

	public Terrain(Level level) {
		super(level, new Vector2f(), new Vector2f());

		ground = new Ground(level);
		ground.setOffset(0);
		ceiling = new Ceiling(level);
		ceiling.setOffset(0);

		objects.add(ground);
		objects.add(ceiling);
	}

	@Override
	public boolean intersectsPoint(Vector2f point) {
		for (Collidable c : objects) {
			if (c.intersectsPoint(point))
				return true;
		}
		return false;
	}

	@Override
	public void update() {
		ground.setOffset(pos.x);
		ceiling.setOffset(pos.x);

		for (Collidable c : objects) {
			c.update();
		}

	}

	@Override
	public void render(Render render) {
		for (Collidable c : objects) {
			c.render(render);
		}
	}

}
