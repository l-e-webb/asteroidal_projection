package com.louiswebbgames.asteroidalprojection.gameplay.weapon;

import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.GameObject;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.Laser;

public class LaserWeapon extends Weapon {

    boolean playerLaser;

    public LaserWeapon(GameObject mount, boolean playerLaser) {
        super(mount);
        this.playerLaser = playerLaser;
    }

    @Override
    public void fire(GameObject target) {
        this.fire(target.getPosition());
    }

    @Override
    public void fire(Vector2 target) {
        Vector2 sourcePosition = mount.getPosition();
        Vector2 velocity = new Vector2(target)
                .sub(sourcePosition)
                .setLength(playerLaser ? GameplayConstants.PLAYER_LASER_SPEED : GameplayConstants.ENEMY_LASER_SPEED);
        ((PlayStage) mount.getStage()).addProjectile(new Laser(sourcePosition.x, sourcePosition.y, velocity, playerLaser));
    }
}
