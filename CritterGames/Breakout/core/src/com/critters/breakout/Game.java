package com.critters.breakout;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.graphics.SpriteLoader;
import com.critters.breakout.input.Input;
import com.critters.breakout.level.Level;

public class Game extends ApplicationAdapter {
	SpriteBatch render;

	private Level level;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(new Input());

		render = new SpriteBatch();
		SpriteLoader.loadSprites();

		level = new Level();
	}

	@Override
	public void render() {
		// Dirty temporary reset feature
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
			level = new Level();
		}

		level.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
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
