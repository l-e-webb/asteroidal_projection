package com.louiswebbgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.louiswebbgames.asteroidalprojection.gameplay.GameplayConstants;
import com.louiswebbgames.asteroidalprojection.gameplay.PlayStage;
import com.louiswebbgames.asteroidalprojection.gameplay.geometry.Projection;
import com.louiswebbgames.asteroidalprojection.utility.Log;
import com.louiswebbgames.asteroidalprojection.utility.ShapeRenderRequest;

/**
 * Abstract superclass for all objects in the game, implements most Steerable
 * interface methods and some scene2d Actor methods.  Defines each object
 * as having radius, position, and velocity properties.
 */
public abstract class GameObject extends Group implements Steerable<Vector2> {

    static boolean debug = false;

    public static final String LOG_TAG = GameObject.class.getSimpleName();

    protected static SteeringAcceleration<Vector2> accel =
            new SteeringAcceleration<Vector2>(new Vector2(0,0));

    float timeSinceSpawn;
    SteeringBehavior<Vector2> behavior;
    Vector2 position;
    Vector2 linearVelocity;
    float orientation;
    float angularVelocity;
    float maxAngularAcceleration;
    float maxAngularSpeed;
    float maxLinearAcceleration;
    float maxLinearSpeed;
    float minLinearSpeed;
    boolean tagged;
    final EntityType type;
    CollisionType collisionType;
    int updateFrame;

    boolean animated;

    TextureRegion texture;
    Animation<TextureRegion> animation;

    boolean independentFacing = false;
    boolean independentScaling = true;
    boolean independentExistence = true;

    public GameObject(float x, float y, float width, float height, EntityType type, CollisionType colType) {
        linearVelocity = new Vector2(1, 0);
        angularVelocity = 0;
        orientation = 0;
        position = new Vector2();
        setSize(width, height);
        setPosition(x, y);
        this.type = type;
        updateFrame = 0;
        updateScale();
        this.collisionType = colType;
        timeSinceSpawn = 0;
        animated = false;
        setTransform(false);
    }

    public GameObject(float x, float y, float radius, EntityType type, CollisionType colType) {
        this(x, y, radius * 2, radius * 2, type, colType);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        timeSinceSpawn += delta;

        updatePositionVector();

        if (independentExistence && distanceFromOrigin() > GameplayConstants.HORIZON) {
            destroy();
            return;
        }
        update(delta);

        if (independentExistence) {
            calculateVelocity(delta);
            moveBy(linearVelocity.x * delta, linearVelocity.y * delta);
            if (independentFacing) {
                adjustOrientation(angularVelocity * delta);
            }
        }

        updateScale();
    }

    public void calculateVelocity(float delta) {
        if (behavior == null) return;
        updateFrame = (updateFrame + 1) % 2;
        if (updateFrame == 0) return;
        Vector2 accel = behavior.calculateSteering(GameObject.accel).linear;
        float desiredOrientation = accel.angleRad();
        float desiredAngularVel = desiredOrientation - orientation;
        //If the desired change in angle is greater than 180 degrees or less than -180 degrees,
        //we adjust so that it is between -180 and 180.  For example, a desired rotation of 300
        //degrees will change to -60.
        while (desiredAngularVel > MathUtils.PI) {
            desiredAngularVel -= 2 * MathUtils.PI;
        }
        while (desiredAngularVel < - MathUtils.PI) {
            desiredAngularVel += 2 * MathUtils.PI;
        }
        float desiredAngularAccel = desiredAngularVel - angularVelocity;
        float angularAccel;
        if (desiredAngularAccel > maxAngularAcceleration) {
            angularAccel = maxAngularAcceleration;
        } else if (-desiredAngularAccel < -maxAngularAcceleration) {
            angularAccel = -maxAngularAcceleration;
        } else {
            angularAccel = desiredAngularAccel;
        }
        float newAngularVel = angularVelocity + angularAccel;
        if (newAngularVel > maxAngularSpeed) {
            newAngularVel = maxAngularSpeed;
        } else if (-newAngularVel < -maxAngularSpeed) {
            newAngularVel = -maxAngularSpeed;
        }
        angularVelocity = newAngularVel;
        if (independentFacing) {
            //If it is independently facing, simply accelerate in the indicated direction.
            linearVelocity.mulAdd(accel, maxLinearAcceleration * delta).limit(maxLinearSpeed);
        } else {
            //If not independently facing, rotate linear velocity by angular velocity.
            float currentOrientation = linearVelocity.angleRad();
            linearVelocity.setAngleRad(currentOrientation + angularVelocity * delta);
            //Always orient in direction of velocity.
            setOrientation(linearVelocity.angleRad());
            //If facing near desired heading, accelerate; if facing away from desired heading,
            //de-accelerate to make a sharper turn.
            if (Math.abs(currentOrientation - desiredOrientation) < MathUtils.PI / 2) {
                accelerate(maxLinearAcceleration * delta);
            } else {
                accelerate(-maxLinearAcceleration * delta);
            }
        }
    }

    public void updateScale() {
        if (independentScaling) {
            setScale(
                    (1 / (distanceFromOrigin() + 1))
            );
        } else {
            setScale(getParent().getScaleX(), getParent().getScaleY());
        }
    }

    public void accelerate(float accel) {
        linearVelocity.setLength(linearVelocity.len() + accel).limit(maxLinearSpeed);
        if (linearVelocity.len() < minLinearSpeed) {
            linearVelocity.setLength(minLinearSpeed);
        }
    }

    public void update(float delta) {}

    public void destroy(boolean removeFromCollection) {
        Log.log(LOG_TAG, "Removing entity of type " + type.toString() + " at position " + getPosition().toString());
        if (removeFromCollection) {
            ((PlayStage)getStage()).removeObject(this);
        }
        remove();
    }

    public void destroy() {
        this.destroy(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (getTexture() == null) return;

        Vector2 projectedPosition = getProjectedPosition();
        if (projectedPosition == null) return;
        float xOffset = getWidth() * getScaleX() / 2;
        float yOffset = getHeight() * getScaleY() / 2;
        batch.draw(
                getTexture(),
                projectedPosition.x - xOffset,
                projectedPosition.y - yOffset,
                xOffset,
                yOffset,
                getWidth() * getScaleX(),
                getHeight() * getScaleY(),
                1,
                1,
                getRotation()
        );

        super.draw(batch, parentAlpha);

        if (debug) {
            ((PlayStage) getStage()).addShapeRenderRequest(
                    new ShapeRenderRequest() {
                        @Override
                        public void draw(ShapeRenderer renderer) {
                            Vector2 projectedPos = getProjectedPosition();
                            renderer.set(ShapeRenderer.ShapeType.Line);
                            renderer.setColor(Color.WHITE);
                            renderer.circle(
                                    projectedPos.x,
                                    projectedPos.y,
                                    getBoundingRadius() * getScaleX(),
                                    7
                            );
                        }
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

    public Vector2 getProjectedPosition() {
        if (independentExistence) {
            return Projection.project(position);
        }
        GameObject parent;
        try {
            parent = (GameObject) getParent();
        } catch (ClassCastException e) {
            Log.log(LOG_TAG, "Non-independent existence has no GameObject parent.", Log.LogLevel.DEBUG);
            return null;
        }
        return new Vector2(parent.getProjectedPosition()).add(
                new Vector2(getX() * getScaleX(), getY() * getScaleY()).rotate(parent.getRotation())
        );
    }

    public TextureRegion getTexture() {
        if (animated && animation != null) {
            return animation.getKeyFrame(timeSinceSpawn);
        } else {
            return texture;
        }
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
        animated = false;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        animated = true;
    }

    @Override
    public void moveBy(float x, float y) {
        super.moveBy(x, y);
        updatePositionVector();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updatePositionVector();
    }

    public void updatePositionVector() {
        position = localToStageCoordinates(new Vector2());
        PlayStage stage = (PlayStage) getStage();
        if (stage != null) position.add(stage.getWorldOffset());
    }

    public void setRadius(float width) {
        setSize(width * 2, width * 2);
    }

    @Override
    public float getBoundingRadius() {
        return getWidth() / 2;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public void adjustOrientation(float angle) {
        setOrientation(orientation + angle);
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    public float getHeadingAngle() {
        if (linearVelocity.len() > getZeroLinearSpeedThreshold()) {
            return linearVelocity.angleRad();
        }
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation % (2 * MathUtils.PI);
        setRotation(orientation * MathUtils.radiansToDegrees - 90);
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    public float getMinLinearSpeed() {
        return minLinearSpeed;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return GameplayConstants.ZERO_SPEED_THRESHOLD;
    }

    @Override
    public void setMaxAngularAcceleration(float accel) {
        maxAngularAcceleration = accel;
    }

    @Override
    public void setMaxAngularSpeed(float speed) {
        maxAngularSpeed = speed;
    }

    @Override
    public void setMaxLinearAcceleration(float accel) {
        maxLinearAcceleration = accel;
    }

    @Override
    public void setMaxLinearSpeed(float speed) {
        maxLinearSpeed = speed;
    }

    public void setMinLinearSpeed(float speed) {
        minLinearSpeed = speed;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float threshold) {}

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = MathUtils.cos(angle);
        outVector.y = MathUtils.sin(angle);
        return outVector;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return vector.angleRad();
    }

    public EntityType getType() {
        return type;
    }

    public float distance(GameObject object) {
        return position.dst(object.position);
    }

    public float distance2(GameObject object) {
        return position.dst2(object.position);
    }

    public float distanceFromOrigin() {
        return Vector2.Zero.dst(position);
    }

    public boolean collidesWith(GameObject object) {
        if (distance(object) > getBoundingRadius() + object.getBoundingRadius()) return false;
        switch (collisionType) {
            case CIRCLE:
                switch (object.collisionType) {
                    case CIRCLE:
                        return Intersector.overlaps(
                                getCircle(), object.getCircle()
                        );
                    case POINT:
                        return getCircle().contains(object.getPosition());
                }
            case POINT:
                switch (object.collisionType) {
                    case CIRCLE:
                        return object.getCircle().contains(position);
                    case POINT:
                        return false;
                }
        }
        return false;
    }

    public Circle getCircle() {
        return new Circle(position, getBoundingRadius());
    }

    public boolean reportHit(Vector2 hitDirection) {
        return false;
    }

}
