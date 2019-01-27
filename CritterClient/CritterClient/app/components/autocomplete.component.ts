﻿import {Component, Output, Input, EventEmitter} from "@angular/core";
import { Comparable } from '../dtos'


@Component({
    selector: "<autocomplete></autocomplete>",
    template: `<div style="position: relative" class="show"><input type="text" placeholder="{{placeholder}}" [(ngModel)]="searchTextModel" (ngModelChange)="checkClearInput($event)" (keyup)="this.handleInput($event)" class="form-control" />
                <div *ngIf="searchTextModel != null && searchTextModel.length > 0">
				    <div class="popover bottom" style="display: block; position: absolute; top: 40px; left: 10px; width: 90%; max-width: 400px;" >
                        <div class="arrow" style="top: -10px; left: 20px;"></div>
                        <div class="popover-content">
                            <div *ngFor="let item of resultList" class="popover-repeater">
                            <a (click)="onClick(item)">
                                <div class="popover-repeater-padding">
                                    <img *ngIf="item.resultData.userImagePath" class="userThumb" src="{{item.resultData.userImagePath}}" style="margin-right: 10px;" />{{item.resultText}}
                                </div>
                            </a>
                        </div>
                        </div>
                    </div>
                </div></div>`
    
})
export class AutocompleteList {
    public resultList: { resultText: string, resultData: Comparable }[] = [];
    private refreshTimer: any = undefined;
    private searching = false;
    private secondSearchNeeded = false;
    private inputSearchTerm: string;
    private searchTextModel: string;

    @Input("autocomplete-local-func") public localSearchFunction: (term: string) => Promise<Array<{ resultText: string, resultData: Comparable }>>;
    @Input("autocomplete-remote-func") public remoteSearchFunction: (term: string) => Promise<Array<{ resultText: string, resultData: Comparable }>>;
    @Input("placeholder") public placeholder: string = "";

    @Output("result-out") public selected = new EventEmitter();


    public handleInput(event: any) {
        if (!this.refreshTimer) {
            this.refreshTimer = setTimeout(() => {
                if (!this.searching) {
                    this.callSearchFunction();
                } else {
                    this.secondSearchNeeded = true;
                }
            },
            200);
        }
        this.inputSearchTerm = event.target.value;
        if (this.inputSearchTerm == "") {
            this.clearResultList();
        }
    }


    public onClick(item: { text: string, data: Comparable }) {
        this.selected.emit(item);
        this.clearResultList();
    }

    private clearResultList() {
        this.searching = false;
        this.secondSearchNeeded = false;
        this.resultList = [];
    }

    private callSearchFunction() {
        this.refreshTimer = null;
        if (this.inputSearchTerm != "") {
            this.searching = true;

            this.remoteSearchFunction(this.inputSearchTerm).then((results) => {
                
                this.searching = false;
                if (this.secondSearchNeeded) {
                    this.secondSearchNeeded = false;
                    this.callSearchFunction();
                } else {
                    if (results != null && results.length > 0)
                        this.resultList.push(...results.filter(a => this.resultList.find(r => r.resultData.isSame(a.resultData))? false : true));
                }
            });

            this.localSearchFunction(this.inputSearchTerm).then((results) => {
                if (results != null && results.length > 0)
                    this.resultList.push(...results.filter(a => this.resultList.indexOf(a) == -1)); 
            });
        }
    }

    checkClearInput(event: any) {
        if (event == "" || event == null) {
            this.clearResultList();
            this.searching = false;
        }
    }
}