package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.geometry.Projection;
import com.tangledwebgames.asteroidalprojection.utility.Log;

public class GameObject extends Group {

    private static final String LOG_TAG = GameObject.class.getSimpleName();
    static boolean debug = false;

    // State
    protected final EntityType type;
    protected float timeSinceSpawn;
    protected Vector2 position;
    protected Vector2 projectedPosition;
    protected float projectedScaleFactor;
    protected boolean independentScaling = true;
    protected boolean independentExistence = true;
    protected boolean independentRotation = true;

    // Rendering
    private TextureRegion texture;
    protected Animation<TextureRegion> animation;
    private boolean animated;

    public GameObject(float x, float y, float width, float height, EntityType type) {
        super();
        this.type = type;
        position = new Vector2();
        projectedPosition = new Vector2();
        setSize(width, height);
        setPosition(x, y);
        timeSinceSpawn = 0;
        animated = false;
        setTransform(false);
    }

    public GameObject(float x, float y, float radius, EntityType type) {
        this(x, y, radius * 2, radius * 2, type);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timeSinceSpawn += delta;
    }

    public EntityType getType() {
        return type;
    }

    public PlayStage getPlayStage() {
        return (PlayStage)getStage();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getProjectedPosition() {
        return projectedPosition;
    }

    public float distanceFromOrigin() {
        return getPosition().len();
    }

    public float projectedDistanceFromOrigin() {
        return getProjectedPosition().len();
    }

    public float distance(GameObject object) {
        return position.dst(object.position);
    }

    public float distance2(GameObject object) {
        return position.dst2(object.position);
    }

    public float projectedDistance(GameObject object) {
        return projectedPosition.dst(object.projectedPosition);
    }

    public float projectedDistance2(GameObject object) {
        return projectedPosition.dst2(object.projectedPosition);
    }

    public float getRadius() {
        return Math.max(getWidth(), getHeight()) / 2;
    }

    public float getProjectedRadius() {
        return getRadius() * projectedScaleFactor;
    }

    @Override
    public float getRotation() {
        float rot = super.getRotation();
        if (!independentRotation) {
            rot += getParent().getRotation();
        }
        return rot;
    }

    public float getProjectedRotation() {
        return getRotation();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        updatePosition();
        updateProjectedPosition();
        SnapshotArray<Actor> children = getChildren();
        for (Actor actor: children) {
            GameObject object;
            try {
                object = (GameObject) actor;
                object.updatePosition();
                object.updateProjectedPosition();
            } catch (ClassCastException e) {
                Log.log(LOG_TAG, "Non-GameObject actor found as child of GameObject", Log.LogLevel.DEBUG);
            }
        }
    }

    protected void updatePosition() {
        position.set(localToStageCoordinates(new Vector2()));
    }

    protected void updateProjectedPosition() {
        if (independentExistence) {
            projectedPosition.set(Projection.project(position));
            projectedScaleFactor = Projection.getProjectedScale(distanceFromOrigin(), getRadius());
            return;
        }
        GameObject parent;
        try {
            parent = (GameObject) getParent();
        } catch (ClassCastException e) {
            Log.log(LOG_TAG, "Non-independent existence has no GameObject parent.", Log.LogLevel.DEBUG);
            return;
        }
        projectedScaleFactor = parent.projectedScaleFactor;
        projectedPosition.set(localToParentCoordinates(new Vector2()))
                .scl(projectedScaleFactor)
                .rotate(parent.getRotation())
                .add(parent.projectedPosition);
    }

    public void destroy(boolean removeFromCollection) {
        if (debug) {
            Log.log(LOG_TAG, "Removing entity of type " + type.toString() + " at position " + getPosition().toString());
        }
        if (removeFromCollection) {
            getPlayStage().removeObject(this);
        }
        remove();
    }

    public void destroy() {
        this.destroy(true);
    }

    /**
     * Rendering
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (projectedPosition == null) return;

        if (getTexture() != null) {
            float width = getWidth() * projectedScaleFactor;
            float height = getHeight() * projectedScaleFactor;
            float xOffset = width / 2;
            float yOffset = height / 2;
            batch.draw(
                    getTexture(),
                    projectedPosition.x - xOffset,
                    projectedPosition.y - yOffset,
                    xOffset,
                    yOffset,
                    width,
                    height,
                    1,
                    1,
                    getProjectedRotation()
            );
        }

        super.draw(batch, parentAlpha);

        if (debug) {
            getPlayStage().addShapeRenderRequest(
                    renderer -> {
                        renderer.set(ShapeRenderer.ShapeType.Line);
                        renderer.setColor(Color.WHITE);
                        renderer.circle(
                                projectedPosition.x,
                                projectedPosition.y,
                                getProjectedRadius(),
                                7
                        );
                    }
            );
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        //Custom implementation to avoid conflicts with projection.
        //Removes culling transform, and translation (all not used).
        parentAlpha *= getColor().a;
        SnapshotArray<Actor> children = getChildren();
        Actor[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            Actor child = actors[i];
            if (!child.isVisible()) continue;
            child.draw(batch, parentAlpha);
        }
        children.end();
    }

    protected TextureRegion getTexture() {
        if (animated && animation != null) {
            return animation.getKeyFrame(timeSinceSpawn);
        } else {
            return texture;
        }
    }

    protected void setTexture(TextureRegion texture) {
        this.texture = texture;
        animated = false;
    }

    protected void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        animated = true;
    }

}
