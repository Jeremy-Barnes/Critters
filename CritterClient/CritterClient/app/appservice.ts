import {User, Pet, PetColor, PetSpecies, AccountInformationRequest, Friendship, Message, Notification, Store, Conversation, Item, InventoryGrouping, GamesInfo, GameThumbnail, UserImageOption, MessageRequest } from './dtos'
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
    public friends: Friendship[] = [];
    public pendingFriendRequests: Friendship[] = [];
    public outstandingFriendRequests: Friendship[] = [];
    public secureID: string = "";

    public errorCallback: (text: string) => any;
    public showDialogCallback: (title: string, text: string, customHTML: string, dangerButtonText: string) => JQueryDeferred<boolean>;

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
        Application.getApp().errorCallback(error.responseText);
    }

    public static submitUserAccountCreationRequest(user: User, pet: Pet) : JQueryPromise<User> {
        var createRequest = new AccountInformationRequest();
        createRequest.pet = pet;
        createRequest.user = user;
        return ServiceMethods.createUser(createRequest).done((u: User) => {
            Application.getApp().user.set(u);
        });
    }

    public static submitCreatePet(pet: Pet): JQueryPromise<Pet> {
        return ServiceMethods.createPet(pet).done((p: Pet) => {
            Application.getApp().user.pets.push(p);
        });
    }

    public static getUserImageOptions() {
        return ServiceMethods.getUserImageOptions();
    }

    public static submitUserAccountUpdate(user: User, image: UserImageOption): JQueryPromise<User> {
        return ServiceMethods.changeUserInformation({ user: user, pet: null, imageChoice: image });
    }

    public static getPetSpecies(){
        if (Application.getApp().petSpecies.length == 0) ServiceMethods.getPetSpecies().done((p: PetSpecies[]) => { Application.getApp().petSpecies.length = 0; Application.getApp().petSpecies.push(...p); });
    }

    public static getPetColors() {
        if (Application.getApp().petColors.length == 0) return ServiceMethods.getPetColors().done((p: PetColor[]) => { Application.getApp().petColors.length = 0; Application.getApp().petColors.push(...p); });
        return $.Deferred().resolve(Application.getApp().petColors);
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
        Application.getApp().pendingFriendRequests.splice(Application.getApp().pendingFriendRequests.indexOf(friendRequest), 1);
        friendRequest.accepted = false;
        return ServiceMethods.respondToFriendRequest(friendRequest);
    }

    public static acceptFriendRequest(friendRequest: Friendship): JQueryPromise<void> {
        Application.getApp().pendingFriendRequests.splice(Application.getApp().pendingFriendRequests.indexOf(friendRequest), 1);
        friendRequest.accepted = true;
        return ServiceMethods.respondToFriendRequest(friendRequest);
    }

    public static cancelFriendRequest(friendRequest: Friendship): JQueryPromise<void> {
        Application.getApp().user.friends.splice(Application.getApp().user.friends.indexOf(friendRequest), 1);
        Application.getApp().outstandingFriendRequests.splice(Application.getApp().outstandingFriendRequests.indexOf(friendRequest), 1);
        return ServiceMethods.cancelFriendRequest(friendRequest);
    }

    public static logIn(user: User) {
        return ServiceMethods.logIn(user).done((retUser: User) => {
            var app = Application.getApp()
            app.user.set(retUser);
            app.pendingFriendRequests.push(...user.friends.filter(f => !f.accepted && f.requested.userID == user.userID));
            app.outstandingFriendRequests.push(...user.friends.filter(f => !f.accepted && f.requester.userID == user.userID));
            app.friends.push(...user.friends.filter(f => f.accepted));
            app.loggedIn = true;
            Application.startLongPolling();
            Application.getNotifications();
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

    public static getNotifications() {
        ServiceMethods.getUnreadMail().done((messages: Message[]) => {
            if (messages != null && messages.length != 0) {
                var notes: Notification[] = [];
                for(let i = 0; i < messages.length; i++) {
                    notes.push({ messages: [messages[i]], friendRequests: [] });
                }
                var user: User = Application.getApp().user;
                var frReqs = user.friends.filter(f => !f.accepted && f.requested.userID == user.userID);

                for(let i = 0; i < frReqs.length; i++) {
                    notes.push({ messages: [], friendRequests: [frReqs[i]]});
                }
                Application.getApp().alerts.push(...notes);
            }
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
                    message.selected = false;
                    conv.selected = false;
                    if (message.sender.userID == user.userID) {
                        sentmsgs.push(message);
                    }
                    if (notReceived && message.recipient.userID == user.userID) { //rather than processing the whole array to find out if its already saved, track it with a boolean
                        notReceived = false;

                        recConvos.push({messages: conv.messages.sort((a, b) => a.dateSent > b.dateSent ? 1 : (b.dateSent > a.dateSent ? -1 : 0)), participants: conv.participants, selected: false });
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

    public static sendMessage(msg: Message): JQueryPromise<Message> {
       return ServiceMethods.sendMessage(msg).done((message: Message) => {
            Application.getApp().sentbox.push(message);
        });
    }

    public static markMessagesRead(messages: Message[]) {
        messages.forEach(m => m.read = true);
        if (messages.length > 0)
            ServiceMethods.setReceived({ messages : messages, user : Application.getApp().user });
    }

    public static markAlertsDelivered(alerts: Notification[]) {
        var messages : Message[] = [];
        alerts.filter(a => a.messages != null).forEach(a => a.messages.filter(m => m.recipient.userID == Application.getApp().user.userID).forEach(m => messages.push(m)));
        messages.forEach(m => m.delivered = true);
        if (messages.length > 0)
            ServiceMethods.setDelievered({ messages: messages, user: Application.getApp().user });
    }

    public static massDeleteMessages(messages: Message[]) {
        ServiceMethods.deleteMail(Application.getApp().user, messages.map(m => m.messageID)).done(() => { });
    }

    public static massSetUnread(messages: Message[]) {
        ServiceMethods.setUnread(Application.getApp().user, messages.map(m => m.messageID)).done(() => { });
    }

    public static searchFriends(searchTerm: string) {
        searchTerm = searchTerm.toLowerCase();
        var app = Application.getApp();
        var results: { resultText: string, resultData: User }[] = [];
        if (app.user.friends != null && app.user.friends.length > 0) {
            for (var i = 0; i < app.user.friends.length; i++) {
                var friendship = app.user.friends[i];
                var resultData = friendship.requested.userID == app.user.userID ? friendship.requester : friendship.requested;
                resultData = $.extend(new User(), resultData);
                if (resultData.userName.toLowerCase().includes(searchTerm) ||
                    resultData.firstName.toLowerCase().includes(searchTerm) ||
                    resultData.lastName.toLowerCase().includes(searchTerm)) {
                    let resultText = resultData.userName;
                    let nameString = (resultData.firstName != null && resultData.firstName.length > 0 ? resultData.firstName + " " : "") + resultData.lastName;
                    resultText += (nameString.length > 0 ? " (" + nameString + ")" : "");
                    results.push({ resultText, resultData });
                }
            }
        }
        return results;
    }

    public static search(searchString: string) {
        return Application.searchUsers(searchString);
    }
    
    public static searchUsers(searchTerm: string) {
        var app = Application.getApp();
        return ServiceMethods.searchUsers(searchTerm);
    }

    public static getInventory() {
        ServiceMethods.getInventory(Application.getApp().user).done((inventory: InventoryGrouping[]) => {
            var user = Application.getApp().user;
            Application.getApp().inventory.length = 0;
            inventory.forEach(i => i.selected = false);
            Application.getApp().inventory.push(...inventory);
        });
    }

    public static moveItemsToStore(items: Item[], containingGroup: InventoryGrouping) {
        return ServiceMethods.moveInventoryItemToStore(this.getApp().user, items).done((i: InventoryGrouping[]) => {
            items.forEach(item => containingGroup.inventoryItemsGrouped.splice(containingGroup.inventoryItemsGrouped.indexOf(item), 1));
            if (containingGroup.inventoryItemsGrouped.length > 0) {
                containingGroup.inventoryItemsGrouped[0].description = items[0].description;
            } else {
                var inventory = Application.getApp().inventory;
                inventory.splice(inventory.indexOf(containingGroup), 1);
            }
        });
    }

    public static moveItemsToGarbage(items: Item[], containingGroup: InventoryGrouping): JQueryPromise<InventoryGrouping[]> {
        return ServiceMethods.discardInventoryItems(this.getApp().user, items).done((i: InventoryGrouping[]) => {
            items.forEach(item => containingGroup.inventoryItemsGrouped.splice(containingGroup.inventoryItemsGrouped.indexOf(item), 1));
            if (containingGroup.inventoryItemsGrouped.length > 0) {
                containingGroup.inventoryItemsGrouped[0].description = items[0].description;
            } else {
                var inventory = Application.getApp().inventory;
                inventory.splice(inventory.indexOf(containingGroup), 1);
            }
        });
    }

    public static searchInventory(searchTerm: string): JQueryPromise<InventoryGrouping[]> {
        return ServiceMethods.searchInventory(searchTerm);
    }

    public static getStore(id: number): JQueryPromise<Store> {
        return ServiceMethods.getStorefront(id);
    }

    public static searchStore(a: any): JQueryPromise<InventoryGrouping[]> { return null; }

    public static getGames(): JQueryPromise<GamesInfo> {
        if (Application.getApp().games.length = 0) {
            return ServiceMethods.getGames().done((games: GamesInfo) => {
                Application.getApp().games.length = 0;
                Application.getApp().games.push(...games.games);
            });
        } else return jQuery.Deferred().resolve(null);
    }

    public static getGameSecureID(): JQueryPromise<{ selector: string }> {
        return ServiceMethods.getSecureID().done((id: { selector: string }) => {
            Application.getApp().secureID = id.selector;
        });
    }

    public static getUsernameGames(gameType: number, userName: string): JQueryPromise<string> {
        return ServiceMethods.getUsernameGames(gameType, userName);
    }


    public static openGameServer(gameType: number, clientID: string, gameName: string): JQueryPromise<string> {
        return ServiceMethods.openGameServer(gameType, clientID, gameName);
    }

    public static connectToGameServer(gameID: string, clientID: string): JQueryPromise<string> {
        return ServiceMethods.connectToGameServer(gameID, clientID);
    }

    public static purchaseItems(items: Item[], containingGroup: InventoryGrouping, sellerStore: Store) {
        var app = Application.getApp();
        return ServiceMethods.purchaseInventoryItemFromStore({
            user: app.user, items: items
        }).done(() => {
            items.forEach(i => {
                i.ownerId = app.user.userID;
                i.price = null;
                i.containingStoreId = null;
                containingGroup.inventoryItemsGrouped.splice(containingGroup.inventoryItemsGrouped.indexOf(i), 1);
            });
            if (app.inventory && app.inventory.length > 0)
                app.inventory.find(g => g.inventoryItemsGrouped[0].description.itemConfigID == items[0].description.itemConfigID).inventoryItemsGrouped.push(...items);
            if (containingGroup.inventoryItemsGrouped.length > 0) {
                containingGroup.inventoryItemsGrouped[0].description = items[0].description;
            } else {
                sellerStore.storeStock.splice(sellerStore.storeStock.indexOf(containingGroup), 1);
            }
            var totalCost = 0;
            items.forEach(i => totalCost += i.price);
            app.user.critterbuxx -= totalCost;
        });
    }
}
