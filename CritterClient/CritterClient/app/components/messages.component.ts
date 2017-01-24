import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User, Notification, Message, Conversation } from '../dtos'
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

    constructor(private route: ActivatedRoute) {
        prepDisplay();
        Application.getMailbox();
        this.user = this.app.user;
        this.alerts = this.app.alerts;
        this.messages = this.app.inbox;
        this.sentMessages = this.app.sentbox;
    }

    ngOnInit() {
    }

    debug() {
        alert(this.user);
        alert(Application.getApp());
        var ap2 = Application.getApp();
        alert(this.messages.length);
    }

    reply(replyMessage: Message) {
        this.replyMessage = replyMessage;
        this.newMessage = new Message();
    }

    returnToOverview() {
        this.activeConversation = null;
        this.replyMessage = null;
        this.newMessage = null;
    }

    cancelReply() {
        this.replyMessage = null;
        this.cancelCompose();
    }

    cancelCompose() {
        this.newMessage = null;
    }

    deleteConversation(...message: Message[]) {
        alert(message);
    }

    replyLatest() {
        this.reply(this.activeConversation[this.activeConversation.length-1]);
    }

    viewDetail(viewMessage: Conversation) {
        this.activeConversation = viewMessage.messages;
    }

    sendMessage() {
        this.newMessage.sender = this.user;
        if (this.replyMessage != null) {
            this.newMessage.parentMessage = this.replyMessage;
            this.newMessage.rootMessage = this.replyMessage.rootMessage != null ? this.replyMessage.rootMessage : this.activeConversation[0];
            this.newMessage.recipient = this.replyMessage.sender;
        }
        Application.sendMessage(this.newMessage);
    }

}