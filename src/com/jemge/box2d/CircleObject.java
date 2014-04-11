package com.jemge.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class CircleObject extends PhysicObject {
	private static final BodyDef BODYDEF = new BodyDef();
	private float radius;

	public CircleObject(float x, float y, float radius, BodyDef.BodyType type) {
		this.radius = radius;

		BODYDEF.type = type;
		BODYDEF.position.set(x, y).add(radius / 2, radius / 2);

		this.body = Physics2D.getMainWorld().createBody(BODYDEF);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 1.5f; // from box2d example, TODO: maybe different
									// values?
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.1f;

		this.fixture = this.body.createFixture(fixtureDef);

		circleShape.dispose();
	}

	public Vector2 getPosition() {
		this.position = this.body.getPosition();
		this.position.sub(this.radius / 2, this.radius / 2);

		return this.position;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return this.radius;
	}
}