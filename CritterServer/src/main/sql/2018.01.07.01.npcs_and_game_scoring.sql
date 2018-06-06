CREATE TABLE npcs(
    npcID SERIAL NOT NULL PRIMARY KEY,
    name varchar(200) not null,
    description varchar(1000) null,
    imagePath varchar(200) not null,
    actionHandlerScript TEXT
);

ALTER TABLE gameThumbnailConfigs ADD COLUMN pointToCurrencyFactor int;
ALTER TABLE gameThumbnailConfigs ADD COLUMN scoreHandlerScript TEXT;


CREATE TABLE questStepConfigs(
    questStepID SERIAL NOT NULL PRIMARY KEY,
    previousStepID INT NOT NULL REFERENCES questStep(questStepID),
    nextStepID INT NOT NULL REFERENCES questStep(questStepID),
    giverNPC INT NULL REFERENCES npcs(npcID),
    canRandom BIT NOT NULL,
    canVolunteer BIT NOT NULL,
    isActive BIT NOT NULL,
    actionHandlerScript TEXT
);

CREATE TABLE userQuestInstance(
    userQuestInstanceID SERIAL NOT NULL PRIMARY KEY,
    questUserID INT NOT NULL REFERENCES users(questStepID),
    currentQuestStepID INT NOT NULL REFERENCES questSteps(questStepID),
    startedDate DATE NOT NULL,
    completedDate DATE NOT NULL
);
