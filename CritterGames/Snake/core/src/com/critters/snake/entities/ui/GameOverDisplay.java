package com.critters.snake.entities.ui;

import com.critters.snake.graphics.Render;
import com.critters.snake.level.Level;
import com.critters.snake.level.Level.State;

public class GameOverDisplay extends ScoreDisplay {

	public GameOverDisplay(Level level) {
		super(level, 0);
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
		render.drawText(font, "Game Over", 90, 55);
	}

}
