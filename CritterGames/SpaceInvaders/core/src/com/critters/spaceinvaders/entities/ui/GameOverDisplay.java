package com.critters.spaceinvaders.entities.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.spaceinvaders.level.Level;
import com.critters.spaceinvaders.level.Level.State;

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
	public void renderIfVisible(SpriteBatch render) {
		font.draw(render, "Game Over ", 550, 50);
	}

}
