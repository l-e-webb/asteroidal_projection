package com.louiswebbgames.asteroidalprojection.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.louiswebbgames.asteroidalprojection.AsteroidalProjectionGame;
import com.louiswebbgames.asteroidalprojection.utility.Constants;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(
                        Constants.HTML_EMBED_WIDTH,
                        Constants.HTML_EMBED_HEIGHT
                );
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new AsteroidalProjectionGame();
        }
}