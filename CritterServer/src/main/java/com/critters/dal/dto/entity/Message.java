package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by Jeremy on 8/14/2016.
 */
@Entity
@Table(name="messages")
public class Message {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int messageID;


	@ManyToOne
	@JoinColumn(name = "senderUserID")
	private User sender;

	@ManyToOne
	@JoinColumn(name = "recipientUserID")
	private User recipient;

	private boolean read;
	private boolean showSender;
	private boolean showRecipient;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateSent;

	private String messageText;
	private String messageSubject;

	@ManyToOne
	@JoinColumn(name = "rootMessageID")
	private Message rootMessage;

	@ManyToOne
	@JoinColumn(name = "parentMessageID")
	private Message parentMessage;

	public Message(User sender, User recipient, boolean read, boolean showSender, boolean showRecipient, Date dateSent, String messageText, String messageSubject, Message rootMessage, Message parentMessage) {
		this.sender = sender;
		this.recipient = recipient;
		this.read = read;
		this.showSender = showSender;
		this.showRecipient = showRecipient;
		this.dateSent = dateSent;
		this.messageText = messageText;
		this.messageSubject = messageSubject;
		this.rootMessage = rootMessage;
		this.parentMessage = parentMessage;
	}

	public Message(){}

	public boolean getShowSender() {
		return showSender;
	}

	public void setShowSender(boolean showSender) {
		this.showSender = showSender;
	}
	
	public boolean getShowRecipient() {
		return showRecipient;
	}

	public void setShowRecipient(boolean showRecipient) {
		this.showRecipient = showRecipient;
	}
	
	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int friendshipID) {
		this.messageID = friendshipID;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User requester) {
		this.sender = requester;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User requested) {
		this.recipient = requested;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}


	public Message getRootMessage() {
		return rootMessage;
	}

	public void setRootMessage(Message rootMessage) {
		this.rootMessage = rootMessage;
	}

	public Message getParentMessage() {
		return parentMessage;
	}

	public void setParentMessage(Message parentMessage) {
		this.parentMessage = parentMessage;
	}

	public void nullUsersOut(){
		sender = new User(sender);
		sender.nullAllButID();
		recipient = new User(recipient);
		recipient.nullAllButID();
	}

	public void nullParentsOut(){
		parentMessage = null;
		rootMessage = null;
	}
}
