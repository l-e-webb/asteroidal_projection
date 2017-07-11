package com.louiswebbgames.asteroidalprojection.gameplay;

import com.badlogic.gdx.math.MathUtils;

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
    public static final float PLAYER_BLINKING_DURATION = 2f;
    public static final float PLAYER_BLINDING_PERIOD = 0.2f;
    public static final int PLAYER_MAX_HEALTH = 5;
    public static final float PLAYER_TRIPLE_LASER_SPREAD = 45f;
    public static final float PLAYER_LASER_COOLDOWN = 0.5f;

    //Enemy
    public static final float ENEMY_COLLISION_RADIUS_MOD = 1.25f;
    public static final float SEEKER_RADIUS = 0.15f;
    public static final float SEEKER_MIN_SPEED = 0.5f;
    public static final float SEEKER_MAX_SPEED = 1.8f;
    public static final float SEEKER_ACCEL = 4f;
    public static final float SEEKER_MAX_ANGULAR_SPEED = 3 * MathUtils.PI / 2;
    public static final float SEEKER_MAX_ANGULAR_ACCEL = 3 * MathUtils.PI;
    public static final float SEEKER_MAINTAIN_DISTANCE = 3f;
    public static final float SEEKER_WEAPON_RANGE = 4f;
    public static final int SEEKER_POINT_VALUE = 50;
    public static final float SNIPER_RADIUS = 0.15f;
    public static final float SNIPER_MIN_SPEED = 0.25f;
    public static final float SNIPER_MAX_SPEED = 1.4f;
    public static final float SNIPER_ACCEL = 3f;
    public static final float SNIPER_MAX_ANGULAR_SPEED = 3 * MathUtils.PI / 2;
    public static final float SNIPER_MAX_ANGULAR_ACCEL = 3 * MathUtils.PI;
    public static final float SNIPER_MAINTAIN_DISTANCE = 12f;
    public static final float SNIPER_WEAPON_RANGE = 14f;
    public static final int SNIPER_POINT_VALUE = 60;
    public static final float FLY_BY_RADIUS = 0.15f;
    public static final float FLY_BY_MIN_SPEED = 2.5f;
    public static final float FLY_BY_MAX_SPEED = 5.5f;
    public static final float FLY_BY_ACCEL = 8f;
    public static final float FLY_BY_MAX_ANGULAR_SPEED = MathUtils.PI / 2;
    public static final float FLY_BY_MAX_ANGULAR_ACCEL = 2 * MathUtils.PI / 3;
    public static final float FLY_BY_WEAPON_RANGE = 5f;
    public static final int FLY_BY_POINT_VALUE = 70;

    //Cruisers
    public static final float CRUISER_WIDTH = 0.55f;
    public static final float CRUISER_HEIGHT = 0.9f;
    public static final float CRUISER_MIN_SPEED = 0.75f;
    public static final float CRUISER_MAX_SPEED = 1.5f;
    public static final float CRUISER_ACCEL = 2f;
    public static final float CRUISER_MAX_ANGULAR_SPEED = MathUtils.PI / 4;
    public static final float CRUISER_MAX_ANGULAR_ACCEL = MathUtils.PI / 2;
    public static final float CRUISER_MAIN_GUN_RANGE = 8f;
    public static final float CRUISER_MAIN_GUN_SPREAD = 25f;
    public static final float CRUISER_SECONDARY_GUN_RANGE = 4.5f;
    public static final float CRUISER_SPREAD_GUN_RANGE = 4.5f;
    public static final float CRUISER_SPREAD_GUN_SPREAD = 30f;
    public static final int CRUISER_SPREAD_GUN_NUM_SHOTS = 10;
    public static final float CRUISER_POINT_DEFENSE_RANGE = 2f;
    public static final int CRUISER_HEALTH = 50;
    public static final float ASTEROID_RADIUS_DAMAGE_CUTOFF = 0.25f;
    public static final int CRUISER_POINT_VALUE = 1000;

    //Projectiles
    public static final float LASER_WIDTH = 0.15f;
    public static final float LASER_HEIGHT = 0.15f;
    public static final float PIERCING_LASER_WIDTH = 0.15f;
    public static final float PIERCING_LASER_HEIGHT = 0.15f;
    public static final float ROUND_PROJECTILE_WIDTH = 0.25f;
    public static final float MISSILE_WIDTH = 0.1f;
    public static final float MISSILE_HEIGHT = 0.1f;
    public static final float MISSILE_FRAME_DURATION = 0.25f;
    public static final float PLAYER_LASER_SPEED = 8f;
    public static final float ENEMY_LASER_SPEED = 8f;
    public static final float PLAYER_ROUND_SPEED = 6f;
    public static final float ENEMY_ROUND_SPEED = 6f;
    public static final float PLAYER_MISSILE_INITIAL_SPEED = 5f;
    public static final float PLAYER_MISSILE_ACCEL = 5f;
    public static final float ENEMY_MISSILE_INITIAL_SPEED = 5f;
    public static final float ENEMY_MISSILE_ACCEL = 5f;
    public static final float PIERCING_LASER_COLLISION_COOLDOWN = 0.15f;


    //Powerups
    public static final float POWERUP_WIDTH = 0.4f;
    public static final float POWERUP_HEIGHT = 0.4f;
    public static final float POWERUP_MIN_SPEED = 0;
    public static final float POWERUP_MAX_SPEED = 0.2f;
    public static final float PIERCING_LASER_DURATION = 20f;
    public static final float TRIPLE_LASER_DURATION = 15f;
    public static final int POINTS_POWERUP_VALUE = 500;
    public static final float POWERUP_SPAWN_CHANCE = 0.125f;


    //Asteroids
    public static final float ASTEROID_SPEED_AVERAGE = 1.8f;
    public static final float ASTEROID_MIN_RADIUS = 0.13f;
    public static final float ASTEROID_MAX_RADIUS = 0.7f;
    public static final float ASTEROID_COLLISION_RADIUS_MOD = 0.75f;
    public static final float ASTEROID_MAX_ANGULAR_VEL = (float) Math.PI / 3;
    public static final float ASTEROID_MIN_ANGULAR_VEL = (float) Math.PI / 8;

    //Asteroid spawning
    public static final float ASTEROID_SPAWN_AVERAGE = 1.5f;
    public static final float ASTEROID_ANGLE_VAR = 7.5f;
    public static final int MIN_ASTEROIDS_PRESENT = 7;
    public static final int MAX_ASTEROIDS_PRESENT = 17;

    //Enemy spawning
    public static final float ENEMY_SPAWN_AVERAGE = 3f;
    public static final float MIN_ENEMIES_PRESENT = 1;
    public static final float MAX_ENEMIES_PRESENT = 5;

    //Steering behaviors
    public static final float ASTEROID_AVOIDANCE_CENTRAL_RAY_LENGTH = 2f;
    public static final float ASTEROID_AVOIDANCE_WHISKER_LENGTH = 1f;
    public static final float ASTEROID_AVOIDANCE_WHISKER_ANGLE = MathUtils.PI / 12;

    //Explosion
    public static final float EXPLOSION_LARGE_RADIUS = 0.4f;
    public static final float EXPLOSION_SMALL_RDAIUS = 0.2f;
    public static final float EXPLOSION_FRAME_DURATION = 0.12f;

}
