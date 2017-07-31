package com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;

public class MaintainDistance extends SteeringBehavior<Vector2> {

    protected Steerable<Vector2> target;
    protected float distance;
    private float distance2;
    private Seek<Vector2> seekBehavior;
    private Flee<Vector2> fleeBehavior;

    public MaintainDistance(Steerable<Vector2> owner, Steerable<Vector2> target, float distance) {
        super(owner);
        this.target = target;
        this.distance = distance;
        distance2 = distance * distance;
        seekBehavior = new Seek<>(owner, target);
        fleeBehavior = new Flee<>(owner, target);
    }

    @Override
    protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
        if (owner.getPosition().dst2(target.getPosition()) > distance2) {
            return seekBehavior.calculateSteering(steering);
        } else {
            return fleeBehavior.calculateSteering(steering);
        }
    }

}
