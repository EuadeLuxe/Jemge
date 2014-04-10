package com.jemge.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public class PhysicObject {
	protected Vector2 position;
	public Body body;
	protected Fixture fixture;

	public PhysicObject setPhysicData(float density, float friction,
			float restitution) {
		this.fixture.setDensity(density);
		this.fixture.setFriction(friction);
		this.fixture.setRestitution(restitution);

		return this;
	}
}