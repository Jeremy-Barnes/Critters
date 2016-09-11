export class User {
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
    critterbuxx: number;
    friends: Array<Friendship>;
}

export class Friendship {
    friendshipID: number;
    requesterUserID: User;
    requestedUserID: User;
    accepted: boolean;
}

