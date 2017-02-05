package com.louiswebbgames.hyperbocalypse.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.louiswebbgames.hyperbocalypse.gameplay.entity.Asteroid;
import com.louiswebbgames.hyperbocalypse.gameplay.entity.GameObject;
import com.louiswebbgames.hyperbocalypse.gameplay.entity.Player;
import com.louiswebbgames.hyperbocalypse.gameplay.entity.PlayerShot;
import com.louiswebbgames.hyperbocalypse.gameplay.geometry.Projection;
import com.louiswebbgames.hyperbocalypse.utility.Log;
import com.louiswebbgames.hyperbocalypse.utility.ShapeRenderRequest;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class PlayStage extends Stage {

    GameObject player;
    AsteroidSpawner asteroidSpawner;

    Vector2 worldOffset;

    Set<PlayerShot> playerShots;
    Set<Asteroid> asteroidSet;

    ShapeRenderer shapeRenderer;
    Queue<ShapeRenderRequest> renderRequestQueue;

    ShapeRenderRequest squareBorder;
    ShapeRenderRequest circleBorder;

    public PlayStage(Viewport viewport) {
        super(viewport);
        initPlayer();
        worldOffset = new Vector2();
        asteroidSpawner = new AsteroidSpawner();
        shapeRenderer = new ShapeRenderer();
        renderRequestQueue = new LinkedList<ShapeRenderRequest>();
        squareBorder = new ShapeRenderRequest() {
            @Override
            public void draw(ShapeRenderer renderer) {
                renderer.set(ShapeRenderer.ShapeType.Line);
                renderer.setColor(Color.WHITE);
                renderer.rect(
                        0.01f - getViewport().getWorldWidth() / 2,
                        0.01f - getViewport().getWorldHeight() / 2,
                        getViewport().getWorldWidth() - 0.01f,
                        getViewport().getWorldHeight() - 0.01f);
            }
        };
        circleBorder = new ShapeRenderRequest() {
            @Override
            public void draw(ShapeRenderer renderer) {
                renderer.set(ShapeRenderer.ShapeType.Line);
                renderer.setColor(Color.WHITE);
                float radius = 0.99f * getViewport().getWorldWidth() / 2;
                renderer.circle(0, 0, radius, 20);
            }
        };
    }

    protected void initPlayer() {
        player = new Player();
        addActor(player);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        asteroidSpawner.update(delta);
    }

    @Override
    public void draw() {
        super.draw();

        addShapeRenderRequest(squareBorder);
        addShapeRenderRequest(circleBorder);
        shapeRenderer.setProjectionMatrix(getViewport().getCamera().combined);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        while (!renderRequestQueue.isEmpty()) {
            renderRequestQueue.poll().draw(shapeRenderer);
        }
        shapeRenderer.end();
    }

    public void moveWorld(Vector2 motion) {
        worldOffset.add(motion);
    }

    public void moveWorld(float x, float y) {
        moveWorld(new Vector2(x, y));
    }

    public Vector2 getWorldOffset() {
        return worldOffset;
    }

    public void addEnemy() {}

    public void addAsteroid(float x, float y, float radius, Vector2 velocity) {
        //TODO: add to set.
        addActor(new Asteroid(x, y, radius, velocity));
    }

    public void addPlayerProjectile(Vector2 heading) {
        PlayerShot shot = new PlayerShot(heading.setLength(GameplayConstants.PLAYER_SHOT_SPEED));
        shot.moveBy(-worldOffset.x, -worldOffset.y);
        //TODO: add to set.
        addActor(shot);
        shot.updatePositionVector();
    }

    public void addShapeRenderRequest(ShapeRenderRequest request) {
        renderRequestQueue.add(request);
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }

    private class AsteroidSpawner {

        float timer;
        float nextAsteroid;

        AsteroidSpawner() {
            timer = 0;
            setNext();
        }

        void update(float delta) {
            timer += delta;
            if (timer > nextAsteroid) {
                Vector2 pos = new Vector2(1, 0);
                pos.setAngle(MathUtils.random(360f));
                pos.setLength(GameplayConstants.HORIZON * 0.95f);
                Vector2 velocity = new Vector2(1, 0);
                velocity.setAngle(
                        pos.angle() + 180 +
                        MathUtils.random(-GameplayConstants.ASTEROID_ANGLE_VAR,
                                GameplayConstants.ASTEROID_ANGLE_VAR)
                );
                velocity.setLength(MathUtils.random(0.5f, 1.5f) * GameplayConstants.ASTEROID_SPEED_AVERAGE);
                PlayStage.this.addAsteroid(
                        pos.x,
                        pos.y,
                        MathUtils.random(GameplayConstants.ASTEROID_MIN_RADIUS, GameplayConstants.ASTEROID_MAX_RADIUS),
                        velocity
                );
                timer = 0;
                setNext();
            }
        }

        void setNext() {
            nextAsteroid = MathUtils.random(0.5f, 1.5f) * GameplayConstants.ASTEROID_SPAWN_AVERAGE;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 mousePos = screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Log.log("Mouse", mousePos.toString());
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
