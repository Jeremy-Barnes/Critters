package com.critters.spaceinvaders;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.graphics.SpriteLoader;
import com.critters.spaceinvaders.input.Input;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.level.Level.State;

public class Game extends ApplicationAdapter {
	SpriteBatch render;

	private Level level;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(new Input());

		render = new SpriteBatch();
		SpriteLoader.loadSprites();

		level = new Level(null, 0);
	}

	public void update() {
		level.update();

		if (level.state == State.WON) {
			level = new Level(level.getShields(), level.score);
		}

		if (level.state == State.LOST && Gdx.input.justTouched()) {
			level = new Level(null, 0);
		}

	}

	@Override
	public void render() {
		// Dirty temporary reset feature
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
			level = new Level(null, 0);
		}

		update();

		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		render.begin();

		level.render(render);

		render.end();
	}

	@Override
	public void dispose() {
		render.dispose();
	}
}
