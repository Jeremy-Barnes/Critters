package com.critters.games;

import com.critters.dal.dto.entity.User;

/**
 * Created by Jeremy on 7/16/2017.
 */
public class PongController extends GameController {

	public PongController(String gameID, String title, User host, String hostClientID, int gameType) {
		super(gameID, title, host, hostClientID, gameType);
	}

	public void tick(){}
}
