package com.critters.breakout;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.graphics.SpriteLoader;
import com.critters.breakout.input.Input;

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
/*
		if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.F)) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		} else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.W)) {
			Gdx.graphics.setWindowedMode(800, 600);
		}*/

		/*InputAdapter webGlfullscreen = new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Keys.ENTER && Gdx.app.getType() == ApplicationType.WebGL) {
					if (!Gdx.graphics.isFullscreen()) {
						DisplayMode dm = (Gdx.graphics.getDisplayModes()[0]);
						Gdx.graphics.setFullscreenMode(dm);
					}
				}
				return true;
			}
		};

		Gdx.input.setInputProcessor(new InputMultiplexer(webGlfullscreen));
*/
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
