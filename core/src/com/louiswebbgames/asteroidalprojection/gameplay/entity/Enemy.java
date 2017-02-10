package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.AvoidAsteroids;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.EnemyFirePattern;

public abstract class Enemy extends GameObject {

    BlendedSteering<Vector2> blendedBehavior;
    EnemyFirePattern firePattern;

    public Enemy(float x, float y, float width, float height) {
        super(x, y, width, height, EntityType.ENEMY, CollisionType.CIRCLE);
        independentFacing = false;
        initBehavior();
    }

    public Enemy(float x, float y, float radius) {
        this(x, y, radius * 2, radius * 2);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        firePattern.update(delta);
    }

    public void initBehavior() {
        blendedBehavior = new BlendedSteering<>(this);
        blendedBehavior.add(new AvoidAsteroids(this), 3f);
        behavior = blendedBehavior;
    }

    public void setPrimaryBehavior(SteeringBehavior<Vector2> behavior) {
        initBehavior();
        blendedBehavior.add(behavior, 1f);
    }

    public void reportHit(Vector2 direction) {
        destroy();
    }

}
