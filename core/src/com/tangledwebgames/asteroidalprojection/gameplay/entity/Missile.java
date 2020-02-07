package com.tangledwebgames.asteroidalprojection.gameplay.entity;

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
import com.tangledwebgames.asteroidalprojection.utility.SoundManager;

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
