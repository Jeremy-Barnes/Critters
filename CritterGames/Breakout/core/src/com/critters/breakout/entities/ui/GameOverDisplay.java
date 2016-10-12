package com.critters.breakout.entities.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.level.Level;
import com.critters.breakout.level.Level.State;

public class GameOverDisplay extends ScoreDisplay {

	public GameOverDisplay(int score) {
		super(score);
	}

	@Override
	public void update() {

		if (Level.level.state == State.LOST) {
			setVisible(true);
		} else {
			setVisible(false);
		}
	}

	@Override
	public void renderIfVisible(SpriteBatch render) {
		font.draw(render, "Game Over ", 30, 50);
	}

}
