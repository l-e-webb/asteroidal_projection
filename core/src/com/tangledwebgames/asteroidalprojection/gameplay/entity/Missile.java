package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.AvoidAsteroids;
import com.tangledwebgames.asteroidalprojection.utility.Assets;
import com.tangledwebgames.asteroidalprojection.utility.Constants;
import com.tangledwebgames.asteroidalprojection.utility.Log;
import com.tangledwebgames.asteroidalprojection.utility.SoundManager;

public class Missile extends Projectile {

    protected GameObject target;

    public Missile(float x, float y, GameObject target, Vector2 heading, boolean playerMissile) {
        super(
                x,
                y,
                heading,
                playerMissile ? ProjectileType.PLAYER_MISSILE : ProjectileType.ENEMY_MISSILE
        );
        init();
        if (target != null) {
            setTarget(target);
        }
    }

    public Missile(float x, float y, Vector2 heading, boolean playerMissile) {
        this(x, y, null, heading, playerMissile);
    }

    protected void init() {
        setMinLinearSpeed(GameplayConstants.MISSILE_MIN_SPEED);
        setMaxLinearSpeed(GameplayConstants.MISSILE_MAX_SPEED);
        setMaxLinearAcceleration(GameplayConstants.MISSILE_ACCEL);
        setMaxAngularSpeed(GameplayConstants.MISSILE_MAX_ANGULAR_SPEED);
        setMaxAngularAcceleration(GameplayConstants.MISSILE_MAX_ANGULAR_ACCEL);
        setAnimation(Assets.instance.missile);
    }

    public void setTarget(GameObject target) {
        setBehavior(new Pursue<>(this, target, GameplayConstants.MISSILE_PURSUE_PREDICT_TIME));
        this.target = target;
    }

    protected void setBehavior(SteeringBehavior<Vector2> behavior) {
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
        } else if (target != null && !target.hasParent()) {
            float nearestD = Float.MAX_VALUE;
            Enemy nearest = null;
            for (Enemy enemy : getPlayStage().getEnemies()) {
                if (distance2(enemy) < nearestD) {
                    nearest = enemy;
                }
            }
            if (nearest != null) {
                setTarget(nearest);
            }
        }
        super.update(delta);
    }

    @Override
    public void calculateVelocity(float delta) {
        super.calculateVelocity(delta);
    }

    @Override
    public void destroy(boolean removeFromCollection) {
        ((PlayStage)getStage()).addExplosion(new MissileExplosion(position.x, position.y));
        float distanceFromOrigin = distanceFromOrigin();
        if (distanceFromOrigin < Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF) {
            float soundMod = (Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF - distanceFromOrigin)
                    / Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF;
            soundMod *= Constants.LARGE_EXPLOSION_SOUND_MOD;
            SoundManager.playSoundEffect(SoundManager.SoundEffect.LARGE_EXPLOSION, soundMod);
        }
        super.destroy(removeFromCollection);
    }

}
