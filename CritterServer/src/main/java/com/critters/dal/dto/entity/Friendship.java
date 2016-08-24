package com.critters.dal.dto.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by Jeremy on 8/14/2016.
 */
@Entity
@Table(name="friendships")
public class Friendship {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int friendshipID;


	@ManyToOne
	@JoinColumn(name = "requesterUserID")
	private User requester;

	@ManyToOne
	@JoinColumn(name = "requestedUserID")
	private User requested;

	private boolean accepted;
	@Temporal(TemporalType.DATE)
	private Date dateSent;

	public Friendship(User requester, User requested, boolean accepted, Date dateSent) {
		this.requester = requester;
		this.requested = requested;
		this.accepted = accepted;
		this.dateSent = dateSent;
	}

	public Friendship(){}

	public int getFriendshipID() {
		return friendshipID;
	}

	public void setFriendshipID(int friendshipID) {
		this.friendshipID = friendshipID;
	}

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	public User getRequested() {
		return requested;
	}

	public void setRequested(User requested) {
		this.requested = requested;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}
}
