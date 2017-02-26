import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User, Notification, Item, ItemDescription, InventoryGrouping } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/inventory.template.htm"
})

export class InventoryComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();
    alerts: Notification[];

    inventory: InventoryGrouping[];
    activeItemGroup: Item[] = [];
    composeToFriend: User;
    itemActions: { id: number, text: string }[] = [];
    selectedAction: { id: number, text: string } = null;

    private defaultActions = [{ id: 0, text: "Move to Shop"}, {id: 1, text: "Discard Item"}]


    constructor(private route: ActivatedRoute) {
        prepDisplay();
        Application.getInventory();
        this.user = this.app.user;
        this.alerts = this.app.alerts;
        this.inventory = this.app.inventory;
    }

    ngOnInit() {
    }

    debug() {
        alert(this.user);
        alert(Application.getApp());
        var ap2 = Application.getApp();
    }

    viewItem(viewItem: InventoryGrouping) {
        this.activeItemGroup.length = 0;
        this.activeItemGroup.push(...viewItem.inventoryItemsGrouped);
        this.itemActions.length = 0;
        this.itemActions.push(this.defaultActions[0]);
        //todo get context sensitive actions based on item type
        this.itemActions.push(this.defaultActions[1]);
        (<any>$("#viewItemDetail")).modal('show'); //I'm not happy about this either.
    }

    submitItem() {
        Application.submitInventoryAction(this.selectedAction.id, this.activeItemGroup);

    //    this.newMessage.sender = this.user;
    //    if (this.replyMessage != null) {
    //        this.newMessage.parentMessage = this.replyMessage;
    //        this.newMessage.rootMessage = this.replyMessage.rootMessage != null ? this.replyMessage.rootMessage : this.activeConversation[0];
    //        this.newMessage.recipient = this.replyMessage.sender;
    //    }
    //    Application.sendMessage(this.newMessage);
    }


    acceptRequest(friendRequest: Item[]) {
      //  Application.acceptFriendRequest(friendRequest);
    }

    public searchFriends(searchTerm: string) {
        return new Promise((resolve) => {
            resolve(Application.searchFriends(searchTerm));
        });
    }


    public searchUsers(searchTerm: string) {
        return new Promise((resolve) => {
            Application.searchUsers(searchTerm).done((results: User[]) => {
                var resultsForDisplay: { resultText: string, resultData: User }[] = [];
                for (var i = 0; i < results.length; i++) {
                    var resultData = results[i];

                    let resultText = (resultData.firstName != null && resultData.firstName.length > 0 ? resultData.firstName + " " : "") +
                        (resultData.lastName != null && resultData.lastName.length > 0 ? resultData.lastName + " " : "");
                    resultText += (resultText.length > 0 ? "| " : "") + resultData.userName;
                    resultsForDisplay.push({ resultText, resultData });
                }
                resolve(resultsForDisplay);
            });
        });
    }

    public onItemSelected(result: { resultText: string, resultData: User }) {
        this.composeToFriend = result.resultData;
    }

    public deselctComposeFriend() {
        this.composeToFriend = null;
    }
}