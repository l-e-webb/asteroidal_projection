package com.louiswebbgames.asteroidalprojection;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.louiswebbgames.asteroidalprojection.ui.UiConstants;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.SoundManager;

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
