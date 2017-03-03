ALTER TABLE itemConfigs ADD COLUMN imagePath varchar(200) not null;
ALTER TABLE petSpeciesConfigs ADD COLUMN imgPathWithoutModifiers varchar(200) not null;
ALTER TABLE petSpeciesConfigs ADD COLUMN speciesDescription varchar(2000) not null;
ALTER TABLE messages ADD COLUMN showRecipient boolean not null;
ALTER TABLE messages ADD COLUMN showSender boolean not null;
ALTER TABLE petColorConfigs ADD COLUMN patternPath varchar(200) not null;
ALTER TABLE storeConfigs add column storeClerkImagePath varchar(200) not null;
ALTER TABLE storeConfigs add column storeBackgroundImagePath varchar(200) not null;
ALTER TABLE storeConfigs add column storeClerkImagePath varchar(200) not null;
ALTER TABLE users add column userImagePath varchar(200) not null;
ALTER TABLE users drop column birthdate;
ALTER TABLE users add column birthMonth;
ALTER TABLE users add column birthDay;

CREATE TABLE userImageOptions(
    userImageOptionID SERIAL NOT NULL PRIMARY KEY,
    imagePath VARCHAR(200) NOT NULL
);

CREATE TABLE storeClerkImageOptions(
    storeClerkImageOptionID SERIAL NOT NULL PRIMARY KEY,
    imagePath VARCHAR(200) NOT NULL
);

CREATE TABLE storeBackgroundImageOptions(
    storeClerkImageOptionID SERIAL NOT NULL PRIMARY KEY,
    imagePath VARCHAR(200) NOT NULL
);
