package com.critters.minesweeper.input;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.critters.minesweeper.math.Vector2f;

public class Input implements InputProcessor {

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
		return false;
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