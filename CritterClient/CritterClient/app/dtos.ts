export class User {
<<<<<<< HEAD
    userID: number = -1;
=======
    userID: number = 0;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
    userName: string = "";
    firstName: string = "";
    lastName: string = "";
    emailAddress: string = "";
    password: string = "";
    birthdate: Date = null;
<<<<<<< HEAD
=======
    sex: string = "";
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
    salt: string = "";
    city: string = "";
    state: string = "";
    country: string = "";
    postcode: string = "";
    tokenSelector: string = "";
    tokenValidator: string = "";
    critterbuxx: number = 0;
    friends: Array<Friendship>;
<<<<<<< HEAD
=======
    pets: Array<Pet>;
    isActive: boolean = true;

    public set(user: User) {
        this.userID = user.userID;
        this.userName = user.userName;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.emailAddress = user.emailAddress;
        this.password = user.password;
        this.birthdate = user.birthdate;
        this.sex = user.sex;
        this.salt = user.salt;
        this.city = user.city;
        this.state = user.state;
        this.country = user.country;
        this.postcode = user.postcode;
        this.tokenSelector = user.tokenSelector;
        this.tokenValidator = user.tokenValidator;
        this.critterbuxx = user.critterbuxx;
        this.friends = user.friends;
        this.pets = user.pets;
        this.isActive = user.isActive;

    }
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
}

export class Friendship {
    friendshipID: number;
<<<<<<< HEAD
    requesterUserID: User;
    requestedUserID: User;
=======
    requester: User;
    requested: User;
>>>>>>> 2b09b9c0877790f1aedb224f3ffcf2be39e0ef2a
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

