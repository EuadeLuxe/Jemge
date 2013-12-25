package com.jemge.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public class PhysicObject {
    protected Vector2 position;
    protected Body body;
    protected Fixture fixture;

    public PhysicObject setPhysicData(float density, float friction, float restitution) {
        fixture.setDensity(density);
        fixture.setFriction(friction);
        fixture.setRestitution(restitution);

        return this;
    }
}