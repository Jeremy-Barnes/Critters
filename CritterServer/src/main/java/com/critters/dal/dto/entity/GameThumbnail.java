package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by Jeremy on 8/14/2016.
 */
@Entity
@Table(name="gameThumbnailConfigs")
public class GameThumbnail {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int gameThumbnailConfigID;
	private String gameName;
	private String gameDescription;
	private String gameIconPath;
	private String gameURL;

	public GameThumbnail(String gameName, String gameDescription, String gameIconPath, String gameURL) {
		this.gameName = gameName;
		this.gameDescription = gameDescription;
		this.gameIconPath = gameIconPath;
		this.gameURL = gameURL;
	}

	public GameThumbnail(){
	}

	public int getGameThumbnailConfigID() {
		return gameThumbnailConfigID;
	}

	public void setGameThumbnailConfigID(int gameThumbnailConfigID) {
		this.gameThumbnailConfigID = gameThumbnailConfigID;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameDescription() {
		return gameDescription;
	}

	public void setGameDescription(String gameDescription) {
		this.gameDescription = gameDescription;
	}

	public String getGameIconPath() {
		return gameIconPath;
	}

	public void setGameIconPath(String gameIconPath) {
		this.gameIconPath = gameIconPath;
	}

	public String getGameURL() {
		return gameURL;
	}

	public void setGameURL(String gameURL) {
		this.gameURL = gameURL;
	}


}
