export class User {
    userID: number = 0;
    userName: string = "";
    firstName: string = "";
    lastName: string = "";
    emailAddress: string = "";
    password: string = "";
    birthdate: Date = null;
    sex: string = "";
    salt: string = "";
    city: string = "";
    state: string = "";
    country: string = "";
    postcode: string = "";
    tokenSelector: string = "";
    tokenValidator: string = "";
    critterbuxx: number = 0;
    friends: Array<Friendship>;
    pets: Array<Pet>;

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


    }
}

export class Friendship {
    friendshipID: number;
    requester: User;
    requested: User;
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

