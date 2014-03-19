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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PolygonObject extends PhysicObject {
    private final static BodyDef bodyDef = new BodyDef();

    protected float height;
    protected float width;

    public PolygonObject(float x, float y, float w, float h, BodyDef.BodyType type) {
        this.height = h;
        this.width = w;

        bodyDef.type = type;
        bodyDef.position.set(x, y).add(this.width / 2, this.height / 2);

        this.body = Physics2D.getMainWorld().createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(w / 2, h / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1.5f; //from box2d example, todo: maybe different values?
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        this.fixture = this.body.createFixture(fixtureDef);

        polygonShape.dispose();
    }

    public PolygonObject(float x, float y, float size, FileHandle fileHandle, String name,BodyDef.BodyType type) {
        final BoxEditorLoader loader = new BoxEditorLoader(fileHandle);

        bodyDef.position.set(x, y);
        bodyDef.type = type;

        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        fd.friction = 0.5f;
        fd.restitution = 0.3f;

        Body body = Physics2D.getMainWorld().createBody(bodyDef);

        loader.attachFixture(body, name, fd, size);
    }

    public PolygonObject(float x, float y, Texture texture, FileHandle fileHandle, String name,BodyDef.BodyType type) {
        final BoxEditorLoader loader = new BoxEditorLoader(fileHandle);

        bodyDef.position.set(x, y);
        bodyDef.type = type;

        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        fd.friction = 0.5f;
        fd.restitution = 0.3f;

        Body body = Physics2D.getMainWorld().createBody(bodyDef);

        loader.attachFixture(body, texture, name, fd);
    }


    public Vector2 getPosition() {
        this.position = this.body.getPosition();
        this.position.sub(this.width / 2, this.height / 2);

        return this.position;
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }
}
