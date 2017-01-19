import { Component, Input, OnInit } from '@angular/core';
import { User } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/dashboard.template.htm"
})

export class DashboardComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    ngOnInit() { this.user = this.app.user; }
}
