package com.critters.flappingbird.entities.ui;

import com.badlogic.gdx.graphics.Color;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;

public class ScoreDisplay extends UIElement {

	int score = 0;

	public ScoreDisplay(Level level,int score) {
		super(level);
		
		this.score = score;
	}

	@Override
	public void update() {
		score = level.score;
	}

	@Override
	public void renderIfVisible(Render render) {
		render.drawText("Score: " + score, Color.BLACK, 30, 30);
	}

}
