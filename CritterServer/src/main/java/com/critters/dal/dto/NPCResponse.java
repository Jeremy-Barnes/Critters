package com.critters.dal.dto;

import com.critters.dal.dto.entity.NPC;

/**
 * Created by Jeremy on 8/9/2016.
 */
public class NPCResponse {

	public NPCResponse(){}

	public NPC npc;
	public String responseMessage;
	public NPCMessageItem[] subItems;

	public static class NPCMessageItem {
		public NPCMessageItem(){}

		public NPCMessageItem(String text, String image){
			this.messageImage = image;
			this.messageText = text;
		}

		public String messageText;
		public String messageImage;
	}
}