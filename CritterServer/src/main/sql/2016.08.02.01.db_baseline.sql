CREATE TABLE users(
    userID SERIAL NOT NULL PRIMARY KEY,
    userName VARCHAR(24) NOT NULL,
    firstName VARCHAR(24),
    lastName VARCHAR(24),
    emailAddress VARCHAR(100) NOT NULL,
    password VARCHAR NOT NULL,
    sex VARCHAR(8) NOT NULL,
    birthdate DATE NOT NULL,
    salt VARCHAR NOT NULL,
    city VARCHAR(50),
    state VARCHAR(50),
    country VARCHAR(50),
    postcode VARCHAR(20),
    tokenSelector VARCHAR,
    tokenValidator VARCHAR,
    critterbuxx INT NOT NULL,
    isActive boolean not null default 't',
    CONSTRAINT uk_username UNIQUE (userName),
    CONSTRAINT uk_email UNIQUE (emailAddress),
    CHECK (sex IN ('male','female','other'))
);

CREATE TABLE petSpeciesConfigs(
    petSpeciesConfigID SERIAL NOT NULL PRIMARY KEY,
    petTypeName VARCHAR(24) NOT NULL
);

CREATE TABLE petColorConfigs(
    petColorConfigID SERIAL NOT NULL PRIMARY KEY,
    petColorName VARCHAR(24) NOT NULL
);

CREATE TABLE pets(
    petID SERIAL NOT NULL PRIMARY KEY,
    petName VARCHAR(24) NOT NULL,
    sex VARCHAR(8) NOT NULL,
    colorID INT NOT NULL REFERENCES petColorConfigs(petColorConfigID),
    ownerID INT NOT NULL REFERENCES users(userID),
    speciesID INT NOT NULL REFERENCES petSpeciesConfigs(petSpeciesConfigID),
    isAbandoned boolean not null default 'f',
    CHECK (sex IN ('male','female','other'))
);

CREATE TABLE itemConfigs(
    itemConfigID SERIAL NOT NULL PRIMARY KEY,
    itemName VARCHAR(24) NOT NULL,
    itemDescription VARCHAR(1000) NOT NULL
);

CREATE TABLE inventoryItems(
    inventoryItemID SERIAL NOT NULL PRIMARY KEY,
    itemTypeID INT NOT NULL REFERENCES itemConfigs(itemConfigID),
    ownerID INT NOT NULL REFERENCES users(userID)
);

CREATE TABLE friendships(
    friendshipID SERIAL NOT NULL PRIMARY KEY,
    requesterUserID INT NOT NULL REFERENCES users(userID),
    requestedUserID INT NOT NULL REFERENCES users(userID),
    accepted BOOLEAN NOT NULL,
    dateSent DATE NOT NULL,
    CONSTRAINT friendshipLink UNIQUE (requesterUserID, requestedUserID)
);

CREATE TABLE messages(
    messageID SERIAL NOT NULL PRIMARY KEY,
    senderUserID INT NOT NULL REFERENCES users(userID),
    recepientUserID INT NOT NULL REFERENCES users(userID),
    read BOOLEAN NOT NULL,
    dateSent DATE NOT NULL,
    messageText TEXT,
    messageSubject VARCHAR(140)
);
