package com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Asteroid;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.SteerableObject;

public class AsteroidProximity extends RadiusProximity<Vector2> {

    SteerableObject owner;
    SteerableObject nearestAsteroid;

    private NearestAsteroidCallback callback;

    public AsteroidProximity(SteerableObject owner, Iterable<Asteroid> asteroids, float radius) {
        super(owner, asteroids, radius);
        this.owner = owner;
        callback = new NearestAsteroidCallback();
    }

    public void update() {
        findNeighbors(callback);
    }

    public SteerableObject getNearestAsteroid() {
        return nearestAsteroid;
    }

    private class NearestAsteroidCallback implements ProximityCallback<Vector2> {

        @Override
        public boolean reportNeighbor(Steerable<Vector2> neighbor) {
            SteerableObject neighborObject = (SteerableObject) neighbor;
            if (nearestAsteroid == null) {
                nearestAsteroid = (SteerableObject) neighbor;
                return true;
            }
            if (owner.distance2(neighborObject) < owner.distance2(nearestAsteroid)) {
                nearestAsteroid = neighborObject;
            }
            return true;
        }
    }
}
