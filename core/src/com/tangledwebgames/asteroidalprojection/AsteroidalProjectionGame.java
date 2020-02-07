package com.tangledwebgames.asteroidalprojection;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.tangledwebgames.asteroidalprojection.ui.UiConstants;
import com.tangledwebgames.asteroidalprojection.utility.Assets;
import com.tangledwebgames.asteroidalprojection.utility.SoundManager;

public class AsteroidalProjectionGame extends Game {
	
	@Override
	public void create () {
		Assets.instance.init(new AssetManager());
		UiConstants.init();
		setScreen(new PlayScreen());
		SoundManager.loadAudio();
	}

	@Override
	public void pause() {
		super.pause();
		SoundManager.disposeAudio();
	}

	@Override
	public void resume() {
		super.resume();
		SoundManager.loadAudio();
	}
}
