package com.critters.snake.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.critters.snake.Game;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(800, 450);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Game();
        }
}