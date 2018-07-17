package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Jeremy on 7/16/2018.
 */
@Entity
@Table(name="npcQuestRewardConfigs")
public class NPCQuestRewardConfig extends DTO {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Integer npcQuestRewardConfigID;
	private int rewardingNPCID;
	@ManyToOne
	@JoinColumn(name="rewardItemConfigID", updatable = false)
	Item.ItemDescription rewardItemConfig;
	boolean rarityMatch;
	Integer rarityMatchFactor;
	Float percentOdds;
	private String specialMessage;

	public NPCQuestRewardConfig(){}

	public NPCQuestRewardConfig(int rewardingNPCID, Item.ItemDescription rewardItemConfig, boolean rarityMatch, Integer rarityMatchFactor, Float percentOdds, String specialMessage) throws Exception {
		this.rewardingNPCID = rewardingNPCID;
		this.rewardItemConfig = rewardItemConfig;
		this.rarityMatch = rarityMatch;
		if(rarityMatch){
			if(rarityMatchFactor == null) {
				throw new Exception("If a rarity match is provided, a match factor must also be!");
			}
		} else {
			if(percentOdds == null) {
				throw new Exception("If no rarity match is provided, a percent to receive must be!");
			}
		}


	}

	public Integer getNPCQuestRewardConfigID() {
		return npcQuestRewardConfigID;
	}

	public void setNPCQuestRewardConfigID(Integer npcQuestRewardConfigID) {
		this.npcQuestRewardConfigID = npcQuestRewardConfigID;
	}

	public int getRewardingNPCID() {
		return rewardingNPCID;
	}

	public void setRewardingNPCID(int rewardingNPCID) {
		this.rewardingNPCID = rewardingNPCID;
	}

	public Item.ItemDescription getRewardItemConfig() {
		return rewardItemConfig;
	}

	public void setRewardItemConfig(Item.ItemDescription rewardItemConfig) {
		this.rewardItemConfig = rewardItemConfig;
	}

	public boolean isRarityMatch() {
		return rarityMatch;
	}

	public void setRarityMatch(boolean rarityMatch) {
		this.rarityMatch = rarityMatch;
	}

	public Integer getRarityMatchFactor() {
		return rarityMatchFactor;
	}

	public void setRarityMatchFactor(Integer rarityMatchFactor) {
		this.rarityMatchFactor = rarityMatchFactor;
	}

	public Float getPercentOdds() {
		return percentOdds;
	}

	public void setPercentOdds(Float percentOdds) {
		this.percentOdds = percentOdds;
	}

	public String getSpecialMessage() {
		return specialMessage;
	}

	public void setSpecialMessage(String specialMessage) {
		this.specialMessage = specialMessage;
	}
}
