package com.critters.snake.entities.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.critters.snake.graphics.Render;
import com.critters.snake.level.Level;

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
	public void renderIfVisible(Render render) {
		render.drawText(font, "Score: " + score, 100, 40);
	}

}
