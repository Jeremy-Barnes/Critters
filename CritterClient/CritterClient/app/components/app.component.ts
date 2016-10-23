import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    selector: 'viewport',
    templateUrl: "../../templates/app.template.htm"
})

export class AppComponent {
    user: User;

    ngOnInit() { this.user = Application.user; prepDisplay(); }

    summonLogin() {
    }

}
