package com.jemge.j2d.shapes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.jemge.core.Jemge;
import com.jemge.core.system.UpdateListener;
import com.jemge.j2d.Entity;
import com.jemge.j2d.Shape;

public class CircleShape implements Shape, Entity{
    public float radius;
    public Rectangle rectangle;

    public UpdateListener listener;

    public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Filled;

    public CircleShape(float x, float y, float radius){
        this.rectangle = new Rectangle(x, y, radius, radius);
        this.radius = radius;
    }

    public CircleShape(Vector2 position, float radius){
        this.rectangle = new Rectangle(position.x, position.y, radius, radius);
        this.radius = radius;
    }

    public CircleShape(CircleShape circleShape){
        this.rectangle = new Rectangle(circleShape.rectangle.x, circleShape.rectangle.y,
                circleShape.radius, circleShape.radius);
        this.radius = circleShape.radius;
    }

    @Override
    public float getX() {
        return rectangle.x;
    }

    @Override
    public float getY() {
        return rectangle.y;
    }

    @Override
    public float getHeight() {
        return radius;
    }

    @Override
    public float getWidth() {
        return radius;
    }

    @Override
    public boolean needRender() {
        //Inside the camera view?
        return Jemge.renderer2D.cameraView.overlaps(rectangle);
    }

    @Override
    public boolean getData(String name) {
        return false;
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void renderShape(ShapeRenderer renderer) {
        if(listener != null){
            listener.update(this);
        }

        renderer.begin(shapeType);
        renderer.circle(rectangle.x, rectangle.y, radius);
        renderer.end();
    }
}
