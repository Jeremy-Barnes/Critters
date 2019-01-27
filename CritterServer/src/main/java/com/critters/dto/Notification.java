package com.critters.dto;

import com.critters.DTO;
import com.critters.dal.entity.Friendship;
import com.critters.dal.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeremy on 1/17/2017.
 * Generic holder for server -> client messages
 * Unopinionated on display, except for UINotifications
 */
public class Notification extends DTO {
	public List<Message> messages;
	public List<Friendship> friendRequests;
	public List<UINotification> serverMessages;

	public Notification(List<Message> messages, List<Friendship> friendRequests) {
		this.messages = messages;
		this.friendRequests = friendRequests;
	}

	public Notification(Message message, Friendship friendRequest, UINotification notification) {
		if(message != null) {
			this.messages = new ArrayList<Message>();
			messages.add(message);
		}
		if(friendRequest != null) {
			this.friendRequests = new ArrayList<Friendship>();
			friendRequests.add(friendRequest);
		}
		if(notification != null) {
			this.serverMessages = new ArrayList<UINotification>();
			serverMessages.add(notification);
		}
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


	public List<UINotification> getServerMessages() {
		return serverMessages;
	}

	public void setserverMessages(List<UINotification> serverMessages) {
		this.serverMessages = serverMessages;
	}



}
