package com.critters.breakout.entities.ui;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.graphics.Render;
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
	public void renderIfVisible(Render render) {
		render.drawText("GameOver", Color.BLACK, 1260, 55);
	}

}
