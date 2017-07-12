import { Component, Input, OnInit  } from '@angular/core';
import { User } from '../dtos'
import {ServiceMethods} from "../servicemethods"
import {Application} from "../appservice"

@Component({
    selector: 'confirmDialog',
    templateUrl: '../../templates/confirmdialog.template.htm'
})

export class ConfirmDialogComponent implements OnInit {
    title: string;
    body: string;
    app = Application.getApp();
    ngOnInit() { this.user = this.app.user }

    accept(){}
    reject(){}
}
