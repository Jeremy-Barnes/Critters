CREATE TABLE npcs(
    npcID SERIAL NOT NULL PRIMARY KEY,
    name varchar(200) not null,
    description varchar(1000) null,
    imagePath varchar(200) not null,
    actionHandlerScript TEXT
);