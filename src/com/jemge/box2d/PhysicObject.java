/*
 * Copyright [2014] @author file
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jemge.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public abstract class PhysicObject {
	protected static final BodyDef BODYDEF = new BodyDef();
	protected Vector2 position;
	protected Body body;
	protected Fixture fixture;

	// TODO wird nicht verwendet?
	public PhysicObject setPhysicData(float density, float friction,
			float restitution) {
		this.fixture.setDensity(density);
		this.fixture.setFriction(friction);
		this.fixture.setRestitution(restitution);

		return this;
	}

	public FixtureDef setupFixtureDef(Shape shape) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.5f; // from box2d example, TODO: maybe different
									// values?
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.1f;
		return fixtureDef;
	}

	public final FixtureDef setupFixtureDef(Shape shape, float density,
			float friction, float restitution) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		return fixtureDef;
	}

	public FixtureDef setupFixtureDef(Shape shape, Filter filter) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.maskBits = filter.maskBits;
		fixtureDef.filter.categoryBits = filter.categoryBits;
		fixtureDef.filter.groupIndex = filter.groupIndex;
		fixtureDef.density = 1.5f; // from box2d example, TODO: maybe different
		// values?
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.1f;
		return fixtureDef;
	}

	public final FixtureDef setupFixtureDef(Shape shape, Filter filter,
			float density, float friction, float restitution) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.maskBits = filter.maskBits;
		fixtureDef.filter.categoryBits = filter.categoryBits;
		fixtureDef.filter.groupIndex = filter.groupIndex;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		return fixtureDef;
	}

	public abstract Vector2 getPosition();
}