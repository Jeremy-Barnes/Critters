package com.critters.flappingbird.entities.ui;

import com.badlogic.gdx.graphics.Color;
import com.critters.flappingbird.graphics.Render;
import com.critters.flappingbird.level.Level;
import com.critters.flappingbird.level.Level.State;

public class GameOverDisplay extends ScoreDisplay {

	public GameOverDisplay(Level level, int score) {
		super(level, score);
	}

	@Override
	public void update() {
		if (level.state == State.LOST) {
			setVisible(true);
		} else {
			setVisible(false);
		}
	}

	@Override
	public void renderIfVisible(Render render) {
		render.drawText("GameOver", Color.BLACK, 1270 + level.getTranslation(), 100);
	}

}
