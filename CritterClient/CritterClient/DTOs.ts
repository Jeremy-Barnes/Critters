class User {
    userID: number;
    userName: string;
    firstName: string;
    lastName: string;
    emailAddress: string;
    password: string;
    birthdate: Date;
    salt: string;
    city: string;
    state: string;
    country: string;
    postcode: string;
    tokenSelector: string;
    tokenValidator: string;
    critterbuxx: string;
    friends: Array<Friendship>;
}

class UserModel {
    userID: KnockoutObservable<number> = ko.observable(0);
    userName: KnockoutObservable<string> = ko.observable("");
    firstName: KnockoutObservable<string> = ko.observable("");
    lastName: KnockoutObservable<string> = ko.observable("");
    emailAddress: KnockoutObservable<string> = ko.observable("");
    password: KnockoutObservable<string> = ko.observable("");
    birthdate: Date;
    salt: KnockoutObservable<string> = ko.observable("");
    city: KnockoutObservable<string> = ko.observable("");
    state: KnockoutObservable<string> = ko.observable("");
    country: KnockoutObservable<string> = ko.observable("");
    postcode: KnockoutObservable<string> = ko.observable("");
    tokenSelector: KnockoutObservable<string> = ko.observable("");
    tokenValidator: KnockoutObservable<string> = ko.observable("");
    critterbuxx: KnockoutObservable<string> = ko.observable("");
    friends: KnockoutObservableArray<Friendship> = ko.observableArray([]);
}

class Friendship {
    friendshipID: number;
    requesterUserID: User;
    requestedUserID: User;
    accepted: boolean;
}

