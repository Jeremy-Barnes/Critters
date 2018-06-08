package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;


/**
 * Created by Jeremy on 1/07/2018.
 */
@Entity
@Table(name="npcs")
public class NPC extends DTO {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int npcID;
	private String name;
	private String description;
	private String imagePath;
	@XmlTransient
	private String actionHandlerScript;

	public NPC(String name, String description, String imagePath) {
		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
	}

	public NPC(String name, String description, String imagePath, String actionHandlerScript) {
		this.name = name;
		this.description = description;
		this.imagePath = imagePath;
		this.actionHandlerScript = actionHandlerScript;
	}

	public NPC(){}

	public int getNpcID() {
		return npcID;
	}

	public void setNpcID(int npcID) {
		this.npcID = npcID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@XmlTransient
	public String getActionHandlerScript() {
		return actionHandlerScript;
	}

	public void setActionHandlerScript(String actionHandlerScript) {
		this.actionHandlerScript = actionHandlerScript;
	}

	@Entity
	@Table(name="npcItemQuestPreferenceConfigs")
	public static class NPCItemPreferences extends DTO {
		@Id
		@GeneratedValue(generator = "increment")
		@GenericGenerator(name="increment", strategy = "increment")
		private int npcItemQuestPreferenceConfigID;
		private int wanterNPC;

		@ManyToOne
		@JoinColumn(name="itemID", updatable = false)
		private Item.ItemDescription item;
		private int critterBuxxValuePerItem;

	}

}
