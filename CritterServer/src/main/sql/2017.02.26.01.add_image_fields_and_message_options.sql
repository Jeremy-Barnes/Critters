ALTER TABLE itemConfigs ADD COLUMN imagePath varchar(200) not null;
ALTER TABLE petSpeciesConfigs ADD COLUMN imagePathWithoutModifiers varchar(200) not null;
ALTER TABLE petSpeciesConfigs ADD COLUMN speciesDescription varchar(2000) not null;
ALTER TABLE messages ADD COLUMN showRecipient boolean not null;
ALTER TABLE messages ADD COLUMN showSender boolean not null;
ALTER TABLE petColorConfigs ADD COLUMN patternPath varchar(200) not null;
ALTER TABLE storeConfigs add column storeClerkImagePath varchar(200) not null;
ALTER TABLE storeConfigs add column storeBackgroundImagePath varchar(200) not null;
ALTER TABLE users add column userImagePath varchar(200) not null;
ALTER TABLE users drop column birthdate;
ALTER TABLE users add column birthMonth int not null;
ALTER TABLE users add column birthDay int not null;
ALTER TABLE users add constraint monthCheck CHECK (birthMonth <= 12 AND birthMonth >= 1)
ALTER TABLE users add constraint dayCheck CHECK (birthDay <= 31 AND birthDay >= 1)

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
