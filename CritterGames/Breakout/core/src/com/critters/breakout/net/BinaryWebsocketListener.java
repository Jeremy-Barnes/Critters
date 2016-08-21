package com.critters.breakout.net;

public interface BinaryWebsocketListener extends WebsocketListener {

    void onMessage(byte[] bytes);
}
