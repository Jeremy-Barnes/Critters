package com.critters.dal.entity;

import com.critters.DTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * Created by Jeremy on 7/10/2018.
 */
@Entity
@Table(name="npcQuestResponseConfigs")
public class NPCQuestResponseConfig extends DTO {

  	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private Integer npcQuestResponseConfigID;
	private int respondingNPCID;
	private String response;
	private boolean worksForFetchQuests;
	private boolean isSuccessResponse;

	public NPCQuestResponseConfig(){}

	public NPCQuestResponseConfig(int respondingNPCID, String response, boolean worksForFetchQuests, boolean isSuccessResponse) {
		this.respondingNPCID = respondingNPCID;
		this.response = response;
		this.worksForFetchQuests = worksForFetchQuests;
		this.isSuccessResponse = isSuccessResponse;
	}

	public Integer getNPCQuestResponseConfigID() {
		return npcQuestResponseConfigID;
	}

	public void setNPCQuestResponseConfigID(Integer npcItemQuestPreferenceConfigID) {
		this.npcQuestResponseConfigID = npcItemQuestPreferenceConfigID;
	}

	public int getRespondingNPCID() {
		return respondingNPCID;
	}

	public void setRespondingNPCID(int respondingNPCID) {
		this.respondingNPCID = respondingNPCID;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public boolean worksForFetchQuests() {
		return worksForFetchQuests;
	}

	public void setWorksForFetchQuests(boolean worksForFetchQuests) {
		this.worksForFetchQuests = worksForFetchQuests;
	}

	public boolean isSuccessResponse() {
		return isSuccessResponse;
	}

	public void setIsSuccessResponse(boolean isSuccessResponse) {
		this.isSuccessResponse = isSuccessResponse;
	}


}
