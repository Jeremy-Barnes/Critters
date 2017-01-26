package com.critters.snake.input;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.critters.snake.graphics.Render;

public class Input implements InputProcessor {

	private boolean fullscreen = false;

	public static ArrayList<Click> inputs = new ArrayList<Click>();
<<<<<<< HEAD
=======
	public static float mouseX;
	public static float mouseY;

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a

	public static boolean ready() {
		return inputs.size() > 0;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// Transform into proper location on the screen and fit it to the render camera size
		int x = (int) (((float) screenX / Gdx.graphics.getWidth()) * Render.WIDTH);
		int y = (int) (((float) (Gdx.graphics.getHeight() - screenY) / Gdx.graphics.getHeight()) * Render.HEIGHT);

<<<<<<< HEAD
=======
		mouseX = x;
		mouseY = y;

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
		inputs.add(new Click(x, y, button));
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == com.badlogic.gdx.Input.Keys.F) {
			if (!fullscreen) {
				fullscreen = true;
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			} else {
				fullscreen = false;
				Gdx.graphics.setWindowedMode(1000, (int) (1000 / (16 / 9f)));
			}
		}

		if (keycode == com.badlogic.gdx.Input.Keys.ESCAPE) {
			System.exit(0);
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
<<<<<<< HEAD
		return false;
=======
		int x = (int) (((float) screenX / Gdx.graphics.getWidth()) * Render.WIDTH);
		int y = (int) (((float) (Gdx.graphics.getHeight() - screenY) / Gdx.graphics.getHeight()) * Render.HEIGHT);

		mouseX = x;
		mouseY = y;
		return true;

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}