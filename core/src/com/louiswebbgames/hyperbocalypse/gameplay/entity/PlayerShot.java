package com.louiswebbgames.hyperbocalypse.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.hyperbocalypse.gameplay.GameplayConstants;
import com.louiswebbgames.hyperbocalypse.utility.Assets;

public class PlayerShot extends GameObject {

    public PlayerShot(Vector2 velocity) {
        super(0, 0, GameplayConstants.PLAYER_SHOT_WIDTH, GameplayConstants.PLAYER_SHOT_HEIGHT, EntityType.PROJECTILE);
        this.linearVelocity = velocity;
        setOrientation(velocity.angleRad());
    }

    @Override
    public TextureRegion getTexture() {
        return Assets.instance.playerLaser;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new PlayerShot(linearVelocity);
    }
}
