package com.critters.breakout.entities.ui;

import static com.critters.breakout.level.Level.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreDisplay extends UIElement {

	int score = 0;
	private BitmapFont font = new BitmapFont();

	public ScoreDisplay(int score) {
		font.setColor(Color.BLACK);
		this.score = score;
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
