import {Component, Output, Input, EventEmitter} from "@angular/core";

@Component({
    selector: "<autocomplete></autocomplete>",
    template: `<input type="text" (keyup)="this.handleInput($event)"/><br />
				<div class="search-results" *ngFor="let item of resultList">
                    <a (click)="onClick(item)">{{item}}</a>
               </div>`
})
export class AutocompleteList {
    public resultList: {resultText: string, resultData: any}[];
    private refreshTimer: any = undefined;
    private searching = false;
    private secondSearchNeeded = false;
    private inputSearchTerm: string;

    @Input("autocomplete-func") public asyncSearchFunction: (term: string) => Promise<Array<{ resultText: string, resultData: any }>>;
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


    public onClick(item: { text: string, data: any }) {
        this.selected.emit(item);
        this.clearResultList();
    }

    private clearResultList() {
        this.searching = false;
        this.secondSearchNeeded = false;
    }

    private callSearchFunction() {
        this.refreshTimer = null;
        if (this.inputSearchTerm != "") {
            this.searching = true;

            this.asyncSearchFunction(this.inputSearchTerm).then((results) => {
                this.searching = false;
                if (this.secondSearchNeeded) {
                    this.secondSearchNeeded = false;
                    this.callSearchFunction();
                } else {
                    this.resultList = results;
                }
            });
        }
    }
}