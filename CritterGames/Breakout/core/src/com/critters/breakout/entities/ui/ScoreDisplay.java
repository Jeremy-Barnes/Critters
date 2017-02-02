package com.critters.breakout.entities.ui;

import static com.critters.breakout.level.Level.level;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.graphics.Render;

public class ScoreDisplay extends UIElement {

	int score = 0;

	public ScoreDisplay(int score) {
		this.score = score;
	}

	@Override
	public void update() {
		score = level.score;
	}

	@Override
	public void renderIfVisible(Render render) {
		render.drawText("Score: " + score, Color.BLACK, 40, 55);
	}

}
