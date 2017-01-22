import {User, Pet, PetColor, PetSpecies, CreateAccountRequest, Friendship, Message, Notification} from './dtos'
import {ServiceMethods} from "./servicemethods"


export class Application {
    public loggedIn: boolean = false;

    public user: User = new User();
    public petSpecies: PetSpecies[] = [];
    public petColors: PetColor[] = [];
    public alerts: Notification[] = [];
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
    }

    public static getPetColors(): JQueryPromise<PetColor[]>{
        return ServiceMethods.getPetColors().done((p: PetColor[]) => { Application.getApp().petColors.push(...p); });
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
            Application.startLongPolling();
        });
    }

    public static getUser(id: number) {
        return ServiceMethods.getUserFromID(id);
    }

    private static startLongPolling() {
        ServiceMethods.startLongPolling().done((a: Notification) => {
            Application.getApp().alerts.push(a);
            Application.startLongPolling();
        }).fail((x) => {
            Application.startLongPolling();
        });
    }



}