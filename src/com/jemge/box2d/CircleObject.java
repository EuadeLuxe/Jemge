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

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;

public class CircleObject extends PhysicObject implements Serializable {
	private float radius;

	public CircleObject(float x, float y, float radius, BodyDef.BodyType type) {
		this.radius = radius;

		BODYDEF.type = type;
		BODYDEF.position.set(x, y).add(radius / 2, radius / 2);

		this.body = Physics2D.getMainWorld().createBody(BODYDEF);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius);

		this.fixture = this.body.createFixture(this
				.setupFixtureDef(circleShape));

		circleShape.dispose();
	}

	public CircleObject(float x, float y, float radius, Filter filter,
			BodyDef.BodyType type) {
		this.radius = radius;

		BODYDEF.type = type;
		BODYDEF.position.set(x, y).add(radius / 2, radius / 2);

		this.body = Physics2D.getMainWorld().createBody(BODYDEF);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius);

		this.fixture = this.body.createFixture(this.setupFixtureDef(
				circleShape, filter));

		circleShape.dispose();
	}

	public void savePosition() {
		this.position = this.body.getPosition();
		this.position.sub(this.radius / 2, this.radius / 2);
	}

	public Vector2 getPosition() {
		if (this.body != null) {
			this.position = this.body.getPosition();
			this.position.sub(this.radius / 2, this.radius / 2);
		}
		return this.position;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return this.radius;
	}
}