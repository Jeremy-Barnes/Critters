package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * Created by Jeremy on 7/10/2018.
 */
@Entity
@Table(name="npcItemQuestPreferenceConfigs")
public class NPCItemQuestPreferenceConfig extends DTO {
	
  	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Integer npcItemQuestPreferenceConfigID;
	private int wanterNPC;
  
  	@ManyToOne
	@JoinColumn(name="itemID", updatable = false)
	private Item.ItemDescription item;
	private int critterBuxxValuePerItem;

	public NPCItemQuestPreferenceConfig(int wanterNPC, int critterBuxxValuePerItem, Item.ItemDescription item) {
		this.wanterNPC = wanterNPC;
		this.critterBuxxValuePerItem = critterBuxxValuePerItem;
		this.item = item;
	}

	public NPCItemQuestPreferenceConfig(){}

	public Integer getNpcItemQuestPreferenceConfigID() {
		return npcItemQuestPreferenceConfigID;
	}

	public void setNpcItemQuestPreferenceConfigID(Integer npcItemQuestPreferenceConfigID) {
		this.npcItemQuestPreferenceConfigID = npcItemQuestPreferenceConfigID;
	}

	public int getWanterNPC() {
		return wanterNPC;
	}

	public void setWanterNPC(int wanterNPC) {
		this.wanterNPC = wanterNPC;
	}

	public Item.ItemDescription getItem() {
		return item;
	}

	public void setItem(Item.ItemDescription item) {
		this.item = item;
	}

	public int getCritterBuxxValuePerItem() {
		return critterBuxxValuePerItem;
	}

	public void setCritterBuxxValuePerItem(int critterBuxxValuePerItem) {
		this.critterBuxxValuePerItem = critterBuxxValuePerItem;
	}
}
