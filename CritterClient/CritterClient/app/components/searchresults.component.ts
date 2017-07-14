import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser'
import { ActivatedRoute, Params, Router } from '@angular/router';
import { User, SearchResponse } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/searchresults.template.htm"
})

export class SearchResultsComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    userResults: User[] = [];
    searchString: string;
    searchOp: JQueryPromise<SearchResponse>;

    ngOnInit() {
        this.user = this.app.user;
        var self = this;        
    }

    constructor(private route: ActivatedRoute, private router: Router, private domSanitizer: DomSanitizer) {
        var self = this;
        this.route.params.forEach((params: Params) => {
            self.searchString = params['searchTerm'];
        });
        this.searchOp = Application.searchUsers(self.searchString).done((users: SearchResponse) => {
            self.userResults.push(...users.users);
        });
    }

    
    selectResult(result: User) {
        let link = ['viewUser/' + result.userID];
        this.router.navigate(link);
    }
}
