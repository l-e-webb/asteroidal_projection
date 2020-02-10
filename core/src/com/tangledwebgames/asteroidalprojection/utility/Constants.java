package com.tangledwebgames.asteroidalprojection.utility;

public class Constants {

    //Display constants.
    public static final float UI_VIEWPORT_WIDTH = 960;
    public static final float UI_VIEWPORT_HEIGHT = 960;
    public static final int HTML_EMBED_WIDTH = 800;
    public static final int HTML_EMBED_HEIGHT = 800;

    //Image asset constants.
    public static final String ATLAS_PATH = "images/sprites.pack.atlas";
    public static final String PLAYER_REGION = "player_ship";
    public static final String ASTEROID_REGION = "asteroid";
    public static final String PLAYER_LASER_REGION = "bluelaser";
    public static final String PLAYER_PIERCING_LASER_REGION = "bluesharp";
    public static final String PLAYER_ROUND_LASER_REGION = "blueround";
    public static final String ENEMY_LASER_REGION = "redlaser";
    public static final String ENEMY_PIERCING_LASER_REGION = "redsharp";
    public static final String ENEMY_ROUND_LASER_REGION = "redround";
    public static final String MISSILE_REGIONS = "missile";
    public static final String TRIPLE_LASER_REGION = "triplelaser";
    public static final String MISSILE_AMMO_REGION = "missileammo";
    public static final String POINTS_REGION = "points";
    public static final String EXTRA_HEALTH_REGION = "extralife";
    public static final String PIERCING_LASER_REGION = "piercinglaser";
    public static final String EXPLOSION_REGIONS = "explosion";
    public static final String SEEKER_REGION = "seeker";
    public static final String SNIPER_REGION = "sniper";
    public static final String FLY_BY_REGION = "fly_by";
    public static final String CRUISER_REGION = "cruiser";
    public static final String SIMPLE_CANON_REGION = "simple_canon";
    public static final String HOLE_CANON_REGION = "hole_canon";
    public static final String SQUARE_BUTTON_DARK_REGION = "base_label_9patch";
    public static final String SQUARE_BUTTON_LIGHT_REGION = "base_label_9patch_filled";
    public static final String CHECKBOX_CHECKED = "checkbox_checked";
    public static final String SLIDER_KNOB_REGION = "slider_knob";
    public static final String SLIDER_REGION = "slider";
    public static final String GRAY_SQUARE_REGION = "gray_square";

    //Sound asset constants.
    public static final String BACKGROUND_MUSIC = "audio/Rocket.mp3";
    public static final String PLAYER_LASER_SOUND = "audio/player_laser.mp3";
    public static final String PLAYER_PIERCING_LASER_SOUND = "audio/player_piercing_laser.mp3";
    public static final String PLAYER_MISSILE_SOUND = "audio/player_missile.mp3";
    public static final String ENEMY_LASER_SOUND = "audio/enemy_laser.mp3";
    public static final String ENEMY_PIERCING_LASER_SOUND = "audio/enemy_piercing_laser.mp3";
    public static final String ENEMY_ROUND_LASER_SOUND = "audio/enemy_round_laser.mp3";
    public static final String IMPACT_SOUND = "audio/impact.mp3";
    public static final String POWERUP_SOUND = "audio/powerup.mp3";
    public static final String EXPLOSION_SOUND = "audio/explosion.mp3";
    public static final String LARGE_EXPLOSION_SOUND = "audio/explosion_large.mp3";
    public static final String COIN_SOUND = "audio/coin.mp3";
    public static final float DISTANCE_FROM_ORIGIN_SOUND_CUTOFF = 12f;
    public static final float TRIPLE_LASER_SOUND_AMP = 1.4f;
    public static final float LARGE_EXPLOSION_SOUND_MOD = 1f;
    public static final float ASTEROID_EXPLOSION_SOUND_MOD = 0.75f;
    public static final float EXPLOSION_SOUND_MOD = 0.6f;
    public static final float POWERUP_SOUND_MOD = 1.5f;
    public static final float MUSIC_VOLUME_MOD = 0.8f;

    //Animation constants
    public static final float MISSILE_FRAME_DURATION = 0.4f;
    public static final float PLAYER_FRAME_DURATION = 0.3f;
    public static final float ENEMY_FRAME_DURATION = 0.3f;
    public static final float PLAYER_CANNON_RADIUS = 0.225f;
    public static final float PLAYER_CANNON_WIDTH_RATIO = 0.5f;
    public static final float PLAYER_CANNON_HEIGHT_RATIO = 0.3f;
    public static final float ENEMY_CANNON_RADIUS = 0.3f;
    public static final float ENEMY_HOLE_CANNON_RADIUS = 0.3f;
    public static final float POINT_DEFENSE_CANNON_RADIUS = 0.25f;
    public static final float SEEKER_CANNON_WIDTH_RATIO = 0.5f;
    public static final float SEEKER_CANNON_HEIGHT_RATIO = 0.33f;
    public static final float FLY_BY_CANNON_WIDTH_RATIO = 0.5f;
    public static final float FLY_BY_CANNON_HEIGHT_RATIO = 0.33f;
    public static final float SNIPER_CANNON_WIDTH_RATIO = 0.5f;
    public static final float SNIPER_CANNON_HEIGHT_RATIO = 0.33f;
    public static final float CRUISER_MAIN_CANNON_HEIGHT_RATIO = 0.33f;
    public static final float CRUISER_MAIN_CANNON_WIDTH_RATIO = 0f;
    public static final float CRUISER_SECONDARY_CANNON_HEIGHT_RATIO = -0.33f;
    public static final float CRUISER_SECONDARY_CANNON_WIDTH_RATIO = 0f;
    public static final float CRUISER_SPREAD_CANNON_HEIGHT_RATIO = 0f;
    public static final float CRUISER_SPREAD_CANNON_WIDTH_RATIO = 0f;
    public static final float CRUISER_POINT_DEF_1_WIDTH_RATIO = 0f;
    public static final float CRUISER_POINT_DEF_1_HEIGHT_RATIO = 0.45f;
    public static final float CRUISER_POINT_DEF_2_WIDTH_RATIO = 0.33f;
    public static final float CRUISER_POINT_DEF_2_HEIGHT_RATIO = -0.33f;
    public static final float CRUISER_POINT_DEF_3_WIDTH_RATIO = -0.33f;
    public static final float CRUISER_POINT_DEF_3_HEIGHT_RATIO = -0.33f;
}
