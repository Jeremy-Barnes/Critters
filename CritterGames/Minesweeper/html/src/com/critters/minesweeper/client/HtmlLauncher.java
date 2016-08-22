package com.critters.minesweeper.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.critters.minesweeper.Game;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(32 * 8, 32 * 8);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Game();
        }
}