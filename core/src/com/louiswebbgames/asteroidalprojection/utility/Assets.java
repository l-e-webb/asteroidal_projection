package com.louiswebbgames.asteroidalprojection.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;

/**
 * Singleton for loading and managing assets.
 */
public class Assets implements Disposable, AssetErrorListener  {

    public static final String LOG_TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    public TextureRegion player;
    public TextureRegion asteroid;
    public TextureRegion playerLaser;
    public TextureRegion enemyLaser;
    public TextureRegion seekerEnemy;
    public TextureRegion sniperEnemy;
    public TextureRegion flyByEnemy;
    public Animation<TextureRegion> explosion;

    private AssetManager assetManager;

    private Assets() {}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.ATLAS_PATH, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.ATLAS_PATH);
        player = atlas.findRegion(Constants.PLAYER_REGION);
        asteroid = atlas.findRegion(Constants.ASTEROID_REGION);
        playerLaser = atlas.findRegion(Constants.PLAYER_LASER_REGION);
        enemyLaser = atlas.findRegion(Constants.ENEMY_LASER_REGION);
        explosion = new Animation<>(
                GameplayConstants.EXPLOSION_FRAME_DURATION,
                atlas.findRegions(Constants.EXPLOSION_REGIONS),
                Animation.PlayMode.NORMAL
        );
        seekerEnemy = atlas.findRegion(Constants.ENEMY_REGION);
        sniperEnemy = seekerEnemy;
        flyByEnemy = seekerEnemy;
    }

    public void error(AssetDescriptor asset, Throwable throwable) {
        Log.log(LOG_TAG, "Unable to load asset " + asset.fileName);
    }

    public void dispose() {
        assetManager.dispose();
    }



}
