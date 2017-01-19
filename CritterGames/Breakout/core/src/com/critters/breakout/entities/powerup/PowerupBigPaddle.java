package com.critters.breakout.entities.powerup;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.critters.breakout.math.Vector2f;

public class PowerupBigPaddle extends Powerup {

	public PowerupBigPaddle(Vector2f pos) {
		super(pos, 45);
		color = new Color(0x16A085ff);
	}


}
