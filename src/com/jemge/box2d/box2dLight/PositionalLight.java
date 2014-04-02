package com.jemge.box2d.box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.jemge.j2d.Entity;

public abstract class PositionalLight extends Light {

    private Body body;
    private Entity entity;
    private float bodyOffsetX;
    private float bodyOffsetY;
    final float[] sin;
    final float[] cos;

    final Vector2 start = new Vector2();
    final float[] endX;
    final float[] endY;

    /**
     * attach positional light to automatically follow body. Position is fixed
     * to given offset.
     */
    @Override
    public void attachToBody(Body body, float offsetX, float offSetY) {
        this.body = body;
        this.bodyOffsetX = offsetX;
        this.bodyOffsetY = offSetY;
        if (this.staticLight) {
            staticUpdate();
        }
    }

    @Override
    public void attachToEntity(Entity entity, float offsetX, float offSetY) {
        this.entity = entity;
        this.bodyOffsetX = offsetX;
        this.bodyOffsetY = offSetY;
        if (this.staticLight) {
            staticUpdate();
        }
    }

    @Override
    public Vector2 getPosition() {
        this.tmpPosition.x = this.start.x;
        this.tmpPosition.y = this.start.y;
        return this.tmpPosition;
    }

    public Body getBody() {
        return this.body;
    }

    /**
     * horizontal starting position of light in world coordinates.
     */
    @Override
    public float getX() {
        return this.start.x;
    }

    /**
     * vertical starting position of light in world coordinates.
     */
    @Override
    public float getY() {
        return this.start.y;
    }

    private final Vector2 tmpEnd = new Vector2();

    @Override
    public void setPosition(float x, float y) {
        this.start.x = x;
        this.start.y = y;
        if (this.staticLight) {
            staticUpdate();
        }
    }

    @Override
    public void setPosition(Vector2 position) {
        this.start.x = position.x;
        this.start.y = position.y;
        if (this.staticLight) {
            staticUpdate();
        }
    }

    @Override
    void update() {
        if (this.body != null && !this.staticLight) {
            final Vector2 vec = this.body.getPosition();
            float angle = this.body.getAngle();
            final float cos = MathUtils.cos(angle);
            final float sin = MathUtils.sin(angle);
            final float dX = this.bodyOffsetX * cos - this.bodyOffsetY * sin;
            final float dY = this.bodyOffsetX * sin + this.bodyOffsetY * cos;
            this.start.x = vec.x + dX;
            this.start.y = vec.y + dY;
            setDirection(angle * MathUtils.radiansToDegrees);
        } else if (this.entity != null && !this.staticLight) {
            final Vector2 vec = new Vector2(this.entity.getX(), this.entity.getY());
            float angle = this.entity.getRotation();
            final float cos = MathUtils.cos(angle);
            final float sin = MathUtils.sin(angle);
            final float dX = this.bodyOffsetX * cos - this.bodyOffsetY * sin;
            final float dY = this.bodyOffsetX * sin + this.bodyOffsetY * cos;
            this.start.x = vec.x + dX;
            this.start.y = vec.y + dY;
            setDirection(angle * MathUtils.radiansToDegrees);
        }

        if (this.rayHandler.culling) {
            this.culled = ((!this.rayHandler.intersect(this.start.x, this.start.y, this.distance
                    + this.softShadowLenght)));
            if (this.culled) {
                return;
            }
        }

        if (this.staticLight) {
            return;
        }

        for (int i = 0; i < this.rayNum; i++) {
            this.m_index = i;
            this.f[i] = 1f;
            this.tmpEnd.x = this.endX[i] + this.start.x;
            this.mx[i] = this.tmpEnd.x;
            this.tmpEnd.y = this.endY[i] + this.start.y;
            this.my[i] = this.tmpEnd.y;
            if (this.rayHandler.world != null && !this.xray) {
                this.rayHandler.world.rayCast(this.ray, this.start, this.tmpEnd);
            }
        }
        setMesh();
    }

    void setMesh() {
        // ray starting point
        int size = 0;

        this.segments[size++] = this.start.x;
        this.segments[size++] = this.start.y;
        this.segments[size++] = this.colorF;
        this.segments[size++] = 1;
        // rays ending points.
        for (int i = 0; i < this.rayNum; i++) {
            this.segments[size++] = this.mx[i];
            this.segments[size++] = this.my[i];
            this.segments[size++] = this.colorF;
            this.segments[size++] = 1 - this.f[i];
        }
        this.lightMesh.setVertices(this.segments, 0, size);

        if (!this.soft || this.xray) {
            return;
        }

        size = 0;
        // rays ending points.

        for (int i = 0; i < this.rayNum; i++) {
            this.segments[size++] = this.mx[i];
            this.segments[size++] = this.my[i];
            this.segments[size++] = this.colorF;
            final float s = (1 - this.f[i]);
            this.segments[size++] = s;
            this.segments[size++] = this.mx[i] + s * this.softShadowLenght * this.cos[i];
            this.segments[size++] = this.my[i] + s * this.softShadowLenght * this.sin[i];
            this.segments[size++] = zero;
            this.segments[size++] = 0f;
        }
        this.softShadowMesh.setVertices(this.segments, 0, size);
    }

    @Override
    void render() {
        if (this.rayHandler.culling && this.culled) {
            return;
        }

        this.rayHandler.lightRenderedLastFrame++;

        this.lightMesh.render(this.rayHandler.lightShader, GL20.GL_TRIANGLE_FAN, 0,
                this.vertexNum);
        if (this.soft && !this.xray) {
            this.softShadowMesh.render(this.rayHandler.lightShader,
                    GL20.GL_TRIANGLE_STRIP, 0, (this.vertexNum - 1) * 2);
        }
    }

    public PositionalLight(int rays, Color color,
                           float distance, float x, float y, float directionDegree) {
        super(rays, color, directionDegree, distance);
        this.start.x = x;
        this.start.y = y;
        this.sin = new float[rays];
        this.cos = new float[rays];
        this.endX = new float[rays];
        this.endY = new float[rays];


        this.lightMesh = new Mesh(VertexDataType.VertexArray, false, this.vertexNum, 0,
                new VertexAttribute(Usage.Position, 2, "vertex_positions"),
                new VertexAttribute(Usage.ColorPacked, 4, "quad_colors"),
                new VertexAttribute(Usage.Generic, 1, "s"));
        this.softShadowMesh = new Mesh(VertexDataType.VertexArray, false, this.vertexNum * 2, 0,
                new VertexAttribute(Usage.Position, 2, "vertex_positions"),
                new VertexAttribute(Usage.ColorPacked, 4, "quad_colors"),
                new VertexAttribute(Usage.Generic, 1, "s"));


        setMesh();
    }

    @Override
    public boolean contains(float x, float y) {

        // fast fail
        final float x_d = this.start.x - x;
        final float y_d = this.start.y - y;
        final float dst2 = x_d * x_d + y_d * y_d;
        if (this.distance * this.distance <= dst2) {
            return false;
        }

        // actual check

        boolean oddNodes = false;
        float x2 = this.mx[this.rayNum] = this.start.x;
        float y2 = this.my[this.rayNum] = this.start.y;
        float x1, y1;
        for (int i = 0; i <= this.rayNum; x2 = x1, y2 = y1, ++i) {
            x1 = this.mx[i];
            y1 = this.my[i];
            if (((y1 < y) && (y2 >= y))
                    || (y1 >= y) && (y2 < y)) {
                if ((y - y1) / (y2 - y1)
                        * (x2 - x1) < (x - x1)) {
                    oddNodes = !oddNodes;
                }
            }
        }
        return oddNodes;

    }
}
