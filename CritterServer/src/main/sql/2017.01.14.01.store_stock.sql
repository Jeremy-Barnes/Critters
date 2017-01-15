ALTER TABLE inventoryItems ADD COLUMN containingStoreId int null REFERENCES storeConfigs(storeConfigID);
