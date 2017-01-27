package com.critters.flappingbird.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.input.Input;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.math.Vector2f;

public class Bird extends Entity {

	public static final float G = -25;

	private Vector2f vel;
	private Vector2f size;

	private boolean dead;

	public Bird(Level level, Vector2f pos) {
		super(level, pos);
		vel = new Vector2f(3, 0);
		size = new Vector2f(50, 50);
	}

	private void checkCollisions() {
		ArrayList<Collidable> collidables = level.getCollidables();
		for (Collidable c : collidables) {
			if (c.intersectsPoint(pos.add(size))) {
				dead = true;
			}
			if (c.intersectsPoint(pos)) {
				dead = true;
			}
			if (c.intersectsPoint(pos.add(size.mul(0, 1)))) {
				dead = true;
			}
			if (c.intersectsPoint(pos.add(size.mul(1, 0)))) {
				dead = true;
			}
		}
	}

	private void move() {
		vel = vel.add(new Vector2f(0, G).mul(Gdx.graphics.getDeltaTime()));
		pos = pos.add(vel);

		if (Input.ready()) {
			Input.inputs.remove(0);
			vel = new Vector2f(vel.x, 10);
		}
	}

	@Override
	public void update() {
		move();
		checkCollisions();
	}

	@Override
	public void render(Render render) {
		render.drawRectangle(Color.ORANGE, pos.x, pos.y, size.x, size.y);
	}

	public boolean isDead() {
		return dead;
	}

}