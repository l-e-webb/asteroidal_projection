package com.tangledwebgames.asteroidalprojection.gameplay.geometry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tangledwebgames.asteroidalprojection.gameplay.GameplayConstants;
import com.tangledwebgames.asteroidalprojection.utility.ShapeRenderRequest;

public class GridRenderer implements ShapeRenderRequest {

    public static boolean gridOn = true;
    public static boolean drawLines = true;

    Color color;
    float gridWidth;
    float dotRadius;
    Vector2 offset;

    private float[][] junctions;

    public GridRenderer(float gridWidth, float dotRadius, Color color) {
        this.gridWidth = gridWidth;
        this.dotRadius = dotRadius;
        this.color = color;
        offset = new Vector2();
        initJunctionsGrid();
    }

    @Override
    public void draw(ShapeRenderer renderer) {
        renderer.setColor(color);
        if (drawLines) drawLineGrid(renderer); else drawDotGrid(renderer);
    }

    private void drawLineGrid(ShapeRenderer renderer) {
        int junctionsPerRow = junctions.length;
        Vector2 p1 = new Vector2();
        Vector2 p2 = new Vector2();
        Vector2 p3 = new Vector2();
        Vector2 p4 = new Vector2();
        for (int i = 0; i < junctionsPerRow - 1; i++) {
            for (int j = 0; j < junctionsPerRow - 1; j++) {
                p1.set(junctions[i][2 * j], junctions[i][2 * j + 1]);
                p2.set(junctions[i][2 * j + 2], junctions[i][2 * j + 3]);
                p3.set(junctions[i + 1][2 * j], junctions[i + 1][2 * j + 1]);
                p4.set(p2.x, p3.y);
                p1.add(offset);
                p2.add(offset);
                p3.add(offset);
                p4.add(offset);
                float distance = Math.min(p1.len2(), p2.len2());
                distance = Math.min(distance, p3.len2());
                distance = Math.min(distance, p4.len2());
                if (distance < GameplayConstants.THREE_LINE_GRID_HORIZON2) {
                    threeLineGridSegment(renderer, p1, p2, p3);
                } else if (distance < GameplayConstants.TWO_LINE_GRID_HORIZON2) {
                    twoLineGridSegment(renderer, p1, p2, p3);
                } else {
                    oneLineGridSegment(renderer, p1, p2, p3);
                }
            }
        }
    }

    private void threeLineGridSegment(ShapeRenderer renderer, Vector2 p1, Vector2 p2, Vector2 p3) {
        nLineGridSegment(renderer, 3, p1, p2, p3);
    }

    private void twoLineGridSegment(ShapeRenderer renderer, Vector2 p1, Vector2 p2, Vector2 p3) {
        nLineGridSegment(renderer, 2, p1, p2, p3);
    }

    private void nLineGridSegment(ShapeRenderer renderer, int n, Vector2 p1, Vector2 p2, Vector2 p3) {
        Vector2[] segment = new Vector2[n + 1];

        float l = (p2.x - p1.x) / n;
        segment[0] = Projection.project(p1);
        segment[n] = Projection.project(p2);
        for (int i = 1; i < n; i++) {
            segment[i] = Projection.project(p1.x + i * l, p1.y);
        }
        for (int i = 0; i < n; i++) {
            renderer.line(segment[i], segment[i + 1]);
        }

        l = (p3.y - p1.y) / n;
        segment[n] = Projection.project(p3);
        for (int i = 1; i < n; i++) {
            segment[i] = Projection.project(p1.x, p1.y + i * l);
        }
        for (int i = 0; i < n; i++) {
            renderer.line(segment[i], segment[i + 1]);
        }
    }

    private void oneLineGridSegment(ShapeRenderer renderer, Vector2 p1, Vector2 p2, Vector2 p3) {
        p1.set(Projection.project(p1));
        p2.set(Projection.project(p2));
        p3.set(Projection.project(p3));
        renderer.line(p1, p2);
        renderer.line(p1, p3);
    }

    private void drawDotGrid(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Point);
        int dotsPerRow = (int) (GameplayConstants.GRID_HORIZON * 2 / gridWidth);
        Vector2 dot = new Vector2();
        for (int i = 0; i < dotsPerRow; i++) {
            for (int j = 0; j < dotsPerRow; j++) {
                dot.set(
                        i * gridWidth - GameplayConstants.GRID_HORIZON,
                        j * gridWidth - GameplayConstants.GRID_HORIZON
                ).add(offset);
                dot.set(Projection.project(dot));
                renderer.point(dot.x, dot.y, 0);
            }
        }
    }

    public void setGridWidth(float gridWidth) {
        this.gridWidth = gridWidth;
        initJunctionsGrid();
    }

    public void setDotRadius(float dotRadius) {
        this.dotRadius = dotRadius;
    }

    public void setOffset(Vector2 offset) {
        this.offset.x = offset.x % gridWidth;
        this.offset.y = offset.y % gridWidth;
    }

    private void initJunctionsGrid() {
        int junctionsPerRow = (int) (GameplayConstants.GRID_HORIZON * 2 / gridWidth);
        junctions = new float[junctionsPerRow][junctionsPerRow * 2];
        for (int i = 0; i < junctionsPerRow; i += 1) {
            for (int j = 0; j < junctionsPerRow; j += 1) {
                junctions[i][2 * j] = j * gridWidth - GameplayConstants.GRID_HORIZON;
                junctions[i][2 * j + 1] = i * gridWidth - GameplayConstants.GRID_HORIZON;
            }
        }
    }
}
