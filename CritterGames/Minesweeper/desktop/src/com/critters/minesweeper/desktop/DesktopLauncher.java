package com.critters.minesweeper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.critters.minesweeper.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 32 * 8;
		config.height = 32 * 8;		
		new LwjglApplication(new Game(), config);
	}
}
