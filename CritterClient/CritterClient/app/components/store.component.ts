import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User, Notification, Item, ItemDescription, InventoryGrouping, Store } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/store.template.htm"
})

export class StoreComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();

    /**** View State Controllers ***/
    viewStore: Store = new Store();
    inventory: InventoryGrouping[] = [];
    activeItem: InventoryGrouping;
    selectedQuantity: number = 0;

    bulkSelect: boolean = false;

    searchText: string = "";

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
        var self = this;
        this.route.params.forEach((params: Params) => {
            let id = parseFloat(params['id']);
            Application.getStore(id).done((store: Store) => {
                self.viewStore = store;
                self.inventory = store.storeStock;
            });
        });
        this.user = this.app.user;
    }

    ngOnInit() {
    }

    selectedQuantityChange(value: number) {
        this.selectedQuantity = value;
    }

    viewItem(viewItem: InventoryGrouping) {
        this.actionCompleted = false;
        this.activeItem = viewItem;
        this.selectedQuantity = 0;

        (<any>$("#viewItemDetail")).modal('show'); //I'm not happy about this either.
    }

    increment() {
        if (this.selectedQuantity < this.activeItem.inventoryItemsGrouped.length)
        this.selectedQuantity = this.selectedQuantity + 1;
    }

    decrement() {
        if (this.selectedQuantity > 0)
        this.selectedQuantity = this.selectedQuantity - 1;
    }

    buy() {
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
           // promise = Application.buyItems(items, this.activeItem);
            this.statusText = this.selectedQuantity + " " + items[0].description.itemName + (this.selectedQuantity == 1 ? " " : "s ") + "discarded!";
            
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
        Application.searchStore(searchTerm).done((results: InventoryGrouping[]) => { //todo resolve loading icon
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