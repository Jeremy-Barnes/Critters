package com.critters.dto;

import com.critters.DTO;
import com.critters.dal.entity.GameThumbnail;
import com.critters.games.GameController;

/**
 * Created by Jeremy on 2/25/2017.
 */
public class GamesInfo extends DTO {
	public GameThumbnail[] games;
	public GameThumbnail featuredGame;

	public GameController[] runningGames;
}
