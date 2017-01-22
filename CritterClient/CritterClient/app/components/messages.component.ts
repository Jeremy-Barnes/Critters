import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User, Notification } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/messages.template.htm"
})

export class MessageComponent implements OnInit {
    viewUser: User = new User();
    user: User;
    app: Application = Application.getApp();
    alerts: Notification[];

    constructor(
        private route: ActivatedRoute
    ) { this.user = this.app.user; prepDisplay(); this.alerts = this.app.alerts; }

    ngOnInit() {
        this.user = this.app.user;
        var self = this;
        this.route.params.forEach((params: Params) => {
            let id = parseFloat(params['id']);
            Application.getUser(id).done((u: User) => {
                self.viewUser = u;
            });
        });
    }
}
