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
    result = $.Deferred();
    ngOnInit() { 
        Application.getApp().confirmDialogCallback = this.showDialog.bind(this);
    }
    
    showDialog(title: string, body: string) {
        this.title = title;
        this.string = string;
        (<any>$("#confirmDialog")).modal('show'); //I'm not happy about this either.
        return this.result.promise();
    }

    accept(){ this.result.resolve(true); }
    reject(){ this.result.resolve(false); }
}
