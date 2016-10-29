package com.critters.breakout.entities.ui;

import static com.critters.breakout.level.Level.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreDisplay extends UIElement {

	int score = 0;
	protected BitmapFont font;

	public ScoreDisplay(int score) {
		this.score = score;

		font = new BitmapFont();

		font.setColor(Color.BLACK);
	}

	@Override
	public void update() {
		score = level.score;
	}

	@Override
	public void renderIfVisible(SpriteBatch render) {
		font.draw(render, "Score: " + score, 30, 30);
	}

}
