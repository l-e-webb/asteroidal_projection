package com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Asteroid;

public class AsteroidCollisionDetector implements RaycastCollisionDetector<Vector2> {

    static AsteroidCollisionDetector instance = new AsteroidCollisionDetector();

    private static Iterable<Asteroid> asteroids;
    private Vector2 asteroidCenter = new Vector2();

    @Override
    public boolean collides(Ray<Vector2> ray) {
        for (Asteroid asteroid : asteroids) {
            if (Intersector.intersectSegmentCircle(
                    ray.start,
                    ray.end,
                    asteroid.getPosition(),
                    asteroid.getRadius() * asteroid.getRadius())
                    ) {
                asteroidCenter.set(asteroid.getPosition());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {
        if (collides(inputRay)) {
            outputCollision.point.set(asteroidCenter);
            Vector2 rayStartToEnd = new Vector2(inputRay.end).sub(inputRay.start);
            Vector2 shipToAsteroid = new Vector2(asteroidCenter).sub(inputRay.start);
            //Calculate the angle between the vector straight from the ship to the asteroid and the ship's
            //actual trajectory.  If this is positive, we want to turn left; if its negative, we want to turn
            //right.  This will be reflected in the collision normal.
            float trajectoryAngle = rayStartToEnd.angleRad() - shipToAsteroid.angleRad();
            if (trajectoryAngle < -MathUtils.PI) trajectoryAngle += 2 * MathUtils.PI;
            outputCollision.normal.set(shipToAsteroid).rotate90(trajectoryAngle > 0 ? 1 : -1);
            return true;
        }
        return false;
    }

    public static void setAsteroids(Iterable<Asteroid> asteroids) {
        AsteroidCollisionDetector.asteroids = asteroids;
    }
}
