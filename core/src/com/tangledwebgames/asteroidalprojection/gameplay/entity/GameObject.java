package com.tangledwebgames.asteroidalprojection.gameplay.entity;

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
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.geometry.Projection;
import com.tangledwebgames.asteroidalprojection.utility.Log;

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

    protected float timeSinceSpawn;

    protected SteeringBehavior<Vector2> behavior;
    protected Vector2 position;
    protected Vector2 projectedPosition;
    protected float projectedScale;
    protected  Vector2 linearVelocity;
    protected  float orientation;
    protected float angularVelocity;
    private float maxAngularAcceleration;
    private float maxAngularSpeed;
    private float maxLinearAcceleration;
    private float maxLinearSpeed;
    private float minLinearSpeed;
    private boolean tagged;
    protected boolean independentFacing = false;
    protected boolean independentScaling = true;
    protected boolean independentExistence = true;
    protected int updateFrame;

    protected final EntityType type;
    protected CollisionType collisionType;

    private TextureRegion texture;
    protected Animation<TextureRegion> animation;
    private boolean animated;



    public GameObject(float x, float y, float width, float height, EntityType type, CollisionType colType) {
        this.type = type;
        position = new Vector2();
        projectedPosition = new Vector2();
        linearVelocity = new Vector2(1, 0);
        angularVelocity = 0;
        orientation = 0;
        setSize(width, height);
        setPosition(x, y);
        updateFrame = 0;
        this.collisionType = colType;
        timeSinceSpawn = 0;
        animated = false;
        setTransform(false);
    }

    public GameObject(float x, float y, float radius, EntityType type, CollisionType colType) {
        this(x, y, radius * 2, radius * 2, type, colType);
    }

    public PlayStage getPlayStage() {
        return (PlayStage)getStage();
    }

    @Override
    public void act(float delta) {
        timeSinceSpawn += delta;

        if (independentExistence) {
            calculateVelocity(delta);
            applyMotion(delta);
            if (distanceFromOrigin() > GameplayConstants.HORIZON) {
                destroy();
                return;
            }
        } else {
            updatePositionVector();
            updateProjectedPosition();
        }

        update(delta);

        super.act(delta);
    }

    protected void update(float delta) {}

    protected void updatePositionVector() {
        position.set(localToStageCoordinates(new Vector2()));
        PlayStage stage = getPlayStage();
        if (stage != null) position.add(stage.getWorldOffset());
    }

    protected void updateProjectedPosition() {
        if (independentExistence) {
            projectedPosition.set(Projection.project(position));
            updateScale();
            return;
        }
        GameObject parent;
        try {
            parent = (GameObject) getParent();
        } catch (ClassCastException e) {
            Log.log(LOG_TAG, "Non-independent existence has no GameObject parent.", Log.LogLevel.DEBUG);
            return;
        }
        projectedPosition.set(parent.projectedPosition);
        Vector2 offset = new Vector2(getX() * getScaleX(), getY() * getScaleY());
        offset.rotate(parent.getRotation());
        projectedPosition.add(offset);
        updateScale();
    }

    protected void updateScale() {
        if (independentScaling) {
            projectedScale = Projection.getProjectedScale(distanceFromOrigin(), getRadius());
            setScale(projectedScale);
        } else {
            GameObject parent = (GameObject) getParent();
            projectedScale = parent.projectedScale;
            setScale(parent.getScaleX(), parent.getScaleY());
        }
    }

    protected void applyMotion(float delta) {
        moveBy(linearVelocity.x * delta, linearVelocity.y * delta);
        if (independentFacing) {
            adjustOrientation(angularVelocity * delta);
        }
    }

    public void calculateVelocity(float delta) {
        if (behavior == null) return;
        updateFrame = (updateFrame + 1) % 2;
        if (updateFrame == 0) return;

        Vector2 accel = behavior.calculateSteering(GameObject.accel).linear;
        accel = accel.nor();
        float desiredOrientation = accel.angleRad();
        float desiredOrientationChange = desiredOrientation - orientation;
        //If the desired change in angle is greater than 180 degrees or less than -180 degrees,
        //we adjust so that it is between -180 and 180.  For example, a desired rotation of 300
        //degrees will change to -60.
        while (desiredOrientationChange > MathUtils.PI) {
            desiredOrientationChange -= 2 * MathUtils.PI;
        }
        while (desiredOrientationChange < - MathUtils.PI) {
            desiredOrientationChange += 2 * MathUtils.PI;
        }
        float angularAccel = desiredOrientationChange > 0 ?
                maxAngularAcceleration : -maxAngularAcceleration;
        angularVelocity = angularVelocity + angularAccel * delta;
        angularVelocity = MathUtils.clamp(angularVelocity, -maxAngularSpeed, maxAngularSpeed);

        //Check whether there is sufficient angular velocity to reach desired orientation.
        float angleChange = angularVelocity * delta;
        boolean sufficientAngularVel = false;
        if (desiredOrientationChange > 0 && angleChange > desiredOrientationChange ||
                desiredOrientationChange < 0 && angleChange < desiredOrientationChange) {
            sufficientAngularVel = true;
        }

        if (independentFacing) {
            //If it is independently facing, simply accelerate in the indicated direction.
            linearVelocity.mulAdd(accel, maxLinearAcceleration * delta).limit(maxLinearSpeed);
            if (sufficientAngularVel) {
                setOrientation(desiredOrientation);
                angularVelocity = 0;
            }
        } else {
            //If not independently facing, rotate linear velocity by angular velocity.
            if (sufficientAngularVel) {
                linearVelocity.setAngleRad(desiredOrientation);
                angularVelocity = 0;
            } else {
                linearVelocity.setAngleRad(orientation + angleChange);
                //If facing near desired heading, accelerate; if facing away from desired heading,
                //de-accelerate to make a sharper turn.
                if (Math.abs(desiredOrientationChange) < MathUtils.PI / 2) {
                    accelerate(maxLinearAcceleration * delta);
                } else {
                    accelerate(-maxLinearAcceleration * delta);
                }
            }
            //Always orient in direction of velocity.
            setOrientation(linearVelocity.angleRad());
        }
    }

    public void accelerate(float accel) {
        float newSpeed = linearVelocity.len() + accel;
        newSpeed = MathUtils.clamp(newSpeed, minLinearSpeed, maxLinearSpeed);
        linearVelocity.setLength(newSpeed);
    }

    @Override
    public void moveBy(float x, float y) {
        super.moveBy(x, y);
        updatePositionVector();
        updateProjectedPosition();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updatePositionVector();
        updateProjectedPosition();
    }

    public void setRadius(float width) {
        setSize(width * 2, width * 2);
    }

    public float getRadius() {
        return Math.max(getWidth(), getHeight()) / 2;
    }

    @Override
    public float getBoundingRadius() {
        return getRadius();
    }

    public float getProjectedRadius() {
        return getRadius() * projectedScale;
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

    protected float getProjectedRotation() {
        return getRotation();
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
        return position.len();
    }

    public float projectedDistance(GameObject object) {
        return projectedPosition.dst(object.projectedPosition);
    }

    public float projectedDistance2(GameObject object) {
        return projectedPosition.dst2(object.projectedPosition);
    }

    public float projectedDistanceFromOrigin() {
        return projectedPosition.len();
    }

    public void destroy(boolean removeFromCollection) {
        Log.log(LOG_TAG, "Removing entity of type " + type.toString() + " at position " + getPosition().toString());
        if (removeFromCollection) {
            getPlayStage().removeObject(this);
        }
        remove();
    }

    public void destroy() {
        this.destroy(true);
    }

    public boolean collidesWith(GameObject object) {
        if (projectedDistance(object) > getProjectedRadius() + object.getProjectedRadius())
            return false;
        switch (collisionType) {
            case CIRCLE:
                switch (object.collisionType) {
                    case CIRCLE:
                        return Intersector.overlaps(
                                getCircle(), object.getCircle()
                        );
                    case RECTANGLE:
                        return Intersector.overlapConvexPolygons(
                                getHexagon(), object.getRectangle()
                        );
                    case POINT:
                        return getCircle().contains(object.projectedPosition);
                }
            case RECTANGLE:
                switch (object.collisionType) {
                    case CIRCLE:
                        return Intersector.overlapConvexPolygons(
                                getRectangle(), object.getHexagon()
                        );
                    case RECTANGLE:
                        return Intersector.overlapConvexPolygons(
                                getRectangle(), object.getRectangle()
                        );
                    case POINT:
                        return getRectangle().contains(object.projectedPosition);
                }
            case POINT:
                switch (object.collisionType) {
                    case CIRCLE:
                        return object.getCircle().contains(projectedPosition);
                    case RECTANGLE:
                        return object.getRectangle().contains(projectedPosition);
                    case POINT:
                        return false;
                }
        }
        return false;
    }

    protected Circle getCircle() {
        return new Circle(projectedPosition, getProjectedRadius());
    }

    protected Polygon getRectangle() {
        float w = getWidth() * projectedScale / 2;
        float h = getHeight() * projectedScale / 2;
        Vector2 p1 = new Vector2(w, h);
        Vector2 p2 = new Vector2(w, -h);
        Vector2 p3 = new Vector2(-w, -h);
        Vector2 p4 = new Vector2(-w, h);
        Polygon rect = new Polygon(new float[]{
                p1.x, p1.y,
                p2.x, p2.y,
                p3.x, p3.y,
                p4.x, p4.y
        });
        rect.translate(projectedPosition.x, projectedPosition.y);
        rect.rotate(getRotation());
        return rect;
    }

    protected Polygon getHexagon() {
        float r = getProjectedRadius();
        float rot = MathUtils.PI / 3f;
        float[] vertices = new float[12];
        for (int i = 0; i < 6; i++) {
            Vector2 p = new Vector2(r, 0);
            p.rotateRad(rot * i);
            vertices[2 * i] = p.x;
            vertices[2 * i + 1] = p.y;
        }
        Polygon hex = new Polygon(vertices);
        hex.translate(projectedPosition.x, projectedPosition.y);
        return hex;
    }

    public boolean reportHit(Vector2 hitDirection) {
        return false;
    }

    /**
     * Rendering
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (projectedPosition == null) return;

        if (getTexture() != null) {
            float width = getWidth() * getScaleX();
            float height = getHeight() * getScaleY();
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
