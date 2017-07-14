import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User, Notification, Message, Conversation, Friendship, SearchResponse } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/messages.template.htm"
})

export class MessageComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    alerts: Notification[];
    messages: Conversation[];
    sentMessages: Message[];
    activeConversation: Message[];
    replyMessage: Message;
    newMessage: Message;
    composeToFriend: User;
    selectedConversations: Conversation[] = [];
    selectedMessages: Message[] = [];
    pendingFriendRequests: Friendship[];
    outstandingFriendRequests: Friendship[];
    friends: Friendship[];
    tab: number = 0;
    loadingTabs = false;
    constructor(private route: ActivatedRoute) {
        Application.getMailbox();
        this.user = this.app.user;
        this.friends = this.app.friends;
        this.pendingFriendRequests = this.app.pendingFriendRequests;
        this.outstandingFriendRequests = this.app.outstandingFriendRequests;
        this.alerts = this.app.alerts;
        this.messages = this.app.inbox;
        this.sentMessages = this.app.sentbox;
    }

    ngOnInit() {
    }

    reply(replyMessage: Message) {
        this.replyMessage = replyMessage;
        this.composeNewMessage();
    }

    composeNewMessage() {
        this.newMessage = new Message();
        this.newMessage.selected = false;
    }

    selectMessage($event: any, item: Message) {
        $event.stopPropagation();
        (<any>item).selected = !(<any>item).selected;
        (<any>item).selected ? this.selectedMessages.push(item) : this.selectedMessages.splice(this.selectedMessages.indexOf(item), 1);
        return false;
    }

    selectConversation($event: any, item: Conversation) {
        $event.stopPropagation();
        (<any>item).selected = !(<any>item).selected;
        (<any>item).selected ? this.selectedConversations.push(item) : this.selectedConversations.splice(this.selectedConversations.indexOf(item), 1);
        return false;
    }

    isUnread(conversation: Conversation) {
        return conversation.messages.filter(m => !m.read && m.recipient.userID == this.user.userID).length > 0
    }

    returnToOverview(tab: number) {
        if(this.newMessage && this.newMessage.messageText != null && this.newMessage.messageText.length > 0) {
            var self = this;
            Application.getApp().showDialogCallback("Are you sure?", "You are about to discard your message - your message will be lost if you continue.", null, null).done((okay: boolean) => {
                if (okay) {
                    this.activeConversation = null;
                    this.cancelMessage();
                    this.tab = tab;
                }
            });
        } else {
            this.activeConversation = null;
		    this.cancelMessage();
            this.tab = tab;
        }
    }

    cancelReply() {
        this.cancelMessage();
    }
	
	cancelMessage(){
		this.replyMessage = null;
		this.newMessage = null;
		this.composeToFriend = null;
	}

    cancelCompose() {
        this.loadingTabs = true;
        this.cancelMessage();
        var tabname = this.tab == 0 ? "inbox" : this.tab == 1 ? "sent" : "friends";
        (<any>$("#messageTabs a[href=\"#" + tabname + "\"]")).tab('show'); //I'm not happy about this either.
    }


    deleteConversation(messages: Message[]) {
        alert("this one doesn't work yet, make the server op for it, dummy");
        Application.massDeleteMessages(messages);
    }

    markUnread(messages: Message[]) {
        alert("this one doesn't work yet, make the server op for it, dummy");
        messages.forEach(m => m.read = true);
    }

    affectSelectedInbox(deleteMessages: boolean) {
        if (deleteMessages) {
            var deletes: Message[] = [];
            this.selectedConversations.forEach(p => deletes.push(...p.messages));
            this.deleteConversation(deletes);
        } else {
            var unreads: Message[] = [];
            this.selectedConversations.forEach(p => unreads.push(...p.messages));
            this.markUnread(unreads);
        }
    }

    affectSelectedSentbox(deleteMessages: boolean) {
        if (deleteMessages) {
            this.deleteConversation(this.selectedMessages);
        }
    }

    replyLatest() {
        this.reply(this.activeConversation[this.activeConversation.length-1]);
    }

    viewDetail(viewMessage: Conversation) {
        this.activeConversation = viewMessage.messages;
        Application.markMessagesRead(this.activeConversation.filter(m => !m.read && m.recipient.userID == this.user.userID))
    }

    viewSentDetail(message: Message) {
        this.activeConversation = [message];
    }

    sendMessage() {
        this.newMessage.sender = this.user;
        this.newMessage.dateSent = new Date(Date.now());
        this.newMessage.selected = false;
        if (this.replyMessage != null) {
            this.newMessage.parentMessage = this.replyMessage;
            this.newMessage.rootMessage = this.replyMessage.rootMessage != null ? this.replyMessage.rootMessage : this.activeConversation[0];
            this.newMessage.recipient = this.replyMessage.sender;
            this.activeConversation.push(this.newMessage);
        } else {
            this.newMessage.recipient = this.composeToFriend;
        }
        var self = this;
        var newMessagePersist: Message = this.newMessage;
        Application.sendMessage(this.newMessage).done((message: Message) => newMessagePersist.messageID = message.messageID);
        this.newMessage = null;
    }

    acceptRequest(friendRequest: Friendship) {
        Application.acceptFriendRequest(friendRequest);
    }

    declineRequest(friendRequest: Friendship) {
        Application.rejectFriendRequest(friendRequest);
    }

    cancelRequest(friendRequest: Friendship) {
        Application.cancelFriendRequest(friendRequest);
    }

    toggleHideMessage(message: Message) {
        if (message.collapsed) { //not just doing c = !c because not sure how null/undefined reacts with that.
            message.collapsed = false;
        } else {
            message.collapsed = true;
        }
    }

    writeMessage(friend: Friendship) {
        var toUser = friend.requested.userID == this.user.userID ? friend.requester : friend.requested;
        this.composeNewMessage();
        this.onItemSelected({
            resultText: toUser.userName + (toUser.firstName + (toUser.lastName.length > 0 ? " " : "") + toUser.lastName), resultData: toUser
        });
    }

    public searchFriends(searchTerm: string) {
        return new Promise((resolve) => {
            resolve(Application.searchFriends(searchTerm));
        });
    }

    public searchUsers(searchTerm: string) {
        return new Promise((resolve) => {
            Application.searchUsers(searchTerm).done((results: SearchResponse) => {
                var resultsForDisplay: { resultText: string, resultData: User }[] = [];
                for (var i = 0; i < results.users.length; i++) {
                    var resultData = results.users[i];
                    resultData = $.extend(new User(), resultData);
                    let resultText = resultData.userName;
                    let nameString = (resultData.firstName != null && resultData.firstName.length > 0 ? resultData.firstName + " " : "") + resultData.lastName;
                    resultText += (nameString.length > 0 ? " (" + nameString + ")" : "");
                    resultsForDisplay.push({ resultText, resultData });
                }
                resolve(resultsForDisplay);
            });
        });
    }

    public onItemSelected(result: { resultText: string, resultData: User }) {
        this.composeToFriend = result.resultData;
    }

    public deselctComposeFriend() {
        this.composeToFriend = null;
    }

    public selectFriendship($event: any, friend: Friendship) {
        alert(friend.dateSent);
    }

}
