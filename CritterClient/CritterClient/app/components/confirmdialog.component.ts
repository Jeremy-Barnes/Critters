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
    customBodyHTML: string;
    dangerButtonText: string;
    result: JQueryDeferred<boolean>;
    ngOnInit() { 
        Application.getApp().showDialogCallback = this.showDialog.bind(this);
    }
    
    showDialog(title: string, body: string, customBodyHTML: string, dangerButtonText: string) {
        this.title = title;
        this.body = body;
        this.customBodyHTML = customBodyHTML;
        this.dangerButtonText = dangerButtonText;
        (<any>$("#confirmDialog")).modal('show'); //I'm not happy about this either.
        this.result = $.Deferred();
        return this.result.promise();
    }

    respond(accept: boolean) {
        this.result.resolve(accept);
        (<any>$("#confirmDialog")).modal('hide'); //I'm not happy about this either.

    }
}
