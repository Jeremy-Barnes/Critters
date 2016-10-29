﻿import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/userprofile.template.htm"
})

export class UserProfileComponent implements OnInit {
    user: User;

    constructor(
        private route: ActivatedRoute
    ) { }

    ngOnInit() {
        var self = this;
        this.route.params.forEach((params: Params) => {
            let id = params['id'];
            Application.getUser(id).done((u: User) => {
                self.user = u;
            });
        });
    }
}