package com.louiswebbgames.asteroidalprojection.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.louiswebbgames.asteroidalprojection.gameplay.enemybehavior.AsteroidCollisionDetector;
import com.louiswebbgames.asteroidalprojection.gameplay.entity.*;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.GridRenderer;
import com.louiswebbgames.asteroidalprojection.utility.Log;
import com.louiswebbgames.asteroidalprojection.utility.ShapeRenderRequest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class PlayStage extends Stage {

    Player player;
    AsteroidSpawner asteroidSpawner;

    Vector2 worldOffset;

    Set<Projectile> projectiles;
    Set<Asteroid> asteroids;
    Set<Enemy> enemies;
    Group asteroidGroup;
    Group projectileGroup;
    Group enemyGroup;

    ShapeRenderer shapeRenderer;
    Queue<ShapeRenderRequest> renderRequestQueue;

    ShapeRenderRequest squareBorder;
    ShapeRenderRequest circleBorder;
    GridRenderer gridRenderer;

    public PlayStage(Viewport viewport) {
        super(viewport);
        initPlayer();
        projectiles = new HashSet<>();
        asteroids = new HashSet<>();
        enemies = new HashSet<>();
        asteroidGroup = new Group();
        AsteroidCollisionDetector.setAsteroids(asteroids);
        projectileGroup = new Group();
        enemyGroup = new Group();
        addActor(enemyGroup);
        addActor(asteroidGroup);
        addActor(projectileGroup);
        worldOffset = new Vector2();
        asteroidSpawner = new AsteroidSpawner();
        shapeRenderer = new ShapeRenderer();
        renderRequestQueue = new LinkedList<>();
        squareBorder = renderer -> {
                renderer.set(ShapeRenderer.ShapeType.Line);
                renderer.setColor(Color.WHITE);
                renderer.rect(
                        0.01f - getViewport().getWorldWidth() / 2,
                        0.01f - getViewport().getWorldHeight() / 2,
                        getViewport().getWorldWidth() - 0.01f,
                        getViewport().getWorldHeight() - 0.01f);
        };
        circleBorder = renderer -> {
                renderer.set(ShapeRenderer.ShapeType.Line);
                renderer.setColor(Color.WHITE);
                float radius = 0.99f * getViewport().getWorldWidth() / 2;
                renderer.circle(0, 0, radius, 20);
        };
        gridRenderer = new GridRenderer(GameplayConstants.GRID_WIDTH, GameplayConstants.GRID_DOT_RADIUS);
        addEnemy(new SeekerEnemy(17, 0, player));
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
        addShapeRenderRequest(gridRenderer);
        gridRenderer.setOffset(worldOffset);
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

    public void addEnemy(Enemy enemy) {
        enemy.moveBy(-worldOffset.x, -worldOffset.y);
        enemies.add(enemy);
        enemyGroup.addActor(enemy);
    }

    public void addAsteroid(Asteroid asteroid) {
        asteroid.moveBy(-worldOffset.x, -worldOffset.y);
        asteroids.add(asteroid);
        asteroidGroup.addActor(asteroid);
        asteroid.updatePositionVector();
    }

    public void addProjectile(Projectile projectile) {
        projectile.moveBy(-worldOffset.x, -worldOffset.y);
        projectiles.add(projectile);
        projectileGroup.addActor(projectile);
    }

    public void addShapeRenderRequest(ShapeRenderRequest request) {
        renderRequestQueue.add(request);
    }

    public void removeObject(GameObject object) {
        switch (object.getType()) {
            case ASTEROID:
                asteroids.remove(object);
                break;
            case PROJECTILE:
                projectiles.remove(object);
                break;
            case ENEMY:
                enemies.remove(object);
        }
    }

    public Set<Asteroid> getAsteroids() {
        return asteroids;
    }

    public Set<Projectile> getProjectiles() {
        return projectiles;
    }

    public Set<Enemy> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
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
                PlayStage.this.addAsteroid(new Asteroid(
                        pos.x,
                        pos.y,
                        MathUtils.random(GameplayConstants.ASTEROID_MIN_RADIUS, GameplayConstants.ASTEROID_MAX_RADIUS),
                        velocity
                ));
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
