package com.tangledwebgames.asteroidalprojection.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.tangledwebgames.asteroidalprojection.AsteroidalProjectionGame;
import com.tangledwebgames.asteroidalprojection.utility.Constants;

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