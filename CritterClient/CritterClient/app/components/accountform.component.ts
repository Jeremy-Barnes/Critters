import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { User, UserImageOption } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: '../../templates/accountform.template.htm'
})

export class AccountFormComponent {
    changeImage: boolean = false;
    confirmPassword: string;
    user: User;
    images: UserImageOption[] = [];

    constructor(private router: Router) {
        var self = this;
        Application.getUserImageOptions().done((images: UserImageOption[]) => { self.images.push(...images); });
    }


    ngOnInit() { this.user = Application.getApp().user }

    changeImages() {
        this.changeImage = true;
        (<any>$("#viewImages")).modal('show'); //I'm not happy about this either.
    }

    onSubmit() {
        var self = this;

        Application.submitUserAccountUpdate(this.user, null).then((u: User) => { //todo image selection
        }).fail((error: JQueryXHR) => {
            alert("Error text received from server (do something with this later): \n\n" + error.responseText)
        });

        return false;
    }

    
}