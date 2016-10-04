package com.critters.breakout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.critters.breakout.graphics.SpriteLoader;
import com.critters.breakout.net.CloseEvent;
import com.critters.breakout.net.Websocket;
import com.critters.breakout.net.WebsocketListener;

public class LevelWebsocket {

	private int x = 0;
	private String message = "";
	private Websocket socket;
	private boolean connected;
	private int time;

	public LevelWebsocket() {
		// Websocket socket = new Websocket("ws://hostname:port/path");

		socket = new Websocket("ws://f999c0d7.ngrok.io/api/session/25");

		socket.addListener(new WebsocketListener() {

			@Override
			public void onClose(CloseEvent event) {
				System.out.println("Socket closed");
				x = 100;
			}

			@Override
			public void onMessage(String msg) {
				System.out.println("Message received: " + msg);
				x = 200;
				message = msg;
			}

			@Override
			public void onOpen() {
				System.out.println("Socket opened");
				x = 50;
				connected = true;
			}
		});

		System.out.println("test");

		socket.open();
	}

	/**
	 * Receive and process the input
	 */
	public void update() {
		if (connected && time++ % 60 == 0) {
			String text = "Hello, the client time is " + time;
			socket.send(text);
		}
	}

	/**
	 * Render the level.
	 */
	public void render(SpriteBatch render) {
		render.draw(SpriteLoader.getBomb(), x, 50, 50, 50);

		BitmapFont font = new BitmapFont();
		font.setColor(Color.BLACK);
		font.draw(render, message, 10, 30);

		System.out.println("Testing");
	}

}
