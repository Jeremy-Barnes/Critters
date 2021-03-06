﻿import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User, Notification, Item, ItemDescription, InventoryGrouping } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/inventory.template.htm"
})

export class InventoryComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();

    /**** View State Controllers ***/
    inventory: InventoryGrouping[];
    activeItem: InventoryGrouping;
    selectedQuantity: number = 0;

    selectedItems: InventoryGrouping[] = [];
    bulkSelect: boolean = false;

    searchText: string = "";

    itemActions: { id: number, text: string }[] = [];
    selectedAction: { id: number, text: string } = null;
    actionCompleted: boolean = false;
    statusText: string = "";

    /******** Logical Storage ******/
    fullInventoryStoredForSearch: InventoryGrouping[] = [];

    sorts: { id: number, text: string }[] = [{ id: 1, text: "Name (A-Z)" }, { id: 2, text: "Name (Z-A)" }, { id: 3, text: "Quantity (High-Low)" }, { id: 4, text: "Quantity (Low-High)" },
        { id: 5, text: "Rarity (High-Low)" }, { id: 6, text: "Rarity (Low-High)" }, { id: 7, text: "Group by type" }]
    activeSortBy: { id: number, text: string } = null;
    private defaultActions = [{ id: 0, text: "Move to Shop" }, { id: 1, text: "Discard Item" }]
    private contextActions = [{ id: 2, classes: [1], text: "Feed to Pet" }, { id: 3, classes: [4], text: "Equip to Pet" }, { id: 4, classes: [2], text: "Play with toy" }]

    constructor(private route: ActivatedRoute) {
        Application.getInventory();
        this.user = this.app.user;
        this.inventory = this.app.inventory;
    }

    ngOnInit() {

    }

    selectItem($event: any, item: InventoryGrouping) {
        $event.stopPropagation();
        (<any>item).selected = !(<any>item).selected;
        (<any>item).selected ? this.selectedItems.push(item) : this.selectedItems.splice(this.selectedItems.indexOf(item), 1);
        return false;
    }

    toggleBulk() {
        this.bulkSelect = !this.bulkSelect;
        if (!this.bulkSelect) {
            this.selectedItems.forEach(s => s.selected = false);
            this.selectedItems.length = 0;

        }
    }

    selectedQuantityChange(value: number) {
        this.selectedQuantity = value;
    }

    viewItem(viewItem: InventoryGrouping) {
        this.actionCompleted = false;
        this.activeItem = viewItem;
        this.selectedQuantity = 0;

        this.itemActions.length = 0;
        this.itemActions.push(...this.defaultActions);
        this.itemActions.push(...this.contextActions.filter((act) => act.classes.indexOf(this.activeItem.inventoryItemsGrouped[0].description.itemClass.itemClassificationID) >= 0));

        (<any>$("#viewItemDetail")).modal('show'); //I'm not happy about this either.
    }

    submitItem() {
        var items: Item[] = [];
        if (this.selectedQuantity == 0) {
            //do nothing, complain. showComplaint()
            alert("No no");
        } else {
            if (this.activeItem.inventoryItemsGrouped.length == this.selectedQuantity) {
                items = [];
                items.push(...this.activeItem.inventoryItemsGrouped);
            } else {
                items = this.activeItem.inventoryItemsGrouped.slice(0, this.selectedQuantity);
            }
            var promise: any;
            switch (this.selectedAction.id) {
                case 0: promise = Application.moveItemsToStore(items, this.activeItem);
                    this.statusText = this.selectedQuantity + " " + items[0].description.itemName + (this.selectedQuantity == 1 ? " " : "s ") + "moved to your shop!";
                    break;
                case 1: promise = Application.moveItemsToGarbage(items, this.activeItem);
                    this.statusText = this.selectedQuantity + " " + items[0].description.itemName +(this.selectedQuantity == 1 ? " " : "s ") + "discarded!";
                    break;
                case 2: alert("Nom nom"); break;
                case 3: alert("SHIIIIING"); break;
                case 4: alert("what fun"); break;
            }
            var self = this;
            promise.done((group: InventoryGrouping) => {
                self.actionCompleted = true;
            });
        } 
    }

    onChangeSort(event: any) {
        this.activeSortBy = event;
        var self = this;
        this.inventory.sort((a, b) => {
            switch (self.activeSortBy.id) {
                case SortType.NameAsc: return a.inventoryItemsGrouped[0].description.itemName > b.inventoryItemsGrouped[0].description.itemName ? 1 :
                    a.inventoryItemsGrouped[0].description.itemName < b.inventoryItemsGrouped[0].description.itemName ? -1 : 0;
                case SortType.NameDesc: return a.inventoryItemsGrouped[0].description.itemName > b.inventoryItemsGrouped[0].description.itemName ? -1 :
                    a.inventoryItemsGrouped[0].description.itemName < b.inventoryItemsGrouped[0].description.itemName ? 1 : 0;
                case SortType.QtyAsc: return a.inventoryItemsGrouped.length < b.inventoryItemsGrouped.length ? 1 :
                    a.inventoryItemsGrouped.length > b.inventoryItemsGrouped.length ? -1 : 0;
                case SortType.QtyDesc: return a.inventoryItemsGrouped.length > b.inventoryItemsGrouped.length ? 1 :
                    a.inventoryItemsGrouped.length < b.inventoryItemsGrouped.length ? -1 : 0;
                case SortType.RarityAsc: return a.inventoryItemsGrouped[0].description.rarity.itemRarityTypeID < b.inventoryItemsGrouped[0].description.rarity.itemRarityTypeID ? 1 :
                    a.inventoryItemsGrouped[0].description.rarity.itemRarityTypeID > b.inventoryItemsGrouped[0].description.rarity.itemRarityTypeID ? -1 : 0;
                case SortType.RarityDesc: return a.inventoryItemsGrouped[0].description.rarity.itemRarityTypeID > b.inventoryItemsGrouped[0].description.rarity.itemRarityTypeID ? 1 :
                    a.inventoryItemsGrouped[0].description.rarity.itemRarityTypeID < b.inventoryItemsGrouped[0].description.rarity.itemRarityTypeID ? -1 : 0;
                case SortType.Type: a.inventoryItemsGrouped[0].description.itemClass.itemClassificationID > b.inventoryItemsGrouped[0].description.itemClass.itemClassificationID ? 1 :
                    a.inventoryItemsGrouped[0].description.itemClass.itemClassificationID < b.inventoryItemsGrouped[0].description.itemClass.itemClassificationID ? -1 : 0;
            }
            return 0;
        });
    }

    public searchItems(searchTerm: string) {
        this.fullInventoryStoredForSearch.push(...this.inventory);
        var self = this;
        //do some kind of loading icon TODO
        Application.searchInventory(searchTerm).done((results: InventoryGrouping[]) => { //todo resolve loading icon
            self.inventory.length = 0;
            self.inventory.push(...results);
        });
    }

    public dismissSearch() {
        this.inventory.length = 0;
        this.inventory.push(...this.fullInventoryStoredForSearch);
    }

}

enum SortType {
    NameAsc = 1,
    NameDesc,
    QtyAsc,
    QtyDesc,
    RarityAsc,
    RarityDesc,
    Type
}