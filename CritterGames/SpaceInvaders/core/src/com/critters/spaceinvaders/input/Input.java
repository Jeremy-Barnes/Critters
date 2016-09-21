package com.critters.spaceinvaders.input;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class Input implements InputProcessor {

	private boolean fullscreen = false;

	public static ArrayList<Click> inputs = new ArrayList<Click>();

	public static boolean ready() {
		return inputs.size() > 0;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		inputs.add(new Click(screenX, Gdx.graphics.getHeight() - screenY, button));
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