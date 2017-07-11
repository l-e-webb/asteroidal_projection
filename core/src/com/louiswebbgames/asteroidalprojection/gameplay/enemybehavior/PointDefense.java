package com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Asteroid;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.EnemyGun;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Projectile;
import com.louiswebbgames.asteroidalprojection.gameplay.weapon.BaseWeapon;

public class PointDefense extends EnemyGun {

    protected AsteroidProximity proximity;

    public PointDefense(float x, float y, Iterable<Asteroid> asteroids) {
        super(x, y, FireRate.SLOW, new BaseWeapon(null, Projectile.ProjectileType.ENEMY_LASER));
        proximity = new AsteroidProximity(
                this,
                asteroids,
                GameplayConstants.CRUISER_POINT_DEFENSE_RANGE
        );
    }

    @Override
    public void fire() {
        proximity.update();
        GameObject targetAsteroid = proximity.getNearestAsteroid();
        if (targetAsteroid == null ||
                targetAsteroid.distance2(this) > GameplayConstants.CRUISER_POINT_DEFENSE_RANGE * GameplayConstants.CRUISER_POINT_DEFENSE_RANGE)
            return;
        weapon.fire(targetAsteroid);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new PointDefense(getX(), getY(), ((PlayStage)getStage()).getAsteroids());
    }
}
