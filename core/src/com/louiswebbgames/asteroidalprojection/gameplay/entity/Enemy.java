package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.AvoidAsteroids;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.EnemyFirePattern;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.Projection;
import com.louiswebbgames.asteroidalprojection.utility.ShapeRenderRequest;

public abstract class Enemy extends GameObject {

    EnemyFirePattern firePattern;
    private AvoidAsteroidDebugRenderer debugRenderer = new AvoidAsteroidDebugRenderer();

    public Enemy(float x, float y, float width, float height) {
        super(x, y, width, height, EntityType.ENEMY, CollisionType.CIRCLE);
        independentFacing = false;
    }

    public Enemy(float x, float y, float radius) {
        this(x, y, radius * 2, radius * 2);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        firePattern.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (AvoidAsteroids.debug) {
            ((PlayStage)getStage()).addShapeRenderRequest(debugRenderer);
        }
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior) {
        PrioritySteering<Vector2> priorityBehavior = new PrioritySteering<>(this);
        priorityBehavior.add(new AvoidAsteroids(this));
        priorityBehavior.add(behavior);
        this.behavior = priorityBehavior;
    }

    @Override
    public void destroy(boolean removeFromCollection) {
        ((PlayStage)getStage()).addExplosion(position.x, position.y, GameplayConstants.EXPLOSION_LARGE_RADIUS);
        super.destroy(removeFromCollection);
    }

    public boolean reportHit(Vector2 direction) {
        destroy(false);
        return true;
    }

    private class AvoidAsteroidDebugRenderer implements ShapeRenderRequest {
        @Override
        public void draw(ShapeRenderer renderer) {
            Vector2[] whiskerVertices = new Vector2[3];
            whiskerVertices[0] = new Vector2(1, 0)
                    .setLength(GameplayConstants.ASTEROID_AVOIDANCE_CENTRAL_RAY_LENGTH)
                    .setAngleRad(getOrientation())
                    .add(getPosition());
            whiskerVertices[1] = new Vector2(1, 0)
                    .setLength(GameplayConstants.ASTEROID_AVOIDANCE_WHISKER_LENGTH)
                    .setAngleRad(getOrientation() + GameplayConstants.ASTEROID_AVOIDANCE_WHISKER_ANGLE)
                    .add(getPosition());
            whiskerVertices[2] = new Vector2(1, 0)
                    .setLength(GameplayConstants.ASTEROID_AVOIDANCE_WHISKER_LENGTH)
                    .setAngleRad(getOrientation() - GameplayConstants.ASTEROID_AVOIDANCE_WHISKER_ANGLE)
                    .add(getPosition());

            renderer.setColor(Color.RED);
            renderer.set(ShapeRenderer.ShapeType.Line);
            for (Vector2 whiskerVertex : whiskerVertices) {
                Vector2 vertex1 = Projection.project(getPosition());
                Vector2 vertex2 = Projection.project(whiskerVertex);
                renderer.line(vertex1, vertex2);
            }
        }
    }

}
