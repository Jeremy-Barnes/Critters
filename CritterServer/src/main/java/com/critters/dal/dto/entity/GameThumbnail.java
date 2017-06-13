package com.critters.dal.dto.entity;

import com.critters.dal.dto.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by Jeremy on 8/14/2016.
 */
@Entity
@Table(name="gameThumbnailConfigs")
public class GameThumbnail extends DTO {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int gameThumbnailConfigID;
	private String gameName;
	private String gameDescription;
	private String gameIconPath;
	private String gameURL;
	private String bannerImagePath;
	private String thumbnailImagePath1;
	private String thumbnailImagePath2;

	public GameThumbnail(String gameName, String gameDescription, String gameIconPath, String gameURL, String bannerImagePath,
						 String thumbnailImagePath1, String thumbnailImagePath2) {
		this.gameName = gameName;
		this.gameDescription = gameDescription;
		this.gameIconPath = gameIconPath;
		this.gameURL = gameURL;
		this.bannerImagePath = bannerImagePath;
		this.thumbnailImagePath1 = thumbnailImagePath1;
		this.thumbnailImagePath2 = thumbnailImagePath2;
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

	public String getBannerImagePath() {
		return bannerImagePath;
	}

	public void setBannerImagePath(String bannerImagePath) {
		this.bannerImagePath = bannerImagePath;
	}

	public String getThumbnailImagePath1() {
		return thumbnailImagePath1;
	}

	public void setThumbnailImagePath1(String thumbnailImagePath1) {
		this.thumbnailImagePath1 = thumbnailImagePath1;
	}

	public String getThumbnailImagePath2() {
		return thumbnailImagePath2;
	}

	public void setThumbnailImagePath2(String thumbnailImagePath2) {
		this.thumbnailImagePath2 = thumbnailImagePath2;
	}
}
