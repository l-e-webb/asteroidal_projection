package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.utility.Assets;

public class Explosion extends GameObject {

    public Explosion(float x, float y, float radius) {
        super(x, y, radius, EntityType.EXPLOSION, CollisionType.NONE);
        setAnimation(Assets.instance.explosion);
        linearVelocity = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (animation.isAnimationFinished(timeSinceSpawn)) destroy();
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Explosion(getX(), getY(), getRadius());
    }
}
