package com.louiswebbgames.hyperbocalypse;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.louiswebbgames.hyperbocalypse.utility.Assets;

public class HyperbocalypseGame extends Game {
	
	@Override
	public void create () {
		Assets.instance.init(new AssetManager());
		setScreen(new PlayScreen());
	}

}
