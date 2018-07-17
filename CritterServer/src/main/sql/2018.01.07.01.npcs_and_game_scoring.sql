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
    rootStepID INT NULL REFERENCES storyQuestStepConfigs(storyQuestStepID),
    previousStepID INT NULL REFERENCES storyQuestStepConfigs(storyQuestStepID),
    nextStepID INT NULL REFERENCES storyQuestStepConfigs(storyQuestStepID),
    giverNPC INT NULL REFERENCES npcs(npcID),
    redeemerNPC INT NULL REFERENCES npcs(npcID),
    canRandom BIT NOT NULL,
    canVolunteer BIT NOT NULL,
    isActive BIT NOT NULL,
    questTitle varchar(300) NOT NULL,
    actionHandlerScript TEXT,
    timeLimitSeconds INT NULL 
);

CREATE TABLE userQuestInstances(
    userQuestInstanceID SERIAL NOT NULL PRIMARY KEY,
    questUserID INT NOT NULL REFERENCES users(userID),
    currentQuestStepID INT NULL REFERENCES storyQuestStepConfigs(storyQuestStepID),
    startedDate TIMESTAMP NOT NULL,
    completedDate TIMESTAMP NULL,
    questText TEXT NOT NULL,
    questTitle varchar(300) NOT NULL,
    questImage varchar(200) NULL,
    randomQuestGiverRedeemer INT NULL REFERENCES npcs(npcID),
    randomQuestObjectivesJSONObject TEXT,
    CHECK (currentQuestStepID IS NOT NULL OR (randomQuestGiverRedeemer IS NOT NULL AND randomQuestObjectivesJSONObject IS NOT NULL))
);

CREATE TABLE npcItemQuestPreferenceConfigs(
    npcItemQuestPreferenceConfigID SERIAL NOT NULL PRIMARY KEY,
    wanterNPC INT NOT NULL REFERENCES npcs(npcID),
    itemConfigID INT NOT NULL REFERENCES itemConfigs(itemConfigID),
    critterBuxxValuePerItem INT NOT NULL
);

CREATE TABLE npcQuestResponseConfigs (
    npcQuestResponseConfigID SERIAL NOT NULL PRIMARY KEY,
    respondingNPCID INT NOT NULL REFERENCES npcs(npcID),
    response TEXT NOT NULL,
    worksForFetchQuests BIT NOT NULL,
    isSuccessResponse BIT NOT NULL
)

ALTER TABLE inventoryItems add COLUMN npcOwnerID INT NULL REFERENCES npcs(npcID);
ALTER TABLE inventoryItems add CHECK ((npcOwnerID IS NOT NULL AND ownerID IS NULL) OR (npcOwnerID IS NULL AND ownerID IS NOT NULL) OR (npcOwnerID IS NULL AND ownerID IS NULL));

CREATE TABLE npcQuestRewardConfigs (
    npcQuestRewardConfigID SERIAL NOT NULL PRIMARY KEY,
    rewardingNPCID INT NOT NULL REFERENCES npcs(npcID),
    rewardItemConfigID INT NOT NULL REFERENCES itemConfigs(itemConfigID),
    rarityMatch BIT NOT NULL,
    rarityMatchFactor INT NULL,
    percentOdds FLOAT NULL,
    specialMessage TEXT NULL,
    CHECK ((rarityMatch = true AND rarityMatchFactor IS NOT NULL) OR (rarityMatch = false AND percentOdds IS NOT NULL))
)