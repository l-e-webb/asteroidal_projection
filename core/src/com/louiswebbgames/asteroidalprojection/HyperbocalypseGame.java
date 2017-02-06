package com.louiswebbgames.asteroidalprojection;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

public class HyperbocalypseGame extends Game {
	
	@Override
	public void create () {
		Assets.instance.init(new AssetManager());
		setScreen(new PlayScreen());
	}

}
