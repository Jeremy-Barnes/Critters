export class User {
    userID: number = 0;
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

export class Pet {
    petID: number;
    petName: string;
    sex: string;
    ownerID: number;
    isAbandoned: boolean;

    petColor: PetColor;
    petSpecies: PetSpecies;
}

export class PetSpecies {
    petSpeciesConfigID: number;
    petTypeName: string;
}

export class PetColor {
    petColorConfigID: number;
    petColorName: string;
}

export class CreateAccountRequest {
    user: User;
    pet: Pet;
}

