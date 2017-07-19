package com.critters.games;

import com.critters.dal.dto.entity.User;

/**
 * Created by Jeremy on 7/16/2017.
 */
public class PongController extends GameController {

	private final int RIGHT_X = 150;
	private final int LEFT_X = 0;
	private final int TOP_Y = 100;
	private final int BOTTOM_Y = 0;

	private int ballX = 75;
	private int ballY = 50;
	private final int BALL_DIAMETER = 2;
	private int ballVelocity = 50;
	private int ballXV = 0;
	private int ballYV = 0;
	private int ballAngleDeg = 0;

	private final int PADDLE_LENGTH = 12;


	public PongController(String gameID, String title, String hostClientID, int gameType) {
		super(gameID, title, hostClientID, gameType);
	}

	public void addPlayer(User user){

	}

	public void tick(long dT){




	}
}
