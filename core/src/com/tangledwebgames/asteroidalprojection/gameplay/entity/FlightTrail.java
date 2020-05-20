package com.tangledwebgames.asteroidalprojection.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tangledwebgames.asteroidalprojection.gameplay.PlayStage;
import com.tangledwebgames.asteroidalprojection.gameplay.geometry.Projection;
import com.tangledwebgames.asteroidalprojection.utility.ShapeRenderRequest;

import java.util.LinkedList;
import java.util.Queue;

public class FlightTrail extends Actor {

    private static final int NUM_NODES_DEFAULT = 10;
    private static final int NODE_FRAME_GAP_DEFAULT = 6;

    private GameObject target;
    private Vector2 offset;
    private int nodeFrameGap;
    private int numNodes;
    private Color color;
    private float alphaStart;
    private float alphaEnd;

    private LinkedList<Vector2> nodes;
    private Color[] colors;
    private int currentFrame;
    private FlightTrailRequest renderRequest;

    public FlightTrail(Color color, float alphaStart, float alphaEnd) {
        this.color = color;
        this.alphaStart = alphaStart;
        this.alphaEnd = alphaEnd;
        nodes = new LinkedList<>();
        setNodes(NUM_NODES_DEFAULT, NODE_FRAME_GAP_DEFAULT);
        renderRequest = new FlightTrailRequest();
    }

    public FlightTrail(Color color, float alphaStart) {
        this(color, alphaStart, 0);
    }

    public FlightTrail(Color color) {
        this(color, 1);
    }

    public void setNodes(int numNodes, int nodeFrameGap) {
        this.numNodes = numNodes;
        this.nodeFrameGap = nodeFrameGap;
        init();
    }

    public void setTarget(GameObject object, Vector2 offset) {
        this.target = object;
        this.offset = offset;
        init();
    }

    public void setTarget(GameObject object, float xOffset, float yOffset) {
        setTarget(object, new Vector2(xOffset, yOffset));
    }

    public void setTarget(GameObject object) {
        setTarget(object, 0, 0);
    }

    private PlayStage getPlayStage() {
        return (PlayStage)getStage();
    }

    private void init() {
        nodes.clear();
        colors = new Color[numNodes + 1];
        for (int i = 0; i < colors.length; i++) {
            float alpha = MathUtils.lerp(alphaEnd, alphaStart, i * 1.0f / (colors.length - 1));
            colors[i] = new Color(color.r, color.g, color.b, alpha);
        }
        currentFrame = -1;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        currentFrame = (currentFrame + 1) % nodeFrameGap;
        if (currentFrame != 0) return;
        if (!target.hasParent()) {
            if (nodes.isEmpty()) {
                remove();
            } else {
                nodes.remove();
            }
            return;
        }

        addNewNode();
    }

    protected void addNewNode() {
        Vector2 node;
        if (nodes.size() == numNodes) {
            node = nodes.remove();
        } else {
            node = new Vector2();
        }
        node.set(offset)
                .rotate(target.getRotation())
                .add(target.position)
                .sub(getPlayStage().getWorldOffset());
        nodes.add(node);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (nodes.size() < 2) return;
        getPlayStage().addShapeRenderRequest(renderRequest, false);
    }

    private class FlightTrailRequest implements ShapeRenderRequest {
        @Override
        public void draw(ShapeRenderer renderer) {
            if (nodes.size() < 2) return;
            Vector2 worldOffset = getPlayStage().getWorldOffset();
            Vector2 node1 = new Vector2();
            Vector2 node2 = new Vector2();
            Color color1;
            Color color2 = null;
            renderer.set(ShapeRenderer.ShapeType.Line);
            for (int i = 0; i < nodes.size() - 1; i++) {
                node1 = Projection.project(node1.set(nodes.get(i)).add(worldOffset));
                node2 = Projection.project(node2.set(nodes.get(i + 1)).add(worldOffset));
                color1 = colors[i];
                color2 = colors[i + 1];
                drawSegment(renderer, node1, node2, color1, color2);
            }
            if (target.hasParent()) {
                // Set node 1 to be the most recent node2, i.e. the last in the chain
                node1 = node2;
                node2 = target.projectedPosition;
                color1 = color2;
                color2 = colors[nodes.size()];
                drawSegment(renderer, node1, node2, color1, color2);
            }
        }

        private void drawSegment(ShapeRenderer renderer, Vector2 node1, Vector2 node2, Color color1, Color color2) {
            renderer.line(node1.x, node1.y, node2.x, node2.y, color1, color2);
        }
    }
}
