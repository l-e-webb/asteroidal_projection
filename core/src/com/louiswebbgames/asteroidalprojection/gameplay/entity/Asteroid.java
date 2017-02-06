package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.Log;

public class Asteroid extends GameObject {

    public static final String LOG_TAG = Asteroid.class.getSimpleName();

    public Asteroid(float x, float y, float radius, Vector2 velocity) {
        super(x, y, radius, EntityType.ASTEROID, CollisionType.CIRCLE);
        independentFacing = true;
        linearVelocity = velocity;
        setMaxLinearAcceleration(0);
        angularVelocity = MathUtils.random(
                GameplayConstants.ASTEROID_MIN_ANGULAR_VEL,
                GameplayConstants.ASTEROID_MAX_ANGULAR_VEL
        );
    }

    @Override
    public TextureRegion getTexture() {
        return Assets.instance.asteroid;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Asteroid(getX(Align.center), getY(Align.center), getBoundingRadius(), linearVelocity);
    }

    @Override
    public float getBoundingRadius() {
        return super.getBoundingRadius() * GameplayConstants.ASTEROID_COLLISION_RADIUS_MOD;
    }

    @Override
    public void reportHit(Vector2 hitDirection) {
        super.reportHit(hitDirection);
        Log.log(LOG_TAG, "Reporting hit on asteroid.");
        float radius = getWidth() / 2;
        if (radius < GameplayConstants.ASTEROID_MIN_RADIUS) {
            destroy();
            return;
        }
        int pieces = MathUtils.random(2, 3);
        float averageRadius;
        if (pieces == 2) {
            averageRadius = radius * 0.7f;
        } else {
            averageRadius = radius * 0.6f;
        }
        float angle = hitDirection.angle() + 90;
        PlayStage stage = (PlayStage) getStage();
        for (int i = 0; i < pieces; i++) {
            Vector2 heading = new Vector2(1, 0);
            angle += 360f / pieces;
            heading.setAngle(angle);
            float newRadius = MathUtils.random(0.9f, 1.1f) * averageRadius;
            stage.addAsteroid(position.x, position.y, newRadius, heading);
        }
        destroy();
    }
}
