package com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;

public class AvoidAsteroids extends RaycastObstacleAvoidance<Vector2> {

    public static boolean debug = false;

    public AvoidAsteroids(Steerable<Vector2> owner) {
        super(
                owner,
                new CentralRayWithWhiskersConfiguration<>(
                        owner,
                        GameplayConstants.ASTEROID_AVOIDANCE_CENTRAL_RAY_LENGTH,
                        GameplayConstants.ASTEROID_AVOIDANCE_WHISKER_LENGTH,
                        GameplayConstants.ASTEROID_AVOIDANCE_WHISKER_ANGLE
                ),
                AsteroidCollisionDetector.instance,
                GameplayConstants.ASTEROID_MAX_RADIUS + owner.getBoundingRadius()
        );
    }
}
