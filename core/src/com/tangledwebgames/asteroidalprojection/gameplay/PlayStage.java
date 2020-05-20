package com.tangledwebgames.asteroidalprojection.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tangledwebgames.asteroidalprojection.gameplay.enemybehavior.AsteroidCollisionDetector;
import com.tangledwebgames.asteroidalprojection.gameplay.entity.*;
import com.tangledwebgames.asteroidalprojection.gameplay.geometry.GridRenderer;
import com.tangledwebgames.asteroidalprojection.utility.Log;
import com.tangledwebgames.asteroidalprojection.utility.ShapeRenderRequest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class PlayStage extends Stage {

    int score;
    float time;
    boolean paused;
    boolean gameOver;

    Player player;
    AsteroidSpawner asteroidSpawner;
    EnemySpawner enemySpawner;

    Set<Projectile> projectiles;
    Set<Asteroid> asteroids;
    Set<Enemy> enemies;
    Group worldGroup;
    Group asteroidGroup;
    Group projectileGroup;
    Group enemyGroup;
    Group powerupGroup;
    Group explosionGroup;
    int numCruisers;

    ShapeRenderer shapeRenderer;
    Queue<ShapeRenderRequest> postRenderRequestQueue;
    Queue<ShapeRenderRequest> preRenderRequestQueue;

    ShapeRenderRequest squareBorder;
    ShapeRenderRequest circleBorder;
    GridRenderer gridRenderer;

    public PlayStage(Viewport viewport) {
        super(viewport);
        projectiles = new HashSet<>();
        asteroids = new HashSet<>();
        enemies = new HashSet<>();
        worldGroup = new Group();
        asteroidGroup = new Group();
        AsteroidCollisionDetector.setAsteroids(asteroids);
        projectileGroup = new Group();
        enemyGroup = new Group();
        powerupGroup = new Group();
        explosionGroup = new Group();
        player = new Player();
        worldGroup.addActor(enemyGroup);
        worldGroup.addActor(asteroidGroup);
        worldGroup.addActor(projectileGroup);
        worldGroup.addActor(powerupGroup);
        worldGroup.addActor(explosionGroup);
        worldGroup.setTransform(false);
        asteroidGroup.setTransform(false);
        enemyGroup.setTransform(false);
        explosionGroup.setTransform(false);
        powerupGroup.setTransform(false);
        projectileGroup.setTransform(false);
        asteroidSpawner = new AsteroidSpawner();
        enemySpawner = new EnemySpawner();
        shapeRenderer = new ShapeRenderer();
        postRenderRequestQueue = new LinkedList<>();
        preRenderRequestQueue = new LinkedList<>();
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
        gridRenderer = new GridRenderer(
                GameplayConstants.GRID_WIDTH,
                GameplayConstants.GRID_DOT_RADIUS,
                GameplayConstants.GRID_COLOR
        );
    }

    public void initGame(boolean demoScreen) {
        clear();
        projectiles.clear();
        enemies.clear();
        asteroids.clear();
        projectileGroup.clear();
        enemyGroup.clear();
        asteroidGroup.clear();
        explosionGroup.clear();
        addActor(player);
        addActor(worldGroup);
        worldGroup.setPosition(0, 0);
        time = 0;
        score = 0;
        numCruisers = 0;
        gameOver = false;
        if (demoScreen) {
            player.setActive(false);
            enemySpawner.active = false;
        } else {
            player.init();
            player.setActive(true);
            enemySpawner.active = true;
        }
    }

    public void initGame() {
        this.initGame(false);
    }

    @Override
    public void act(float delta) {
        if (isGameOver() || isPaused()) return;

        super.act(delta);
        time += delta;
        GdxAI.getTimepiece().update(delta);
        asteroidSpawner.update(delta);
        enemySpawner.update(delta);
    }

    @Override
    public void draw() {
        if (GridRenderer.gridOn) {
            addShapeRenderRequest(gridRenderer, false);
            gridRenderer.setOffset(getWorldOffset());
        }
        addShapeRenderRequest(squareBorder, true);
        addShapeRenderRequest(circleBorder, true);
        shapeRenderer.setProjectionMatrix(getViewport().getCamera().combined);
        shapeRenderer.setAutoShapeType(true);

        //Pre-bitmap rendering shape rendering
        shapeRenderer.begin();
        while (!preRenderRequestQueue.isEmpty()) {
            preRenderRequestQueue.poll().draw(shapeRenderer);
        }
        shapeRenderer.end();

        //All bitmap rendering
        super.draw();

        //Post-bitmap rendering shape rendering
        shapeRenderer.begin();
        while (!postRenderRequestQueue.isEmpty()) {
            postRenderRequestQueue.poll().draw(shapeRenderer);
        }
        shapeRenderer.end();
    }

    public Vector2 getWorldOffset() {
        return new Vector2(getWorldOffsetX(), getWorldOffsetY());
    }

    public float getWorldOffsetX() {
        return worldGroup.getX();
    }

    public float getWorldOffsetY() {
        return worldGroup.getY();
    }

    public void moveWorld(Vector2 motion) {
        moveWorld(motion.x, motion.y);
    }

    public void moveWorld(float x, float y) {
        worldGroup.moveBy(x, y);
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
        enemyGroup.addActor(enemy);
        enemy.moveBy(-getWorldOffsetX(), -getWorldOffsetY());
        enemy.init();
    }

    public void addAsteroid(Asteroid asteroid) {
        asteroids.add(asteroid);
        asteroidGroup.addActor(asteroid);
        asteroid.moveBy(-getWorldOffsetX(), -getWorldOffsetY());
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
        projectileGroup.addActor(projectile);
        projectile.moveBy(-getWorldOffsetX(), -getWorldOffsetY());
    }

    public void addPowerup(Powerup powerup) {
        powerupGroup.addActor(powerup);
        powerup.setPlayer(player);
        powerup.moveBy(-getWorldOffsetX(), -getWorldOffsetY());

    }

    public void addExplosion(float x, float y, float radius) {
        addExplosion(new Explosion(x, y, radius));
    }

    public void addExplosion(Explosion explosion) {
        explosionGroup.addActor(explosion);
        explosion.moveBy(-getWorldOffsetX(), -getWorldOffsetY());
    }

    public void addShapeRenderRequest(ShapeRenderRequest request) {
        addShapeRenderRequest(request, true);
    }

    public void addShapeRenderRequest(ShapeRenderRequest request, boolean postRender) {
        if (postRender) postRenderRequestQueue.add(request);
        else preRenderRequestQueue.add(request);
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

    public boolean isGameOver() {
        return gameOver;
    }

    public void gameOver() {
        gameOver = true;
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
        boolean active;

        AsteroidSpawner() {
            init();
        }

        void update(float delta) {
            if (!active) return;

            if (timer < nextAsteroid) {
                timer += delta;
            }
            if (timer > nextAsteroid && asteroids.size() < GameplayConstants.MAX_ASTEROIDS_PRESENT
                    || asteroids.size() < GameplayConstants.MIN_ASTEROIDS_PRESENT) {
                Vector2 pos = new Vector2(1, 0);
                Vector2 centerOfMass = getAsteroidCenterOfMass();
                if (centerOfMass.len2() > GameplayConstants.ASTEROID_CENTER_OF_MASS_CUTOFF2) {
                    pos.setAngle(centerOfMass.angle() - 180 + MathUtils.random(-45, 45));
                } else {
                    pos.setAngle(MathUtils.random(360));
                }
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
            active = true;
            setNext();
        }

        Vector2 getAsteroidCenterOfMass() {
            Vector2 center = new Vector2();
            for (Asteroid asteroid : asteroids) {
                center.x += asteroid.getPosition().x;
                center.y += asteroid.getPosition().y;
            }
            center.x = center.x / asteroids.size();
            center.y = center.y / asteroids.size();
            return center;
        }
    }

    private class EnemySpawner {

        int epoch;
        float timer;
        float nextEnemy;
        boolean active;

        EnemySpawner() {
            init();
        }

        void update(float delta) {
            if (!active) return;

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
            active = true;
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
