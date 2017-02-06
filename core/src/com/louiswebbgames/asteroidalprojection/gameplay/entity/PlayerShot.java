package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.Log;

public class PlayerShot extends GameObject {

    public PlayerShot(Vector2 velocity) {
        super(0, 0, GameplayConstants.PLAYER_SHOT_WIDTH, GameplayConstants.PLAYER_SHOT_HEIGHT, EntityType.PLAYER_SHOT, CollisionType.POINT);
        this.linearVelocity = velocity;
        setOrientation(velocity.angleRad());
    }

    @Override
    public void update(float delta) {
        for (Asteroid asteroid : ((PlayStage)getStage()).getAsteroids()) {
            if (collidesWith(asteroid)) {
                Log.log(asteroid.getCircle().toString());
                Log.log(position.toString());
                asteroid.reportHit(linearVelocity);
                destroy();
                return;
            }
        }
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
