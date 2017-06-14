﻿import {User, Pet, PetColor, PetSpecies, AccountInformationRequest, Friendship, Message, Notification, Store, Conversation, Item, InventoryGrouping, GamesInfo, GameThumbnail, UserImageOption, MessageRequest } from './dtos'
import {ServiceMethods} from "./servicemethods"


export class Application {
    public loggedIn: boolean = false;

    public user: User = new User();
    public petSpecies: PetSpecies[] = [];
    public petColors: PetColor[] = [];
    public alerts: Notification[] = [];
    public inbox: Conversation[] = [];
    public sentbox: Message[] = [];
    public inventory: InventoryGrouping[] = [];


    public errorCallback: (text: string) => void;
    public static app: Application = new Application();


    public games: GameThumbnail[] = [];

    public static getApp() {
        if (Application.app) {
            return Application.app;
        } else {
            Application.app = new Application();
            return Application.app;
        }
    }

    public static handleServerError(error: JQueryXHR) {
        if(error.status != 500)
            Application.getApp().errorCallback(error.responseText);
    }

    public static submitUserAccountCreationRequest(user: User, pet: Pet) : JQueryPromise<User> {
        var createRequest = new AccountInformationRequest();
        createRequest.pet = pet;
        createRequest.user = user;
        return ServiceMethods.createUser(createRequest);
    }

    public static submitCreatePet(pet: Pet): JQueryPromise<Pet> {
        return ServiceMethods.createPet(pet);
    }

    public static submitUserAccountUpdate(user: User, image: UserImageOption): JQueryPromise<User> {
        return ServiceMethods.changeUserInformation({ user: user, pet: null, imageChoice: image });
    }

    public static getPetSpecies(){
        if (Application.getApp().petSpecies.length == 0) ServiceMethods.getPetSpecies().done((p: PetSpecies[]) => { Application.getApp().petSpecies.length = 0; Application.getApp().petSpecies.push(...p); });
    }

    public static getPetColors() {
        if (Application.getApp().petColors.length == 0) ServiceMethods.getPetColors().done((p: PetColor[]) => { Application.getApp().petColors.length = 0; Application.getApp().petColors.push(...p); });
    }

    public static sendFriendRequest(requestingUserID: number, requestedUserID: number) {
        var requesterUser = new User();
        var requestedUser = new User();
        requesterUser.userID = requestingUserID;
        requestedUser.userID = requestedUserID;
        return ServiceMethods.sendFriendRequest({
            accepted: false,
            friendshipID: 0,
            requester: requesterUser,
            requested: requestedUser,
            dateSent: undefined
        }).done((friendRequest: Friendship) => {
            Application.getApp().user.friends.push(friendRequest);
        });
    }

    public static rejectFriendRequest(friendRequest: Friendship): JQueryPromise<void> {
        Application.getApp().user.friends.splice(Application.getApp().user.friends.indexOf(friendRequest), 1);
        friendRequest.accepted = false;
        return ServiceMethods.respondToFriendRequest(friendRequest);
    }

    public static acceptFriendRequest(friendRequest: Friendship): JQueryPromise<void> {
        friendRequest.accepted = true;
        return ServiceMethods.respondToFriendRequest(friendRequest);
    }

    public static cancelFriendRequest(friendRequest: Friendship): JQueryPromise<void> {
        Application.getApp().user.friends.splice(Application.getApp().user.friends.indexOf(friendRequest), 1);
        return ServiceMethods.cancelFriendRequest(friendRequest);
    }

    public static logIn(user: User) {
        return ServiceMethods.logIn(user).done((retUser: User) => {
            var app = Application.getApp()
            app.user.set(retUser);
            app.loggedIn = true;
            Application.startLongPolling();
            prepDisplayAfterLogin();
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

    public static getMailbox() {
        ServiceMethods.getMailbox().done((conversations: Conversation[]) => {
            var user = Application.getApp().user;
            var sentmsgs: Message[] = [];
            var alerts: Notification[] = [];
            var recConvos: Conversation[] = [];

            //PROCESS THE CONVERSATIONS FOR INBOX PURPOSES
            //future me: you could have done all this with a *very pretty* lambda but it would have required processing this array 3 times - notifications, inbox, sent box.
            //This is more efficient. Here's what could have been:
            //inbox.push(...(conversations.filter(c => c.messages.findIndex(m => m.recipient.userID == user.userID) != -1)))

            for (var i = 0; i < conversations.length; i++) { //all conversations
                let conv = conversations[i];
                let notReceived = true;
                for (var j = 0; j < conv.messages.length; j++) { //each message in conversation
                    let message = conv.messages[j];
                    if (message.sender.userID == user.userID) {
                        sentmsgs.push(message);
                    } else if (notReceived) { //rather than processing the whole array to find out if its already saved, track it with a boolean
                        notReceived = false;
                        recConvos.push(conv);
                        if (message.recipient.userID == user.userID && !message.delivered) {
                            alerts.push({ messages: [message], friendRequests: null });
                        }
                    }

                    for (var k = 0; k < conv.participants.length; k++) { //all participants in this conversation - setting up objects that are omitted for over-wire serialization
                        let participant = conv.participants[k];
                        if (message.recipient.userID == participant.userID) {
                            message.recipient = (participant);
                        }
                        if (message.sender.userID == participant.userID) {
                            message.sender = (participant);
                        }
                    }
                    message.dateSent = new Date(<any>message.dateSent);
                }
            }


            Application.getApp().sentbox.length = 0;
            Application.getApp().inbox.length = 0;
            Application.getApp().sentbox.push(...sentmsgs);
            Application.getApp().inbox.push(...recConvos);
            Application.getApp().alerts.push(...alerts);
        });
    }

    public static sendMessage(msg: Message) {
        ServiceMethods.sendMessage(msg);
    }

    public static markMessagesRead(messages: Message[]) {
        messages.forEach(m => m.read = true);
        ServiceMethods.setReceived({ messages : messages, user : Application.getApp().user });
    }

    public static markAlertsDelivered(alerts: Notification[]) {
        var messages : Message[] = [];
        alerts.filter(a => a.messages != null).forEach(a => a.messages.filter(m => m.recipient.userID == Application.getApp().user.userID).forEach(m => messages.push(m)));
        messages.forEach(m => m.delivered = true);
        ServiceMethods.setDelievered({ messages: messages, user: Application.getApp().user });
    }

    public static searchFriends(searchTerm: string) {
        searchTerm = searchTerm.toLowerCase();
        var app = Application.getApp();
        var results: { resultText: string, resultData: User }[] = [];
        if (app.user.friends != null && app.user.friends.length > 0) {
            for (var i = 0; i < app.user.friends.length; i++) {
                var friendship = app.user.friends[i];
                var resultData = friendship.requested.userID == app.user.userID ? friendship.requester : friendship.requested;

                if (resultData.userName.toLowerCase().includes(searchTerm) ||
                    resultData.firstName.toLowerCase().includes(searchTerm) ||
                    resultData.lastName.toLowerCase().includes(searchTerm)) {
                    let resultText = (resultData.firstName != null && resultData.firstName.length > 0 ? resultData.firstName + " " : "") +
                        (resultData.lastName != null && resultData.lastName.length > 0 ? resultData.lastName + " " : "");
                    resultText += (resultText.length > 0 ? "| " : "") + resultData.userName;
                    results.push({ resultText, resultData });
                }
            }
        }
        return results;
    }

    public static searchUsers(searchTerm: string) {
        var app = Application.getApp();
        return ServiceMethods.searchUsers(searchTerm);
    }

    public static getInventory() {
        ServiceMethods.getInventory(Application.getApp().user).done((inventory: InventoryGrouping[]) => {
            var user = Application.getApp().user;
            Application.getApp().inventory.length = 0;
            Application.getApp().inventory.push(...inventory);
        });
    }

    public static submitInventoryAction(actionCode: number, items: Item[]) {
        switch (actionCode) {
            case 0: ServiceMethods.moveInventoryItemToStore(this.getApp().user, items); break;
            case 1: case 0: ServiceMethods.discardInventoryItems(this.getApp().user, items); break;  
        }
    }

    public static getGames(): JQueryPromise<GamesInfo> {
        if (Application.getApp().games.length = 0) {
            return ServiceMethods.getGames().done((games: GamesInfo) => {
                Application.getApp().games.length = 0;
                Application.getApp().games.push(...games.games);
            });
        } else return jQuery.Deferred().resolve(null);
    }

}