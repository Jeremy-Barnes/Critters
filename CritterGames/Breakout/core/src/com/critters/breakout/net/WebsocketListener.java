package com.critters.breakout.net;

public interface WebsocketListener {

    void onClose(CloseEvent event);

    void onMessage(String msg);

    void onOpen();
}
