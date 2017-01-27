CREATE TABLE storeConfigs(
    storeConfigID SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(24) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    ownerID INT NULL REFERENCES users(userID)
);

CREATE TABLE storePurchaseLogs (
    storeLogID SERIAL NOT NULL PRIMARY KEY,
    sellerStoreID INT NOT NULL REFERENCES storeConfigs(storeConfigID),
    purchaserID INT NOT NULL REFERENCES users(userID),
    purchasePrice int NOT NULL,
    soldItemID INT NOT NULL REFERENCES inventoryItems(inventoryItemID)
);

ALTER TABLE inventoryItems ADD COLUMN price int null;
ALTER TABLE inventoryItems ALTER COLUMN ownerId DROP NOT NULL;