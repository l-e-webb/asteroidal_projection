package com.louiswebbgames.asteroidalprojection.gameplay;

public class GameplayConstants {

    public static final float ZERO_SPEED_THRESHOLD = 0.1f;

    public static final float HORIZON = 23f;
    public static final float GRID_WIDTH = 0.75f;
    public static final float GRID_DOT_RADIUS = 0.0025f;

    //Player
    public static final float PLAYER_RADIUS = .05f;
    public static final float PLAYER_MAX_SPEED = 2.5f;
    public static final float PLAYER_ACCEL = 5f;
    public static final float PLAYER_DAMPENING = 7f;

    //Player shot
    public static final float PLAYER_SHOT_WIDTH = 0.02f;
    public static final float PLAYER_SHOT_HEIGHT = 0.1f;
    public static final float PLAYER_SHOT_SPEED = 8f;
    public static final float PLAYER_SHOT_COOLDOWN = 0.5f;

    //Asteroids
    public static final float ASTEROID_SPAWN_AVERAGE = 1.5f;
    public static final float ASTEROID_ANGLE_VAR = 7.5f;
    public static final float ASTEROID_SPEED_AVERAGE = 1.8f;
    public static final float ASTEROID_MIN_RADIUS = 0.13f;
    public static final float ASTEROID_MAX_RADIUS = 0.7f;
    public static final float ASTEROID_COLLISION_RADIUS_MOD = 0.75f;
    public static final float ASTEROID_MAX_ANGULAR_VEL = (float) Math.PI / 3;
    public static final float ASTEROID_MIN_ANGULAR_VEL = (float) Math.PI / 8;
}
