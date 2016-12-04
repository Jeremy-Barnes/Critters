ALTER TABLE messages ALTER COLUMN dateSent TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE messages ADD COLUMN rootMessage int null REFERENCES messages(messageID);
ALTER TABLE messages ADD COLUMN parentMessage int null REFERENCES messages(messageID);