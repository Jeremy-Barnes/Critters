import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { User, Notification, Item, ItemDescription, InventoryGrouping, Store, StoreClerkImageOption, StoreBackgroundImageOption } from '../dtos'
import {Application} from "../appservice"

@Component({
    templateUrl: "../../templates/storeconfig.template.htm"
})

export class StoreConfigComponent implements OnInit {
    user: User;
    app: Application = Application.getApp();

    /**** View State Controllers ***/
    viewStore: Store = new Store();
    inventory: InventoryGrouping[] = [];
    activeItem: InventoryGrouping;
    selectedQuantity: number = 0;

    clerkOptions: StoreClerkImageOption[];
    backgroundOptions: StoreBackgroundImageOption[];

    searchText: string = "";

    actionCompleted: boolean = false;
    statusText: string = "";

    constructor(private route: ActivatedRoute) {
        var self = this;
        this.user = this.app.user;
        if (this.app.loggedIn)
            this.route.params.forEach((params: Params) => {
                if (params['id']) {
                    let id = parseFloat(params['id']);
                    Application.getStore(id).done((store: Store) => {
                        self.viewStore = store;
                        self.inventory = store.storeStock;
                    });
                } else {
                    self.viewStore = new Store();
                    self.inventory = [];
                }
                Application.getClerkImageOptions().done((images: StoreClerkImageOption[]) => {
                    self.clerkOptions = images;
                });
                Application.getBackgroundImageOptions().done((images: StoreBackgroundImageOption[]) => {
                    self.backgroundOptions = images;
                });
            });
    }

    ngOnInit() { }

    oldNameForRevert: string;
    editingTitle: boolean = false;
    editTitle() {
        this.oldNameForRevert = this.viewStore.name;
        this.editingTitle = true;
    }
    saveTitle() {
        this.editingTitle = false;
        this.oldNameForRevert = null;
    }
    revertTitle() {
        this.editingTitle = false;
        this.viewStore.name = this.oldNameForRevert;
        this.oldNameForRevert = null;
    }

    oldDescriptionForRevert: string;
    editingDescription: boolean = false;
    editDescription() {
        this.oldDescriptionForRevert = this.viewStore.description;
        this.editingDescription = true;
    }
    saveDescription() {
        this.editingDescription = false;
        this.oldDescriptionForRevert = null;
    }
    revertDescription() {
        this.editingDescription = false;
        this.viewStore.description = this.oldDescriptionForRevert;
        this.oldDescriptionForRevert = null;
    }

    selectedQuantityRemove(value: number) {
        this.selectedQuantity = value;
    }

    viewItem(viewItem: InventoryGrouping) {
        this.actionCompleted = false;
        this.activeItem = viewItem;
        this.selectedQuantity = 0;

        (<any>$("#viewItemDetail")).modal('show'); //I'm not happy about this either.
    }

    submitRemoveItem() {
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
            var promise = Application.moveItemsFromStore(items, this.activeItem);
            this.statusText = this.selectedQuantity + " " + items[0].description.itemName + (this.selectedQuantity == 1 ? " " : "s ") + "moved to your inventory!";
            
            var self = this;
            promise.done(() => {
                self.actionCompleted = true;
            });
        }
    }

    public images: any[] = [];
    changeImage: boolean = false;
    public changeStoreClerk: boolean = false;
    public changeBackground: boolean = false;
    editImage(backgroundNotClerk: boolean) {
        this.changeImage = true;
        this.changeBackground = backgroundNotClerk;
        this.changeStoreClerk = !backgroundNotClerk;
        if (backgroundNotClerk) {
            this.images = this.backgroundOptions;
        } else {
            this.images = this.clerkOptions;
        }
        (<any>$("#viewImages")).modal('show'); //I'm not happy about this either.
    }

    public selectedImagePath: string = "";
    public selectedClerk: StoreClerkImageOption;
    public selectedBackground: StoreBackgroundImageOption;
    selectImage(image: any) {
        this.selectedImagePath = image.imagePath;
        if (this.changeStoreClerk) {
            this.viewStore.storeClerkImagePath = image.imagePath
            this.selectedClerk = image;
        } else {
            this.viewStore.storeBackgroundImagePath = image.imagePath
            this.selectedBackground = image;
        }
        (<any>$("#viewImages")).modal('hide'); //I'm not happy about this either.
        this.changeStoreClerk = false;
        this.changeImage = false;
        this.changeBackground = false;
    }

    saveChanges() {
        if (this.selectedBackground != null && this.selectedClerk != null && this.viewStore.name.length > 0) {
            if (this.viewStore.storeConfigID > 0) {
                Application.editStore(this.viewStore, this.selectedBackground, this.selectedClerk);
            } else {
                Application.createStore(this.viewStore, this.selectedBackground, this.selectedClerk);
            }
        } else {
            alert("choose some options for your stuff, jerk");
        }
    }
}