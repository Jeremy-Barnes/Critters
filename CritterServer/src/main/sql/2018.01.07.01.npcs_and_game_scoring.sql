CREATE TABLE npcs(
    npcID SERIAL NOT NULL PRIMARY KEY,
    name varchar(200) not null,
    description varchar(1000) null,
    imagePath varchar(200) not null,
    actionHandlerScript TEXT
);

ALTER TABLE gameThumbnailConfigs ADD COLUMN pointToCurrencyFactor int;
ALTER TABLE gameThumbnailConfigs ADD COLUMN scoreHandlerScript TEXT;


CREATE TABLE storyQuestStepConfigs(
    storyQuestStepID SERIAL NOT NULL PRIMARY KEY,
    rootStepID INT NOT NULL REFERENCES storyQuestStepConfigs(storyQuestStepID),
    previousStepID INT NOT NULL REFERENCES storyQuestStepConfigs(storyQuestStepID),
    nextStepID INT NULL REFERENCES storyQuestStepConfigs(storyQuestStepID),
    giverNPC INT NULL REFERENCES npcs(npcID),
    redeemerNPC INT NULL REFERENCES npcs(npcID),
    canRandom BIT NOT NULL,
    canVolunteer BIT NOT NULL,
    isActive BIT NOT NULL,
    actionHandlerScript TEXT,
    timeLimitSeconds INT NULL 
);

CREATE TABLE userStoryQuestInstances(
    userQuestInstanceID SERIAL NOT NULL PRIMARY KEY,
    questUserID INT NOT NULL REFERENCES users(userID),
    currentQuestStepID INT NOT NULL REFERENCES storyQuestStepConfigs(storyQuestStepID),
    startedDate TIMESTAMP NOT NULL,
    completedDate TIMESTAMP NOT NULL
);

CREATE TABLE npcItemQuestPreferenceConfigs(
    npcItemQuestPreferenceConfigID SERIAL NOT NULL PRIMARY KEY,
    wanterNPC INT NOT NULL REFERENCES npcs(npcID),
    itemID INT NOT NULL REFERENCES itemConfigs(itemConfigID),
    critterBuxxValuePerItem INT NOT NULL
);

