package com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

public class FlyStraight extends SteeringBehavior<Vector2> {

    public FlyStraight(Steerable<Vector2> owner) {
        super(owner);
    }

    @Override
    protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
        steering.linear = new Vector2(owner.getLinearVelocity()).nor();
        return steering;
    }

}
