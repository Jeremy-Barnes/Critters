CREATE TABLE user(
    userID SERIAL NOT NULL PRIMARY KEY,
    userName VARCHAR(24) NOT NULL,
    firstName VARCHAR(24),
    lastName VARCHAR(24),
    emailAddress VARCHAR(100) NOT NULL,
    password VARCHAR NOT NULL,
    birthdate DATE NOT NULL,
    salt VARCHAR NOT NULL,
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50),
    postcode VARCHAR(20),
    tokenSelector VARCHAR,
    tokenValidator VARCHAR,
    critterbuxx INT NOT NULL,
    CONSTRAINT uk_email UNIQUE (userName),
    CONSTRAINT uk_email UNIQUE (emailAddress)
);

CREATE TABLE petSpeciesConfig(
    petSpeciesConfigID SERIAL NOT NULL PRIMARY KEY,
    petTypeName VARCHAR(24) NOT NULL
);

CREATE TABLE petColorConfig(
    petsColorConfigID SERIAL NOT NULL PRIMARY KEY,
    petColorName VARCHAR(24) NOT NULL
);

CREATE TABLE pet(
    petID SERIAL NOT NULL PRIMARY KEY,
    petName VARCHAR(24) NOT NULL,
    sex BOOLEAN NULL,
    colorID INT NOT NULL REFERENCES petsColorConfig(petsColorConfigID),
    ownerID INT NOT NULL REFERENCES users(userID),
    speciesID INT NOT NULL REFERENCES petSpeciesConfig(petSpeciesConfigID)
);

CREATE TABLE itemConfig(
    itemConfigID SERIAL NOT NULL PRIMARY KEY,
    itemName VARCHAR(24) NOT NULL,
    itemDescription VARCHAR(1000) NOT NULL
);

CREATE TABLE inventoryItems(
    inventoryItemID SERIAL NOT NULL PRIMARY KEY,
    itemTypeID INT NOT NULL REFERENCES itemConfig(itemConfigID),
    ownerID INT NOT NULL REFERENCES user(userID)
);

CREATE TABLE friendships(
    friendshipID SERIAL NOT NULL PRIMARY KEY,
    requesterUserID INT NOT NULL REFERENCES user(userID),
    requestedUserID INT NOT NULL REFERENCES user(userID),
    accepted BOOLEAN NOT NULL,
    dateSent DATE NOT NULL,
    CONSTRAINT friendshipLink UNIQUE (requesterUserID, requestedUserID)
);

CREATE TABLE message(
    messageID SERIAL NOT NULL PRIMARY KEY,
    senderUserID INT NOT NULL REFERENCES user(userID),
    recepientUserID INT NOT NULL REFERENCES user(userID),
    read BOOLEAN NOT NULL,
    dateSent DATE NOT NULL,
    messageText TEXT,
    messageSubject VARCHAR(140)
);

-- //pets
-- //inventory
-- //game scores
-- //friends
-- //messages