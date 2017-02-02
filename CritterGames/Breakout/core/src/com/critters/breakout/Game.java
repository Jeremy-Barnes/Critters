package com.critters.breakout;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.critters.breakout.graphics.Render;
import com.critters.breakout.graphics.SpriteLoader;
import com.critters.breakout.input.Input;
import com.critters.breakout.level.Level;
import com.critters.breakout.level.Level.State;

public class Game extends ApplicationAdapter {

	private Render render;

	private Level level;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(new Input());

		render = new Render();
		SpriteLoader.loadSprites();

		level = new Level(0);
	}

	public void update() {
		level.update();

		if (level.state == State.WON) {
			level = new Level(level.score);
		}

		if (level.state == State.LOST && Gdx.input.justTouched()) {
			level = new Level(0);
		}

	}

	@Override
	public void render() {
		// Dirty temporary reset feature
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.O)) {
			level = new Level(level.score);
		}

		render.begin();

		update();

		level.render(render);

		render.end();
	}

	@Override
	public void dispose() {
		render.dispose();
	}
}
