package com.louiswebbgames.asteroidalprojection.gameplay.entity;

public abstract class Projectile extends GameObject {

    public Projectile(float x, float y, float width, float height) {
        super(x, y, width, height, EntityType.PROJECTILE, CollisionType.POINT);
    }
}
