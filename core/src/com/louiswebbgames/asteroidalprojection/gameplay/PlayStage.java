package com.louiswebbgames.asteroidalprojection.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
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

    int score;
    float time;
    boolean paused;

    Player player;
    AsteroidSpawner asteroidSpawner;
    EnemySpawner enemySpawner;

    Vector2 worldOffset;

    Set<Projectile> projectiles;
    Set<Asteroid> asteroids;
    Set<Enemy> enemies;
    Group asteroidGroup;
    Group projectileGroup;
    Group enemyGroup;
    Group powerupGroup;
    Group explosionGroup;
    int numCruisers;

    ShapeRenderer shapeRenderer;
    Queue<ShapeRenderRequest> renderRequestQueue;

    ShapeRenderRequest squareBorder;
    ShapeRenderRequest circleBorder;
    GridRenderer gridRenderer;

    public PlayStage(Viewport viewport) {
        super(viewport);
        projectiles = new HashSet<>();
        asteroids = new HashSet<>();
        enemies = new HashSet<>();
        asteroidGroup = new Group();
        AsteroidCollisionDetector.setAsteroids(asteroids);
        projectileGroup = new Group();
        enemyGroup = new Group();
        powerupGroup = new Group();
        explosionGroup = new Group();
        player = new Player();
        addActor(player);
        addActor(enemyGroup);
        addActor(asteroidGroup);
        addActor(projectileGroup);
        addActor(powerupGroup);
        addActor(explosionGroup);
        worldOffset = new Vector2();
        asteroidSpawner = new AsteroidSpawner();
        enemySpawner = new EnemySpawner();
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
                renderer.circle(0, 0, radius, 50);
        };
        gridRenderer = new GridRenderer(GameplayConstants.GRID_WIDTH, GameplayConstants.GRID_DOT_RADIUS);
    }

    public void initGame() {
        projectiles.clear();
        enemies.clear();
        asteroids.clear();
        projectileGroup.clear();
        enemyGroup.clear();
        asteroidGroup.clear();
        player.init();
        worldOffset.setZero();
        time = 0;
        score = 0;
    }

    @Override
    public void act(float delta) {
        if (gameOver() || isPaused()) return;

        super.act(delta);
        time += delta;
        GdxAI.getTimepiece().update(delta);
        asteroidSpawner.update(delta);
        enemySpawner.update(delta);
    }

    @Override
    public void draw() {
        super.draw();

        addShapeRenderRequest(squareBorder);
        addShapeRenderRequest(circleBorder);
        if (GridRenderer.gridOn) {
            addShapeRenderRequest(gridRenderer);
            gridRenderer.setOffset(worldOffset);
        }
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
        enemy.init();
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

    public void addPowerup(Powerup powerup) {
        powerup.moveBy(-worldOffset.x, -worldOffset.y);
        powerupGroup.addActor(powerup);
        powerup.setPlayer(player);
    }

    public void addExplosion(float x, float y, float radius) {
        x -= worldOffset.x; y -= worldOffset.y;
        explosionGroup.addActor(new Explosion(x, y, radius));
    }

    public void addExplosion(Explosion explosion) {
        explosion.moveBy(-worldOffset.x, -worldOffset.y);
        explosionGroup.addActor(explosion);
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

    public void incrementScore(int addition) {
        score += addition;
    }

    public void incrementNumCruisers(int addition) {
        numCruisers += addition;
    }

    public int getScore() {
        return score;
    }

    public boolean gameOver() {
        return player.currentHealth() <= 0;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void togglePaused() {
        setPaused(!isPaused());
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
            init();
        }

        void update(float delta) {
            if (timer < nextAsteroid) {
                timer += delta;
            }
            if (timer > nextAsteroid && asteroids.size() < GameplayConstants.MAX_ASTEROIDS_PRESENT
                    || asteroids.size() < GameplayConstants.MIN_ASTEROIDS_PRESENT) {
                Vector2 pos = new Vector2(1, 0);
                pos.setAngle(MathUtils.random(360f));
                pos.setLength(GameplayConstants.HORIZON * GameplayConstants.HORIZON_SPAWN_POINT_RATIO);
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

        void init() {
            timer = 0;
            setNext();
        }
    }

    private class EnemySpawner {

        int epoch;
        float timer;
        float nextEnemy;

        EnemySpawner() {
            init();
        }

        void update(float delta) {
            if (timer < nextEnemy) {
                timer += delta;
            }

            if (time > GameplayConstants.DIFFICULTY_EPOCHS[epoch]) {
                epoch++;
            }

            int maxEnemies = GameplayConstants.MAX_ENEMIES_BY_EPOCH[epoch];
            int minEnemies = GameplayConstants.MIN_ENEMIES_BY_EPOCH[epoch];
            if (timer > nextEnemy && enemies.size() < maxEnemies
                    || enemies.size() < minEnemies) {
                Vector2 pos = new Vector2(1, 0);
                pos.setAngle(MathUtils.random(360f));
                pos.setLength(GameplayConstants.HORIZON * 0.95f);
                Enemy enemy;
                if (numCruisers < GameplayConstants.MAX_CRUISERS_BY_EPOCH[epoch]) {
                    enemy = new EnemyCruiser(pos.x, pos.y, player);
                } else {
                    enemy = Enemy.getRandomEnemy(pos.x, pos.y, player, epoch);
                }
                addEnemy(enemy);
                setNext();
            }
        }

        void setNext() {
            nextEnemy = MathUtils.random(0.5f, 1.5f) * GameplayConstants.ENEMY_SPAWN_AVERAGE_BY_EPOCH[epoch];
            timer = 0;
        }

        void init() {
            timer = 0;
            epoch = 0;
            setNext();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 mousePos = screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Log.log("Mouse", mousePos.toString());
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
