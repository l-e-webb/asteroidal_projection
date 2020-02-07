package com.tangledwebgames.asteroidalprojection.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tangledwebgames.asteroidalprojection.AsteroidalProjectionGame;
import com.tangledwebgames.asteroidalprojection.utility.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Constants.UI_VIEWPORT_WIDTH;
		config.height = (int) Constants.UI_VIEWPORT_HEIGHT;
		config.resizable = true;
		new LwjglApplication(new AsteroidalProjectionGame(), config);
	}
}
