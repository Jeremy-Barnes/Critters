﻿export class User {
    userID: number = 0;
    userName: string = "";
    firstName: string = "";
    lastName: string = "";
    emailAddress: string = "";
    password: string = "";
    birthDay: number;
    birthMonth: number;
    sex: string = "";
    salt: string = "";
    city: string = "";
    state: string = "";
    country: string = "";
    postcode: string = "";
    tokenSelector: string = "";
    tokenValidator: string = "";
    critterbuxx: number = 0;
    userImagePath: string = "";
    friends: Array<Friendship>;
    pets: Array<Pet>;
    isActive: boolean = true;

    public set(user: User) {
        this.userID = user.userID;
        this.userName = user.userName;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.emailAddress = user.emailAddress;
        this.password = user.password;
        this.birthDay = user.birthDay;
        this.birthMonth = user.birthMonth;
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
}

export class Friendship {
    friendshipID: number;
    requester: User;
    requested: User;
    accepted: boolean;
    dateSent: Date;
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
    imagePathWithoutModifiers: string;
    speciesDescription: string;
}

export class PetColor {
    petColorConfigID: number;
    petColorName: string;
    patternPath: string;
}

export class AccountInformationRequest {
    user: User;
    pet: Pet;
    imageChoice: UserImageOption;
}

export class StoreInformationRequest {
    store: Store;
    background: StoreBackgroundImageOption;
    clerkImage: StoreClerkImageOption;
}


export class Message {
    messageID: number;
    sender: User;
    recipient: User;
    dateSent: Date;
    messageText: string;
    messageSubject: string;
    rootMessage: Message;
    parentMessage: Message;
    showSender: boolean;
    showRecipient: boolean;
}

export class Conversation {
    messages: Message[];
    participants: User[];
}

export class Notification {
    messages: Message[];
    friendRequests: Friendship[];
}

export class Store {
    storeConfigID: number;
    ownerId: number;
    storeStock: Item[];
    description: string;
    name: string;
    storeClerkImagePath: string;
    storeBackgroundImagePath: string;
}

export class Item {
    inventoryItemId: number;
    ownerId: number;
    storeStock: Item[];
    description: ItemDescription;
    price: string;
    containingStoreId: number;
}

export class ItemDescription {
    itemConfigID: number;
    itemName: string;
    itemDescription: string;
    itemClass: ItemClassification;
    rarity: ItemRarityType;
    imagePath: string;
}

export class ItemClassification {
    itemClassificationID: number;
    name: string;
}

export class ItemRarityType {
    itemRarityTypeID: number;
    name: string;
}

export class InventoryGrouping {
    inventoryItemsGrouped: Item[];
}

export class GameThumbnail {
    gameThumbnailConfigID: number;
    gameName: string;
    gameDescription: string;
    gameIconPath: string;
    gameURL: string;
}

export class GamesInfo {
    games: GameThumbnail[];
    featuredGame: GameThumbnail[];
}

export class StoreClerkImageOption {
    storeClerkImageOption: number;
    imagePath: string;
}

export class StoreBackgroundImageOption {
    storeBackgroundImageOption: number;
    imagePath: string;
}

export class UserImageOption {
    userImageOptionID: number;
    imagePath: string;
}