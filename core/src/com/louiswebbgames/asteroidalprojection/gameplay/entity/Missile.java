package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.AvoidAsteroids;
import com.louiswebbgames.asteroidalprojection.utility.Assets;
import com.louiswebbgames.asteroidalprojection.utility.Log;

public class Missile extends Projectile {

    public Missile(float x, float y, GameObject target, boolean playerMissile) {
        super(
                x,
                y,
                //Heading is towards target.
                new Vector2(target.getPosition()).sub(x, y),
                playerMissile ? ProjectileType.PLAYER_MISSILE : ProjectileType.ENEMY_MISSILE
        );
        init();
        setBehavior(new Pursue<>(this, target, GameplayConstants.MISSILE_PURSUE_PREDICT_TIME));
    }

    public Missile(float x, float y, Vector2 heading, boolean playerMissile) {
        super(x, y, heading, playerMissile ? ProjectileType.PLAYER_MISSILE : ProjectileType.ENEMY_MISSILE);
        init();
    }

    protected void init() {
        setMinLinearSpeed(GameplayConstants.MISSILE_MIN_SPEED);
        setMaxLinearSpeed(GameplayConstants.MISSILE_MAX_SPEED);
        setMaxLinearAcceleration(GameplayConstants.MISSILE_ACCEL);
        setMaxAngularSpeed(GameplayConstants.MISSILE_MAX_ANGULAR_SPEED);
        setMaxAngularAcceleration(GameplayConstants.MISSILE_MAX_ANGULAR_ACCEL);
        setAnimation(Assets.instance.missile);
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior) {
        PrioritySteering<Vector2> priorityBehavior = new PrioritySteering<>(this);
        priorityBehavior.add(new AvoidAsteroids(this));
        priorityBehavior.add(behavior);
        this.behavior = priorityBehavior;
    }

    @Override
    public void update(float delta) {
        if (timeSinceSpawn > GameplayConstants.MISSILE_LIFE_SPAN) {
            destroy();
            return;
        }
        super.update(delta);
    }

    @Override
    public void destroy(boolean removeFromCollection) {
        ((PlayStage)getStage()).addExplosion(new MissileExplosion(position.x, position.y));
        super.destroy(removeFromCollection);
    }

}
