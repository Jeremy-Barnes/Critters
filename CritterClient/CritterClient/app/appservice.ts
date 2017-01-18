import {User, Pet, PetColor, PetSpecies, CreateAccountRequest, Friendship} from './dtos'
import {ServiceMethods} from "./servicemethods"


export class Application {
    public loggedIn: boolean = false;

    public user: User = new User();
    public petSpecies: PetSpecies[] = [];
    public petColors: PetColor[] = [];
    public static app: Application = new Application();


    public static getApp() {
        if (Application.app) {
            return Application.app;
        } else {
            Application.app = new Application();
            return Application.app;
        }
    }

    public static submitUserAccountCreationRequest(user: User, pet: Pet) : JQueryPromise<User> {
        var createRequest = new CreateAccountRequest();
        createRequest.pet = pet;
        createRequest.user = user;
        return ServiceMethods.createUser(createRequest);
    }

    public static submitCreatePet(pet: Pet): JQueryPromise<Pet> {
        return ServiceMethods.createPet(pet);
    }

    public static submitUserAccountUpdate(user: User): JQueryPromise<User> {
        return ServiceMethods.changeUserInformation(user);
    }

    public static getPetSpecies(): JQueryPromise<PetSpecies[]> {
        var self = this;
        return ServiceMethods.getPetSpecies().done((p: PetSpecies[]) => { Application.getApp().petSpecies.push(...p); });
        //return [{ petSpeciesConfigID: 1, petTypeName: "dog" }, { petSpeciesConfigID: 2, petTypeName: "cat" }, { petSpeciesConfigID: 3, petTypeName: "horrible clion" }]; //todo replace with server call, this is test data
    }

    public static getPetColors(): JQueryPromise<PetColor[]>{
        return ServiceMethods.getPetColors().done((p: PetColor[]) => { Application.getApp().petColors.push(...p); });
        //return [{ petColorConfigID: 1, petColorName: "blue" }, { petColorConfigID: 2, petColorName: "red" }, { petColorConfigID: 3, petColorName: "octarine" }]; //todo replace with server call, this is test data

    }

    public static sendFriendRequest(requestingUserID: number, requestedUserID: number): JQueryPromise<void> {
        var requesterUser = new User();
        var requestedUser = new User();
        requesterUser.userID = requestingUserID;
        requestedUser.userID = requestedUserID;
        return ServiceMethods.sendFriendRequest({
            accepted: false,
            friendshipID: 0,
            requester: requesterUser,
            requested: requestedUser
        });
    }

    public static logIn(user: User) {
        return ServiceMethods.logIn(user).done((retUser: User) => {
            var app = Application.getApp()
            app.user.set(retUser);
            app.loggedIn = true;
        });
    }

    public static getUser(id: number) {
        return ServiceMethods.getUserFromID(id);
    }
}