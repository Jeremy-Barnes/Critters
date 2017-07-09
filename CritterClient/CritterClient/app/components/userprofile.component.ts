import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/userprofile.template.htm"
})

export class UserProfileComponent implements OnInit {
    viewUser: User = new User();
    user: User;
    app: Application = Application.getApp();
    constructor(
        private route: ActivatedRoute
    ) { }

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

    sendFriend() {
        Application.sendFriendRequest(this.user.userID, this.viewUser.userID);
    }

    sendMessage() {
        alert("TODO");
    }
}
