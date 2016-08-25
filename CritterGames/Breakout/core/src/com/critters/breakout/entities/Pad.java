package com.critters.breakout.entities;

import static com.critters.breakout.graphics.Render.sr;
import static com.critters.breakout.Level.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.critters.breakout.Level;
import com.critters.breakout.math.Vector2f;

public class Pad extends Collidable {

	enum InputMode {
		POINTER, KEYBOARD;
	}

	// Movement of the pad
	private float goalX;
	private final float MAX_VEL = 5;

	private InputMode mode;

	public Pad(Vector2f pos, Vector2f size) {
		super(pos, size);
		rectangle.onlyBottom = true;

		mode = InputMode.KEYBOARD;
		goalX = pos.x;
	}

	private void processInput() {

		// Switching between modes
		if (Gdx.input.isTouched() || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			mode = InputMode.POINTER;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			mode = InputMode.KEYBOARD;
		}

		switch (mode) {
		case POINTER:
			goalX = Gdx.input.getX() - size.x / 2;
			break;

		case KEYBOARD:
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
				goalX += MAX_VEL;
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
				goalX -= MAX_VEL;
			break;
		}
	}

	@Override
	public void update() {
		processInput();

		// Speed limitation
		float deltaX = Math.abs(pos.x - goalX);
		if (deltaX < MAX_VEL) {
			pos.x = goalX;
		} else {
			pos.x += (pos.x - goalX) > 0 ? -MAX_VEL : MAX_VEL;
		}

		// Position limitation
		if (pos.x < level.WALL_SIZE)
			pos.x = level.WALL_SIZE;
		if (pos.x + size.x > level.LEVEL_WIDTH - level.WALL_SIZE)
			pos.x = level.LEVEL_WIDTH - level.WALL_SIZE - size.x;

		// Goal limitation
		if (goalX < level.WALL_SIZE)
			goalX = level.WALL_SIZE;
		if (goalX + size.x > level.LEVEL_WIDTH - level.WALL_SIZE)
			goalX = level.LEVEL_WIDTH - level.WALL_SIZE - size.x;

		// Collision update
		rectangle.update(pos, pos.add(size));
	}

	@Override
	public void render(SpriteBatch render) {
		sr.setColor(0.75f, 0.5f, 0.25f, 1);
		sr.rect(pos.x, pos.y, size.x, size.y);
	}

}
