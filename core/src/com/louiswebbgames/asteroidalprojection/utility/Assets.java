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
    public TextureRegion playerPiercingLaser;
    public TextureRegion playerRoundLaser;
    public TextureRegion enemyLaser;
    public TextureRegion enemyPiercingLaser;
    public TextureRegion enemyRoundLaser;
    public TextureRegion seekerEnemy;
    public TextureRegion sniperEnemy;
    public TextureRegion flyByEnemy;
    public TextureRegion extraHealthPowerup;
    public TextureRegion missileAmmoPowerup;
    public TextureRegion piercingLaserPowerup;
    public TextureRegion pointsPowerup;
    public TextureRegion tripleLaserPowerup;
    public Animation<TextureRegion> explosion;
    public Animation<TextureRegion> missile;

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
        playerPiercingLaser = atlas.findRegion(Constants.PLAYER_PIERCING_LASER_REGION);
        playerRoundLaser = atlas.findRegion(Constants.PLAYER_ROUND_LASER_REGION);
        enemyLaser = atlas.findRegion(Constants.ENEMY_LASER_REGION);
        enemyPiercingLaser = atlas.findRegion(Constants.ENEMY_PIERCING_LASER_REGION);
        enemyRoundLaser = atlas.findRegion(Constants.ENEMY_ROUND_LASER_REGION);
        extraHealthPowerup = atlas.findRegion(Constants.EXTRA_HEALTH_REGION);
        missileAmmoPowerup = atlas.findRegion(Constants.MISSILE_AMMO_REGION);
        piercingLaserPowerup = atlas.findRegion(Constants.PIERCING_LASER_REGION);
        pointsPowerup = atlas.findRegion(Constants.POINTS_REGION);
        tripleLaserPowerup = atlas.findRegion(Constants.TRIPLE_LASER_REGION);
        explosion = new Animation<>(
                GameplayConstants.EXPLOSION_FRAME_DURATION,
                atlas.findRegions(Constants.EXPLOSION_REGIONS),
                Animation.PlayMode.NORMAL
        );
        missile = new Animation<>(
                GameplayConstants.MISSILE_FRAME_DURATION,
                atlas.findRegions(Constants.MISSILE_REGIONS),
                Animation.PlayMode.LOOP
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
