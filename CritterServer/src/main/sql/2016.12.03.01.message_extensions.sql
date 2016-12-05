ALTER TABLE messages ALTER COLUMN dateSent TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE messages ADD COLUMN rootMessageId int null REFERENCES messages(messageID);
ALTER TABLE messages ADD COLUMN parentMessageId int null REFERENCES messages(messageID);