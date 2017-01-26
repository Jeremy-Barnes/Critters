package com.critters.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.critters.snake.graphics.Render;
import com.critters.snake.graphics.SpriteLoader;
import com.critters.snake.input.Input;
import com.critters.snake.level.Level;

public class Game extends ApplicationAdapter {

	private Level level;

	private Render render;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(new Input());

		render = new Render();
		SpriteLoader.loadSprites();

		level = new Level(this, 1);
<<<<<<< HEAD
=======

>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
	}

	public void update() {
		level.update();

	}

	@Override
	public void render() {
		// Dirty temporary reset feature
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
			level = new Level(this, 1);
		}
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_1)) {
			level = new Level(this, 1);
		}
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_2)) {
			level = new Level(this, 2);
		}
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_3)) {
			level = new Level(this, 4);
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
