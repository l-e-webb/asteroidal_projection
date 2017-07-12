package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.utility.Assets;

public class Explosion extends GameObject {

    private Animation<TextureRegion> animation;

    public Explosion(float x, float y, float radius) {
        super(x, y, radius, EntityType.EXPLOSION, CollisionType.NONE);
        animation = Assets.instance.explosion;
        linearVelocity = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (animation.isAnimationFinished(timeSinceSpawn)) destroy();
    }

    @Override
    public TextureRegion getTexture() {
        return animation.getKeyFrame(timeSinceSpawn);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Explosion(getX(), getY(), getBoundingRadius());
    }
}
