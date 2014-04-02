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

package com.jemge.j2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.jemge.box2d.Physics2D;
import com.jemge.box2d.box2dLight.RayHandler;
import com.jemge.core.Jemge;
import com.jemge.core.debug.Profiler;
import com.jemge.input.InputListener;
import com.jemge.input.InputManager;

import java.util.HashMap;

/**
 * The renderer class. The rendering is done automatically, you just have to add the objects.
 *
 * @author MrBarsack
 * @see RendererObject
 */

public class Renderer2D implements Disposable {

    //Private

    public final HashMap<Integer, Layer> renderTargets;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private final Background background;

    private final InputManager inputManager;
    private final JUIManager juiManager;

    private RayHandler rayHandler;


    //Protected

    public final Rectangle cameraView;


    public enum RenderMode {
        INACTIVE, ENABLED, DISABLED
    }


    public Renderer2D() {
        this.renderTargets = new HashMap<>();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.cameraView = new Rectangle(0, 0, this.camera.viewportWidth, this.camera.viewportHeight);

        this.spriteBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.background = new Background();

        this.inputManager = Jemge.engine.getInputManager();
        this.juiManager = Jemge.engine.getJUIManager();

        this.renderTargets.put(0, new Layer());

    }

    //Public

    public Layer addLayer(Layer layer, int num) {
        this.renderTargets.put(num, layer);

        return layer;
    }

    public void deleteLayer(int num) {
        this.renderTargets.remove(num);
    }

    /**
     * Adds a new object to the renderer.
     */

    public Object add(int layer, Object rendererObject) {
        if (!this.renderTargets.keySet().contains(layer)) {
            this.renderTargets.put(layer, new Layer());
        }
        this.renderTargets.get(layer).addObject(rendererObject);

        if (rendererObject instanceof InputListener) {
            this.inputManager.addListener((InputListener) rendererObject);
        }

        return rendererObject;
    }

    /**
     * Deletes an object from the renderer.
     */


    public void remove(int layer, Object rendererObject) {
        this.renderTargets.get(layer).deleteObject(rendererObject);

        if (rendererObject instanceof InputListener) {
            this.inputManager.removeListener((InputListener) rendererObject);
        }
    }

    /**
     * Adds a new object to the renderer.
     */

    public Object add(Object rendererObject) {
        this.renderTargets.get(0).addObject(rendererObject);

        if (rendererObject instanceof InputListener) {
            this.inputManager.addListener((InputListener) rendererObject);
        }

        return rendererObject;
    }

    /**
     * Deletes an object from the renderer.
     */


    public void remove(Object rendererObject) {
        this.renderTargets.get(0).deleteObject(rendererObject);

        if (rendererObject instanceof InputListener) {
            this.inputManager.removeListener((InputListener) rendererObject);
        }
    }

    /**
     * Draws all objects from the render list.
     */

    public void render() {
        Profiler.start(this, "");

        Profiler.start(this, "prepare");
        Gdx.gl20.glClearColor(this.background.getColor().r, this.background.getColor().g, this.background.getColor().b, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camera.update();
        this.cameraView.setSize(this.camera.viewportWidth, this.camera.viewportHeight);
        this.cameraView.setCenter(this.camera.position.x,
                this.camera.position.y);

        this.spriteBatch.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);

        Profiler.stop(this, "prepare");

        Profiler.start(this, "rendering");
        this.spriteBatch.begin();
        this.background.update(this.spriteBatch);

        for (Layer layer : this.renderTargets.values()) {
            layer.render(this.spriteBatch, this.shapeRenderer);
        }

        this.spriteBatch.end();
        Profiler.stop(this, "rendering");

        if(this.rayHandler != null){
            this.rayHandler.setCombinedMatrix(this.camera.combined);
            this.rayHandler.updateAndRender();
        }
        juiManager.render();

        Profiler.stop(this, "");
    }

    public void initLightSystem(){
        this.rayHandler = new RayHandler(Physics2D.getMainWorld());

    }

    public RayHandler getRayHandler(){
        if(this.rayHandler == null){
            initLightSystem();
        }

        return this.rayHandler;
    }

    /**
     * Must be called before exiting.
     */

    @Override
    public void dispose() {
        this.spriteBatch.dispose();
        if(this.rayHandler != null){
            this.rayHandler.dispose();
        }

    }

    /**
     * @return Returns an instance of the camera.
     */

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public Background getBackground() {
        return this.background;
    }

    public ShapeRenderer getShapeRenderer(){
        return shapeRenderer;
    }

    public SpriteBatch getSpriteBatch() { return spriteBatch; }


}
