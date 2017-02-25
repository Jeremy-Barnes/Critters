CREATE TABLE itemRarityTypes(
    itemRarityTypeID SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(48)
);

INSERT INTO itemRarityTypes (name)
VALUES
 ('Boring'),
 ('Common'),
 ('Uncommon'),
 ('Interesting'),
 ('Surprising'),
 ('Rare'),
 ('Amazing'),
 ('Incredible'),
 ('Legendary'),
 ('There is Literally Only One');

CREATE TABLE itemClassifications(
    itemClassificationID SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(48) NOT NULL
);

INSERT INTO itemClassifications (name)
VALUES
 ('Food'),
 ('Toy'),
 ('Junk'),
 ('Weapon');

ALTER TABLE itemConfigs ADD COLUMN itemClass int not null REFERENCES itemClassifications(itemClassificationID);
ALTER TABLE itemConfigs ADD COLUMN rarity int not null REFERENCES itemRarityTypes(itemRarityTypeID);

CREATE TABLE npcStoreRestockConfigs(
    npcStoreStockConfigID SERIAL NOT NULL PRIMARY KEY,
    percentOdds decimal NOT NULL,
    maxQuantityToAdd int NOT NULL,
    maxTotalQuantity int NOT NULL,
    specificClass INT NULL REFERENCES itemClassifications(itemClassificationID),
    rarityFloor INT NULL REFERENCES itemRarityTypes(itemRarityTypeID),
    rarityCeiling INT NULL REFERENCES itemRarityTypes(itemRarityTypeID),
    specificRarity INT NULL REFERENCES itemRarityTypes(itemRarityTypeID),
    specificItem INT NULL REFERENCES itemConfigs(itemConfigID),
    storeID INT NOT NULL REFERENCES storeConfigs(storeConfigID)
);

CREATE TABLE gameThumbnailConfigs(
    gameThumbnailConfigID SERIAL NOT NULL PRIMARY KEY,
    gameName VARCHAR(48) NOT NULL,
    gameDescription VARCHAR(1000) NOT NULL,
    gameIconPath VARCHAR(100) NOT NULL,
    gameURL VARCHAR(100) NOT NULL
);




CREATE OR REPLACE FUNCTION restockRandomly(maxRarity int, minRarity int, returnQuantity int, specificClass int, specificItemType int)
    RETURNS setof itemConfigs AS
    $$
    BEGIN

    IF maxRarity < 0 then
    maxRarity := null;
    END IF;

    IF minRarity < 0 then
    minRarity := null;
    END IF;

    IF specificClass < 0 then
    specificClass := null;
    END IF;

    IF specificItemType < 0 then
    specificItemType := null;
    END IF;

    return query select *
    from itemConfigs where coalesce(maxRarity, rarity) >= rarity and coalesce(minRarity,rarity) <= rarity and coalesce(specificClass, itemClass) = itemClass and coalesce(specificItemType, itemConfigID) = itemConfigID
    order by random()
    limit  returnQuantity;
    return;
    END
    $$
    LANGUAGE plpgsql;