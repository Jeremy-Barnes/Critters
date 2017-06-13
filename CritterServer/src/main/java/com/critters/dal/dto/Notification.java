package com.critters.dal.dto;

import com.critters.dal.dto.entity.Friendship;
import com.critters.dal.dto.entity.Message;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 1/17/2017.
 */
@XmlRootElement
public class Notification extends DTO {
	public List<Message> messages;
	public List<Friendship> friendRequests;

	public Notification(List<Message> messages, List<Friendship> friendRequests) {
		this.messages = messages;
		this.friendRequests = friendRequests;
	}

	public Notification(Message message, Friendship friendRequest) {
		this.messages = new ArrayList<Message>();
		messages.add(message);
		this.friendRequests = new ArrayList<Friendship>();
		friendRequests.add(friendRequest);
	}

	public Notification(){}

	public List<Friendship> getFriendRequests() {
		return friendRequests;
	}

	public void setFriendRequests(List<Friendship> friendRequests) {
		this.friendRequests = friendRequests;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}



}
