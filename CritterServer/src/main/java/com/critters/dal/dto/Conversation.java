package com.critters.dal.dto;

import com.critters.dal.dto.entity.Message;
import com.critters.dal.dto.entity.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Jeremy on 1/17/2017.
 */
@XmlRootElement
public class Conversation {
	public List<Message> messages;
	public List<User> participants;

	public Conversation(List<Message> messages, List<User> participants) {
		this.messages = messages;
		this.participants = participants;
	}

	public Conversation(){}

	public List<User> getParticipants() {
		return participants;
	}

	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}



}
