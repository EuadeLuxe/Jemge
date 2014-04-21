package com.jemge.box2d.box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class DegreeLight extends PointLight {
    private float direction;
    private float size;

    public DegreeLight(int rays, Color color, float distance, float x, float y, float direction, float size) {
        super(rays, color, distance, x, y);

        this.direction = direction;
        this.size = size;
        this.setEndPoints();
    }

    public DegreeLight(int rays) {
        super(rays);
    }

    @Override
    public void update() {
        this.setEndPoints();

        super.update();
    }

    @Override
    protected void setEndPoints() {
        float angleNum = size / (this.rayNum - 1);
        for (int i = 0; i < this.rayNum; i++) {
            final float angle = angleNum * i + direction;
            this.SIN[i] = MathUtils.sinDeg(angle);
            this.COS[i] = MathUtils.cosDeg(angle);
            this.ENDX[i] = this.distance * this.COS[i];
            this.ENDY[i] = this.distance * this.SIN[i];
        }
    }

    public void setDirection(float direction){ this.direction = direction;  }

    public void showAt(float x, float y){
        setDirection((float) ((Math.atan2 (x - getX() - 32,
                -(y - getY() - 32))*180.0d/Math.PI)+180f + size / 2));
    }

    public void setSize(float size) {
        this.size = size;
    }
}
