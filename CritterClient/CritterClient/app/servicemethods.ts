﻿﻿/// <reference path="../Libraries/typings/jquery/jquery.d.ts" />
/// <reference path="../Libraries/typings/jqueryui/jqueryui.d.ts" />
import {User, Pet, PetColor, PetSpecies, AccountInformationRequest, Friendship, Message, Notification, Store, Conversation, Item, InventoryGrouping, GamesInfo, GameThumbnail, MessageRequest, ItemRequest,
    StoreBackgroundImageOption, StoreClerkImageOption, UserImageOption, SearchResponse} from './dtos'
import {Application} from "./appservice"

export class ServiceMethods {
    static baseURL: string = "http://localhost:8080/api/critters/";
    static selectorValidator: string[];
    static jsessionID: string = null;

    private static doAjax(functionName: string, functionService: string, parameters: any, type: string = "POST"): JQueryPromise<any> {
        var param = parameters != null && parameters.constructor === String ? parameters : JSON.stringify(parameters);
        var pathParams = type == "GET" && parameters != null ? "/" + param : "";
        var settings: JQueryAjaxSettings = {
            url: ServiceMethods.baseURL + functionService + "/" + functionName + pathParams + (ServiceMethods.jsessionID != null ? ";jsessionid=" + ServiceMethods.jsessionID : ""),
            type: type,
            contentType: "application/json",
            xhrFields: {
                withCredentials: true,
                cache: false
            },
            cache: false,
            headers: {
                Cookie: ServiceMethods.jsessionID,
            },
            beforeSend: (xhr: any) => {  
                xhr.setRequestHeader('Cookie', ServiceMethods.jsessionID);
            },
            success: (json, status, args) => {
                if (args.getResponseHeader("SelectorValidator")) {
                    ServiceMethods.selectorValidator = args.getResponseHeader("SelectorValidator").split(":");
                }
                if (args.getResponseHeader("JSESSIONID")) {
                    ServiceMethods.jsessionID = args.getResponseHeader("JSESSIONID");
                }
            },
            error: Application.handleServerError,
            data: type == "POST" ? param : "",
            crossDomain: true,
        };
        return jQuery.ajax(settings);
    }

    public static logIn(user: User): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromLogin", "users", user);
    }

    public static getUserFromToken(selector: string, validator: string): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromToken", "users", { selector: selector, validator: validator });
    }

    public static createUser(userCreateRequest: AccountInformationRequest): JQueryPromise<User> {
        return ServiceMethods.doAjax("createUser", "users", userCreateRequest);
    }

    public static createPet(pet: Pet): JQueryPromise<Pet> {
        return ServiceMethods.doAjax("addPet", "users", pet);
    }

    public static changeUserInformation(userRequest: AccountInformationRequest): JQueryPromise<User> {
        return ServiceMethods.doAjax("changeUserInformation", "users", userRequest);
    }

    public static getPetSpecies(): JQueryPromise<PetSpecies[]> {
        return ServiceMethods.doAjax("getPetSpecies", "meta", null, "GET");
    }

    public static getPetColors(): JQueryPromise<PetColor[]> {
        return ServiceMethods.doAjax("getPetColors", "meta", null, "GET");
    }

    public static getUserFromID(id: number): JQueryPromise<User> {
        return ServiceMethods.doAjax("getUserFromID", "users", id, "GET");
    }

    public static startLongPolling(): JQueryPromise<Notification> {
        return ServiceMethods.doAjax("pollForNotifications", "meta", null, "GET");
    }

    /************** Friend Stuff **************/
    public static sendFriendRequest(request: Friendship): JQueryPromise<Friendship> {
        return ServiceMethods.doAjax("createFriendship", "friends", request);
    }

    public static respondToFriendRequest(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("respondToFriendRequest", "friends", request);
    }

    public static cancelFriendRequest(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("cancelFriendRequest", "friends", request);
    }

    public static deleteFriendship(request: Friendship): JQueryPromise<void> {
        return ServiceMethods.doAjax("deleteFriendship", "friends", request);
    }

    /************** Meta Stuff **************/

    public static searchUsers(searchTerm: string): JQueryPromise<SearchResponse> {
        return ServiceMethods.doAjax("searchUsers", "meta", searchTerm, "GET");
    }

    public static checkUserName(searchTerm: string): JQueryPromise<boolean> {
        return ServiceMethods.doAjax("checkUserName", "meta", searchTerm, "GET");
    }

    public static checkPetName(searchTerm: string): JQueryPromise<boolean> {
        return ServiceMethods.doAjax("checkPetName", "meta", searchTerm, "GET");
    }

    public static checkEmail(searchTerm: string): JQueryPromise<boolean> {
        return ServiceMethods.doAjax("checkEmail", "meta", searchTerm, "GET");
    }

    public static getShopkeeperImageOptions(): JQueryPromise<StoreClerkImageOption[]> {
        return ServiceMethods.doAjax("getShopkeeperImageOptions", "meta", "", "GET");
    }

    public static getUserImageOptions(): JQueryPromise<UserImageOption[]> {
        return ServiceMethods.doAjax("getUserImageOptions", "meta", "", "GET");
    }

    public static getStoreBackgroundOptions(): JQueryPromise<StoreBackgroundImageOption[]> {
        return ServiceMethods.doAjax("getStoreBackgroundOptions", "meta", "", "GET");
    }

    /************** Game Stuff **************/
    public static getGames(): JQueryPromise<GamesInfo> {
        return ServiceMethods.doAjax("getGames", "meta", "", "GET");
    }

    public static getSecureID(): JQueryPromise<{ selector: string }> {
        return ServiceMethods.doAjax("getSecureID", "games", "", "GET");
    }

    public static getUsernameGames(gameType: number, userName: string): JQueryPromise<string> {
        return ServiceMethods.doAjax("findUserNameGames", "games", gameType+"/"+userName, "GET");
    }

    public static getActiveGamesOfType(gameType: number): JQueryPromise<Array<any>> {
        return ServiceMethods.doAjax("getAllActiveGames", "games", gameType, "GET");
    }

    public static openGameServer(gameType: number, clientID: string, gameName: string): JQueryPromise<string> {
        return ServiceMethods.doAjax("openGameServer", "games", gameType + "/" + clientID + "/" + gameName, "GET");
    }

    public static connectToGameServer(gameID: string, clientID: string): JQueryPromise<string> {
        return ServiceMethods.doAjax("connectToGameServer", "games", gameID + "/" + clientID, "GET");
    }

    /************** Store Stuff **************/
    public static getStorefront(storeId: number): JQueryPromise<Store> {
        return ServiceMethods.doAjax("getStorefront", "commerce", storeId, "GET");
    }

    public static purchaseInventoryItemFromStore(request: ItemRequest): JQueryPromise<void> {
        return ServiceMethods.doAjax("purchaseInventoryItemFromStore", "commerce", request);
    }

    public static createStore(store: Store, background: StoreBackgroundImageOption, clerk: StoreClerkImageOption): JQueryPromise<Store> {
        return ServiceMethods.doAjax("createStore", "commerce", { store: store, backgroundImage: background, clerkImage: clerk });
    }

    public static editStore(store: Store, background: StoreBackgroundImageOption, clerk: StoreClerkImageOption): JQueryPromise<Store> {
        return ServiceMethods.doAjax("editStore", "commerce", { store: store, backgroundImage: background, clerkImage: clerk });
    }

    /************** Chat Stuff **************/
    public static sendMessage(request: Message): JQueryPromise<Message> {
        return ServiceMethods.doAjax("sendMessage", "chat", request);
    }

    public static getMailbox(): JQueryPromise<Conversation[]> {
        return ServiceMethods.doAjax("getMailbox", "chat", null, "GET");
    }

    public static setDelievered(request: MessageRequest): JQueryPromise<void> {
        return ServiceMethods.doAjax("markMessagesDelivered", "chat", request);
    }

    public static setReceived(request: MessageRequest): JQueryPromise<void> {
        return ServiceMethods.doAjax("markMessagesRead", "chat", request);
    }

    public static deleteMessage(messageId: number): JQueryPromise<void> {
        return ServiceMethods.doAjax("deleteMessage", "chat", messageId, "GET");
    }

    public static deleteMail(user: User, messageIds: number[]): JQueryPromise<void> {
        return ServiceMethods.doAjax("deleteMail", "chat", { user: user, messages: messageIds });
    }

    public static setUnread(user: User, messageIds: number[]): JQueryPromise<void> {
        return ServiceMethods.doAjax("markMessagesUnread", "chat", { user: user, messages: messageIds });
    }

    public static getUnreadMail(): JQueryPromise<Message[]> {
        return ServiceMethods.doAjax("getUnreadMail", "chat", null, "GET");
    }

    /************** Inventory Stuff **************/
    public static getInventory(request: User): JQueryPromise<InventoryGrouping[]> {
        return ServiceMethods.doAjax("getInventory", "users", request);
    }

    public static discardInventoryItems(user: User, items: Item[]): JQueryPromise<InventoryGrouping[]> {
        return ServiceMethods.doAjax("discardInventoryItem", "users", { user: user, items: items });
    }

    public static moveInventoryItemToStore(user: User, items: Item[]): JQueryPromise<InventoryGrouping[]> {
        return ServiceMethods.doAjax("moveInventoryItemToStore", "commerce", { user: user, items: items });
    }

    public static moveInventoryItemFromStore(user: User, items: Item[]): JQueryPromise<void> {
        return ServiceMethods.doAjax("removeInventoryItemFromStore", "commerce", { user: user, items: items });
    }

    public static searchInventory(searchTerm: string): JQueryPromise<InventoryGrouping[]> {
        return ServiceMethods.doAjax("searchInventory", "users", searchTerm, "GET");
    }
}







