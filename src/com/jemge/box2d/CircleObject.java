package com.jemge.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class CircleObject extends PhysicObject {
    private final static BodyDef bodyDef = new BodyDef();

    private float radius;

    public CircleObject(float x, float y, float radius, BodyDef.BodyType type) {
        this.radius = radius;

        bodyDef.type = type;
        bodyDef.position.set(x, y).add(radius / 2, radius / 2);

        body = Physics2D.getMainWorld().createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        fixture = body.createFixture(fixtureDef);

        circleShape.dispose();
    }

    public Vector2 getPosition() {
        position = body.getPosition();
        position.sub(radius / 2, radius / 2);

        return position;
    }

    public void setRadius(float new_radius) {
        radius = new_radius;
    }

    public float getRadius() {
        return radius;
    }
}
