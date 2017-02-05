package com.louiswebbgames.hyperbocalypse.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.louiswebbgames.hyperbocalypse.gameplay.GameplayConstants;
import com.louiswebbgames.hyperbocalypse.utility.Assets;

public class Asteroid extends GameObject {

    public Asteroid(float x, float y, float radius, Vector2 velocity) {
        super(x, y, radius, EntityType.ASTEROID);
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
}
