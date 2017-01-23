import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User, Notification, Message, Conversation } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/messages.template.htm"
})

export class MessageComponent implements OnInit {
    viewUser: User = new User();
    user: User;
    app: Application = Application.getApp();
    alerts: Notification[];
    messages: Conversation[];
    sentMessages: Message[];


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
}