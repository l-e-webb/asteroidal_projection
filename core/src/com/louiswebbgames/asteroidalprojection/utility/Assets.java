package com.louiswebbgames.asteroidalprojection.utility;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.ui.UiConstants;

/**
 * Singleton for loading and managing assets.
 */
public class Assets implements Disposable, AssetErrorListener  {

    public static final String LOG_TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    public Animation<TextureRegion> player;
    public TextureRegion asteroid;
    public TextureRegion playerLaser;
    public TextureRegion playerPiercingLaser;
    public TextureRegion playerRoundLaser;
    public TextureRegion enemyLaser;
    public TextureRegion enemyPiercingLaser;
    public TextureRegion enemyRoundLaser;
    public Animation<TextureRegion> seekerEnemy;
    public Animation<TextureRegion> sniperEnemy;
    public Animation<TextureRegion> flyByEnemy;
    public Animation<TextureRegion> cruiserEnemy;
    public TextureRegion simpleCannon;
    public TextureRegion holeCannon;
    public TextureRegion extraHealthPowerup;
    public TextureRegion missileAmmoPowerup;
    public TextureRegion piercingLaserPowerup;
    public TextureRegion pointsPowerup;
    public TextureRegion tripleLaserPowerup;
    public Animation<TextureRegion> explosion;
    public Animation<TextureRegion> missile;
    public Drawable missileAmmoDrawable;
    public Drawable tripleLaserDrawable;
    public Drawable piercingLaserDrawable;
    public Drawable squareButtonDark;
    public Drawable squareButtonLight;
    public Drawable checkboxUnchecked;
    public Drawable checkboxChecked;
    public Drawable sliderKnob;
    public Drawable slider;

    private AssetManager assetManager;

    private Assets() {}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.ATLAS_PATH, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.ATLAS_PATH);
        player = new Animation<>(
                Constants.PLAYER_FRAME_DURATION,
                atlas.findRegions(Constants.PLAYER_REGION),
                Animation.PlayMode.LOOP_PINGPONG
        );
        asteroid = atlas.findRegion(Constants.ASTEROID_REGION);
        playerLaser = atlas.findRegion(Constants.PLAYER_LASER_REGION);
        playerPiercingLaser = atlas.findRegion(Constants.PLAYER_PIERCING_LASER_REGION);
        playerRoundLaser = atlas.findRegion(Constants.PLAYER_ROUND_LASER_REGION);
        enemyLaser = atlas.findRegion(Constants.ENEMY_LASER_REGION);
        enemyPiercingLaser = atlas.findRegion(Constants.ENEMY_PIERCING_LASER_REGION);
        enemyRoundLaser = atlas.findRegion(Constants.ENEMY_ROUND_LASER_REGION);
        extraHealthPowerup = atlas.findRegion(Constants.EXTRA_HEALTH_REGION);
        missileAmmoPowerup = atlas.findRegion(Constants.MISSILE_AMMO_REGION);
        missileAmmoDrawable = new TextureRegionDrawable(missileAmmoPowerup);
        piercingLaserPowerup = atlas.findRegion(Constants.PIERCING_LASER_REGION);
        piercingLaserDrawable = new TextureRegionDrawable(piercingLaserPowerup);
        pointsPowerup = atlas.findRegion(Constants.POINTS_REGION);
        tripleLaserPowerup = atlas.findRegion(Constants.TRIPLE_LASER_REGION);
        tripleLaserDrawable = new TextureRegionDrawable(tripleLaserPowerup);
        explosion = new Animation<>(
                GameplayConstants.EXPLOSION_FRAME_DURATION,
                atlas.findRegions(Constants.EXPLOSION_REGIONS),
                Animation.PlayMode.NORMAL
        );
        missile = new Animation<>(
                Constants.MISSILE_FRAME_DURATION,
                atlas.findRegions(Constants.MISSILE_REGIONS),
                Animation.PlayMode.LOOP_PINGPONG
        );
        seekerEnemy = new Animation<>(
            Constants.ENEMY_FRAME_DURATION,
            atlas.findRegions(Constants.SEEKER_REGION),
            Animation.PlayMode.LOOP_PINGPONG
        );
        sniperEnemy = new Animation<>(
                Constants.ENEMY_FRAME_DURATION,
                atlas.findRegions(Constants.SNIPER_REGION),
                Animation.PlayMode.LOOP_PINGPONG
        );
        flyByEnemy = new Animation<>(
                Constants.ENEMY_FRAME_DURATION,
                atlas.findRegions(Constants.FLY_BY_REGION),
                Animation.PlayMode.LOOP_PINGPONG
        );
        cruiserEnemy = new Animation<>(
                Constants.ENEMY_FRAME_DURATION,
                atlas.findRegions(Constants.CRUISER_REGION),
                Animation.PlayMode.LOOP_PINGPONG
        );
        simpleCannon = atlas.findRegion(Constants.SIMPLE_CANON_REGION);
        holeCannon = atlas.findRegion(Constants.HOLE_CANON_REGION);

        TextureRegion squareButtonDarkRegion = atlas.findRegion(Constants.SQUARE_BUTTON_DARK_REGION);
        squareButtonDark = new NinePatchDrawable(new NinePatch(
                squareButtonDarkRegion,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET
        ));
        TextureRegion squareButtonLightRegion = atlas.findRegion(Constants.SQUARE_BUTTON_LIGHT_REGION);
        squareButtonLight = new NinePatchDrawable(new NinePatch(
                squareButtonLightRegion,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET
        ));
        checkboxUnchecked = squareButtonDark;
        TextureRegion checkboxCheckedRegion = atlas.findRegion(Constants.CHECKBOX_CHECKED);
        checkboxChecked = new NinePatchDrawable(new NinePatch(
                checkboxCheckedRegion,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET,
                UiConstants.SQUARE_BUTTON_9PATCH_OFFSET
        ));
        Sprite sliderKnobSprite = new Sprite(atlas.findRegion(Constants.SLIDER_KNOB_REGION));
        sliderKnobSprite.setSize(UiConstants.SLIDER_KNOB_WIDTH, UiConstants.SLIDER_KNOB_HEIGHT);
        sliderKnob = new SpriteDrawable(sliderKnobSprite);
        Sprite sliderSprite = new Sprite(atlas.findRegion(Constants.SLIDER_REGION));
        //sliderSprite.setSize(sliderSprite.getWidth(), UiConstants.SLIDER_HEIGHT);
        slider = new SpriteDrawable(sliderSprite);
    }

    public void error(AssetDescriptor asset, Throwable throwable) {
        Log.log(LOG_TAG, "Unable to load asset " + asset.fileName);
    }

    public void dispose() {
        assetManager.dispose();
    }



}
