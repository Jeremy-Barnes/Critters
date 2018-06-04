CREATE TABLE npcs(
    npcID SERIAL NOT NULL PRIMARY KEY,
    name varchar(200) not null,
    description varchar(1000) null,
    imagePath varchar(200) not null,
    actionHandlerScript TEXT
);

ALTER TABLE gameThumbnailConfigs ADD COLUMN pointToCurrencyFactor int;
ALTER TABLE gameThumbnailConfigs ADD COLUMN scoreHandlerScript TEXT;

CREATE TABLE questLines(
    questID SERIAL NOT NULL PRIMARY KEY,
    startStep INT NOT NULL REFERENCES questStep(questStepID),
);

CREATE TABLE questSteps(
    questStepID SERIAL NOT NULL PRIMARY KEY,
    previousStep INT NOT NULL REFERENCES questStep(questStepID),
    nextStep INT NOT NULL REFERENCES questStep(questStepID),
    giver INT NOT NULL REFERENCES npcs(npcID),
    actionHandlerScript TEXT
);

CREATE TABLE userQuestStepInstance(
    userQuestStepInstanceID SERIAL NOT NULL PRIMARY KEY,
    questUserID INT NOT NULL REFERENCES users(questStepID),
    currentQuestStepID INT NOT NULL REFERENCES questSteps(questStepID),
    startedDate DATE NOT NULL
);