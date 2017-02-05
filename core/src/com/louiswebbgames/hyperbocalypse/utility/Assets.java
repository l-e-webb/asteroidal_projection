package com.louiswebbgames.hyperbocalypse.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Singleton for loading and managing assets.
 */
public class Assets implements Disposable, AssetErrorListener  {

    public static final String LOG_TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    public TextureRegion triangle;
    public TextureRegion shot;
    public TextureRegion asteroid;
    public TextureRegion playerLaser;

    private AssetManager assetManager;

    private Assets() {}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.ATLAS_PATH, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.ATLAS_PATH);
        triangle = atlas.findRegion("triangle");
        shot = atlas.findRegion("laser");
        asteroid = atlas.findRegion("octagon_sun");
        playerLaser = atlas.findRegion("laser");
    }

    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(LOG_TAG, "Unable to load asset " + asset.fileName, throwable);
    }

    public void dispose() {
        assetManager.dispose();
    }



}
