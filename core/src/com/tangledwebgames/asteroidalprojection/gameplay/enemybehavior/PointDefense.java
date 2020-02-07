package com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Asteroid;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.EnemyGun;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.GameObject;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.Projectile;
import com.tangledwebgames.asteroidalprojection.gameplay.weapon.BaseWeapon;

public class PointDefense extends EnemyGun {

    protected AsteroidProximity proximity;

    public PointDefense(float x, float y, float width, float height, TextureRegion texture, boolean rotateWithShot, Iterable<Asteroid> asteroids) {
        super(x, y, width, height, FireRate.SLOW, new BaseWeapon(null, Projectile.ProjectileType.ENEMY_LASER), texture, rotateWithShot);
        proximity = new AsteroidProximity(
                this,
                asteroids,
                GameplayConstants.CRUISER_POINT_DEFENSE_RANGE
        );
        makesSound = false;
    }

    public PointDefense(float x, float y, Iterable<Asteroid> asteroids) {
        this(x, y, 0, 0, null, false, asteroids);
    }

    @Override
    public Vector2 fire() {
        proximity.update();
        GameObject targetAsteroid = proximity.getNearestAsteroid();
        if (targetAsteroid == null ||
                targetAsteroid.distance2(this) > GameplayConstants.CRUISER_POINT_DEFENSE_RANGE * GameplayConstants.CRUISER_POINT_DEFENSE_RANGE)
            return null;
        weapon.fire(targetAsteroid);
        return new Vector2(targetAsteroid.getPosition()).sub(getPosition());
    }

    @Override
    public Location<Vector2> newLocation() {
        return new PointDefense(getX(), getY(), ((PlayStage)getStage()).getAsteroids());
    }
}
