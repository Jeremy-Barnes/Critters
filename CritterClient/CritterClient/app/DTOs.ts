﻿export class User {
    userID: number = -1;
    userName: string = "";
    firstName: string = "";
    lastName: string = "";
    emailAddress: string = "";
    password: string = "";
    birthdate: Date = null;
    salt: string = "";
    city: string = "";
    state: string = "";
    country: string = "";
    postcode: string = "";
    tokenSelector: string = "";
    tokenValidator: string = "";
    critterbuxx: number = 0;
    friends: Array<Friendship>;
}

export class Friendship {
    friendshipID: number;
    requesterUserID: User;
    requestedUserID: User;
    accepted: boolean;
}

