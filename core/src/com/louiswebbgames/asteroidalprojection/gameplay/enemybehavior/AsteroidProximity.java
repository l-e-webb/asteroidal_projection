package com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Asteroid;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;

public class AsteroidProximity extends RadiusProximity<Vector2> {

    GameObject owner;
    GameObject nearestAsteroid;

    private NearestAsteroidCallback callback;

    public AsteroidProximity(GameObject owner, Iterable<Asteroid> asteroids, float radius) {
        super(owner, asteroids, radius);
        this.owner = owner;
        callback = new NearestAsteroidCallback();
    }

    public void update() {
        findNeighbors(callback);
    }

    public GameObject getNearestAsteroid() {
        return nearestAsteroid;
    }

    private class NearestAsteroidCallback implements ProximityCallback<Vector2> {

        @Override
        public boolean reportNeighbor(Steerable<Vector2> neighbor) {
            GameObject neighborObject = (GameObject) neighbor;
            if (nearestAsteroid == null) {
                nearestAsteroid = (GameObject) neighbor;
                return true;
            }
            if (owner.distance2(neighborObject) < owner.distance2(nearestAsteroid)) {
                nearestAsteroid = neighborObject;
            }
            return true;
        }
    }
}
