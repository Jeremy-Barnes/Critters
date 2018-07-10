package com.critters.dto;

import com.critters.dal.entity.NPC;

/**
 * Created by Jeremy on 8/9/2016.
 */
public class NPCResponse {

	public NPCResponse(){}

	public NPC npc;
	public String responseMessage;
	public NPCMessageItem[] subItems;
	public NPCQuestMessage[] questItems;

	public static class NPCMessageItem {
		public String messageText;
		public String messageImage;

		public NPCMessageItem(){}

		public NPCMessageItem(String text, String image){
			this.messageImage = image;
			this.messageText = text;
		}
	}

	public static class NPCQuestMessage extends NPCMessageItem {
		public Integer questStepID;
		public NPCQuestMessage[] subItems;
		public UINotification uiElement = new UINotification();

		public NPCQuestMessage(String text, String image, Integer questStepID, String questText, String questTitle) {
			super(text, image);
			this.questStepID = questStepID;
			this.uiElement = new UINotification();
			this.uiElement.title = questTitle;
			this.uiElement.body = questText;
		}

		public NPCQuestMessage(String text, String image, int questStepID, UINotification uiElement) {
			super(text, image);
			this.questStepID = questStepID;
			this.uiElement = uiElement;
		}

		public NPCQuestMessage() {}
	}
}