package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.AvoidAsteroids;
import com.tangledwebgames.asteroidalprojection.gameplay.geometry.Projection;
import com.tangledwebgames.asteroidalprojection.utility.Constants;
import com.tangledwebgames.asteroidalprojection.utility.ShapeRenderRequest;
import com.tangledwebgames.asteroidalprojection.utility.SoundManager;

public abstract class Enemy extends GameObject {

    private AvoidAsteroidDebugRenderer debugRenderer = new AvoidAsteroidDebugRenderer();

    protected int pointValue;

    public Enemy(float x, float y, float width, float height) {
        super(x, y, width, height, EntityType.ENEMY, CollisionType.CIRCLE);
        independentFacing = false;
    }

    public Enemy(float x, float y, float radius) {
        this(x, y, radius * 2, radius * 2);
    }

    public void init() {}

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (AvoidAsteroids.debug) {
            getPlayStage().addShapeRenderRequest(debugRenderer);
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
        PlayStage stage = (PlayStage) getStage();
        stage.addExplosion(position.x, position.y, getExplosionRadius());
        stage.incrementScore(pointValue);
        float distanceFromOrigin = distanceFromOrigin();
        if (distanceFromOrigin < Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF) {
            boolean isCruiser = this instanceof EnemyCruiser;
            SoundManager.SoundEffect effect = isCruiser ? SoundManager.SoundEffect.LARGE_EXPLOSION : SoundManager.SoundEffect.EXPLOSION;
            float soundMod = (Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF - distanceFromOrigin)
                    / Constants.DISTANCE_FROM_ORIGIN_SOUND_CUTOFF;
            if (isCruiser) soundMod *= Constants.LARGE_EXPLOSION_SOUND_MOD;
            SoundManager.playSoundEffect(effect, soundMod);
        }
        super.destroy(removeFromCollection);
    }

    public boolean reportHit(Vector2 direction) {
        destroy(false);
        return true;
    }

    @Override
    public float getProjectedRadius() {
        return super.getProjectedRadius() * GameplayConstants.ENEMY_COLLISION_RADIUS_MOD;
    }

    public float getExplosionRadius() {
        return GameplayConstants.EXPLOSION_LARGE_RADIUS;
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

    public static Enemy getRandomEnemy(float x, float y, GameObject player, int epoch) {
        switch (MathUtils.random(2)) {
            case 0:
            default:
                return SeekerEnemy.getRandomSeeker(x, y, player, epoch);
            case 1:
                return SniperEnemy.getRandomSniper(x, y, player, epoch);
            case 2:
                return FlyByEnemy.getRandomFlyBy(x, y, player, epoch);
        }
    }

}
