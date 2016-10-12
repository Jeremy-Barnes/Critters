package com.critters.spaceinvaders.entities.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.level.Level;

public class ScoreDisplay extends UIElement {

	int score = 0;
	protected BitmapFont font = new BitmapFont();

	public ScoreDisplay(Level level, int score) {
		super(level);
		this.score = score;
		font.setColor(Color.BLACK);
	}

	@Override
	public void update() {
		score = level.score;
	}

	@Override
	public void renderIfVisible(SpriteBatch render) {
		font.draw(render, "Score: " + score, 550, 25);
	}

}
