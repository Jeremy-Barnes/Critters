package com.critters.spaceinvaders.input;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.critters.spaceinvaders.graphics.Render;

public class Input implements InputProcessor {

	private boolean fullscreen = false;

	public static ArrayList<Click> inputs = new ArrayList<Click>();

	public static boolean ready() {
		return inputs.size() > 0;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		float xRatio = Gdx.graphics.getWidth() / 640f;
		float yRatio = Gdx.graphics.getHeight() / 480f;

		int x = (int) ((screenX) / xRatio);
		int y = (int) ((Gdx.graphics.getHeight() - screenY) / yRatio);

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
				Gdx.graphics.setWindowedMode(640, 480);
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
		return false;
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