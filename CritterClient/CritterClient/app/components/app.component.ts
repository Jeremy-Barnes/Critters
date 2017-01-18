import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    selector: 'viewport',
    templateUrl: "../../templates/app.template.htm"
})

export class AppComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    ngOnInit() { this.user = this.app.user; prepDisplay(); }

    summonLogin() {
    }
}
