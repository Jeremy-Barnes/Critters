import { Component, Input, OnInit } from '@angular/core';
import { User, Notification} from '../dtos'
import {Application} from "../appservice"

@Component({
    selector: 'viewport',
    templateUrl: "../../templates/app.template.htm"
})

export class AppComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    alerts: Notification[];
    ngOnInit() { this.user = this.app.user; prepDisplay(); this.alerts = this.app.alerts; }

    notifications() {
        (<any>$("#notifications-bubble")).modal('show'); //I'm not happy about this either.

    }

}
