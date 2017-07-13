import { Component, Input, OnInit } from '@angular/core';
import { Params, Router } from '@angular/router';
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
    displayingAlerts: boolean
    searchString: string;
    ngOnInit() { this.user = this.app.user; prepDisplay(); this.alerts = this.app.alerts; }

    constructor(private router: Router) {
    }

    deliverAlerts() {
        Application.markAlertsDelivered(this.alerts);
        (<any>$('.messageslink')).popover().on("hidden.bs.popover", () => this.clearAlerts()); //isn't Javascript gross as hell?
    }

    clearAlerts() {
        this.alerts.length = 0;
        $('.messageslink').each(() => (<any>$(this)).popover('hide')); //:(
    }
    
    search() {
        let link = ['search/' + this.searchString];
        this.router.navigate(link);
    }
}
