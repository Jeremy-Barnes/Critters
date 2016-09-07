package com.critters.spaceinvaders.entities.ui;

import static com.critters.spaceinvaders.level.Level.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreDisplay extends UIElement {

	int score = 0;
	private BitmapFont font = new BitmapFont();

	public ScoreDisplay() {
		font.setColor(Color.BLACK);
	}

	@Override
	public void update() {
		score = level.score;
	}

	@Override
	public void render(SpriteBatch render) {
		font.draw(render, "Score: " + score, 30, 25);
	}

}
