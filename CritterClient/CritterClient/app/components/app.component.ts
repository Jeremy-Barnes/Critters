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
    displayingAlerts: boolean
    searchString: string;
    ngOnInit() { this.user = this.app.user; prepDisplay(); this.alerts = this.app.alerts; }

    deliverAlerts() {
        Application.markAlertsDelivered(this.alerts);
        (<any>$('.messageslink')).popover().on("hidden.bs.popover", () => this.clearAlerts()); //isn't Javascript gross as hell?
    }

    clearAlerts() {
        this.alerts.length = 0;
        $('.messageslink').each(() => (<any>$(this)).popover('hide')); //:(
    }
    
    search(){
        Application.search(this.searchString).done(()=>{alert("someday, route to a results compoenent with these results.")});
    }
}
