/// <reference path="Libraries/typings/jquery/jquery.d.ts" />
/// <reference path="Libraries/typings/jqueryui/jqueryui.d.ts" />
/// <reference path="Libraries/typings/knockout/knockout.d.ts" />
/// <reference path="Libraries/typings/knockout.mapping/knockout.mapping.d.ts" />

var page: App;

$(document).ready(function () {
    $.ajaxSetup({ cache: false });
    page = new App();
    ko.applyBindings(page);
});

class App {
    public status: KnockoutObservable<AppStatus> = ko.observable(AppStatus.Landing);

    public user: KnockoutObservable<UserModel> = ko.observable(new UserModel());
    public passwordConfirm: KnockoutObservable<string> = ko.observable("");
    public passwordsMatch: KnockoutComputed<boolean>;

    constructor() {
        this.passwordsMatch = ko.pureComputed(() => {
            return this.user().password() == this.passwordConfirm();
        }, this);

        this.initUser();

    }//ctor

    private initUser(): void {
        let siteCookie: string = this.findCookie();
        if (siteCookie == null && ServiceMethods.selectorValidator == null) { //no user or too many users
            this.status(AppStatus.Landing);
        } else {
            let selectorValidator = siteCookie ? siteCookie.split(":") : ServiceMethods.selectorValidator;
            this.getLongTermCookieUser(selectorValidator[0], selectorValidator[1]);
        }
    }

    private findCookie(): string {
        let usefulCookies = ("; " + document.cookie).split("; critters="); //everyone else's garbage ; mine
        if (usefulCookies.length == 2) {
            return usefulCookies[1].split(";")[0]; //mine; other garbage
        } else {
            return null;
        }
    }

    public getLongTermCookieUser(selector: string, validator: string) {
        var self = this;
        ServiceMethods.getUserFromToken(selector, validator).then(function (o: User) {
            self.user(ko.mapping.fromJS(o));
            self.status(AppStatus.Home);
        }).fail(function (request: JQueryXHR) {
            alert(request);
        });
    }

    public logIn() {
        var self = this;
        ServiceMethods.logIn(this.user()).then(function (o: User) {
            self.user(ko.mapping.fromJS(o));
            self.passwordConfirm("");
            if (o.firstName.length && o.lastName.length) {
                self.status(AppStatus.Home);
            } else {
                self.status(AppStatus.Account);
            }
        }).fail(function (request: JQueryXHR) {
            alert(request);
        });
    }

    public createUser() {
        var self = this;
        ServiceMethods.createUser(this.user()).then(function (updatedUser: User) {
            var us = ko.mapping.fromJS(updatedUser);
            self.user(us);
            self.passwordConfirm("");
            self.status(AppStatus.Account);
        }).fail(function (request: JQueryXHR) {
            alert(request);
        });
    }

    public submitAccountChanges() {
        var self = this;
        ServiceMethods.changeUserInformation(this.user()).then(function (updatedUser: User) {
            var us = ko.mapping.fromJS(updatedUser);
            self.user(us);
            self.status(AppStatus.Home);
        }).fail(function (request: JQueryXHR) {
            alert(request);
        });
    }
}

enum AppStatus {
    Home, Account, Landing, Friends, FriendRequests
}

