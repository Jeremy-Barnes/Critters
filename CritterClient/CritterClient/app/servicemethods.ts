﻿/// <reference path="../Libraries/typings/jquery/jquery.d.ts" />
/// <reference path="../Libraries/typings/jqueryui/jqueryui.d.ts" />
import {User, Pet, PetColor, PetSpecies, AccountInformationRequest, Friendship, Message, Notification, Store, Conversation, Item, InventoryGrouping, GamesInfo, GameThumbnail } from './dtos'

export class ServiceMethods {
    static baseURL: string = "http://localhost:8080/api/critters/";// "http://40f167b1.ngrok.io/api/critters/";//"http://localhost:8080/api/critters/";
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

    public static searchUsers(searchTerm: string): JQueryPromise<User[]> {
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

    public static getGames(): JQueryPromise<GamesInfo> {
        return ServiceMethods.doAjax("getGames", "meta", "", "GET");
    }

    /************** Store Stuff **************/
    public static getStorefront(request: Store): JQueryPromise<Store> {
        return ServiceMethods.doAjax("getStorefront", "commerce", request);
    }

    /************** Chat Stuff **************/
    public static sendMessage(request: Message): JQueryPromise<Message> {
        return ServiceMethods.doAjax("sendMessage", "chat", request);
    }

    public static getMailbox(): JQueryPromise<Conversation[]> {
        return ServiceMethods.doAjax("getMailbox", "chat", null, "GET");
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
}







