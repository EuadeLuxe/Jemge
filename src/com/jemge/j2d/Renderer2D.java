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
import com.badlogic.gdx.graphics.Color;
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
import com.jemge.j2d.culling.CullingSystem;
import com.jemge.j2d.culling.ZoneBasedCulling;

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

    public RayHandler rayHandler;


    //Protected

    public final Rectangle cameraView;


    public enum RenderMode {
        INACTIVE, ENABLED, DISABLED
    }


    public Renderer2D() {
        renderTargets = new HashMap<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraView = new Rectangle(0, 0, camera.viewportWidth, camera.viewportHeight);

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Background();

        inputManager = Jemge.engine.getInputManager();
        rayHandler = new RayHandler(Physics2D.getMainWorld());

        renderTargets.put(0, new Layer());
        rayHandler.setAmbientLight(1.0f);

    }

    //Public

    public Layer addLayer(Layer layer, int num) {
        renderTargets.put(num, layer);

        return layer;
    }

    public void deleteLayer(int num) {
        renderTargets.remove(num);
    }

    /**
     * Adds a new object to the renderer.
     */

    public Object add(int layer, Object rendererObject) {
        if (!renderTargets.keySet().contains(layer)) {
            renderTargets.put(layer, new Layer());
        }
        renderTargets.get(layer).addObject(rendererObject);

        if (rendererObject instanceof InputListener) {
            inputManager.addListener((InputListener) rendererObject);
        }

        return rendererObject;
    }

    /**
     * Deletes an object from the renderer.
     */


    public void remove(int layer, Object rendererObject) {
        renderTargets.get(layer).deleteObject(rendererObject);

        if (rendererObject instanceof InputListener) {
            inputManager.removeListener((InputListener) rendererObject);
        }
    }

    /**
     * Adds a new object to the renderer.
     */

    public Object add(Object rendererObject) {
        renderTargets.get(0).addObject(rendererObject);

        if (rendererObject instanceof InputListener) {
            inputManager.addListener((InputListener) rendererObject);
        }

        return rendererObject;
    }

    /**
     * Deletes an object from the renderer.
     */


    public void remove(Object rendererObject) {
        renderTargets.get(0).deleteObject(rendererObject);

        if (rendererObject instanceof InputListener) {
            inputManager.removeListener((InputListener) rendererObject);
        }
    }

    /**
     * Draws all objects from the render list.
     */

    public void render() {
        Profiler.start(this, "");

        Profiler.start(this, "prepare");
        Gdx.gl20.glClearColor(background.getColor().r, background.getColor().g, background.getColor().b, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        cameraView.setSize(camera.viewportWidth, camera.viewportHeight);
        cameraView.setCenter(camera.position.x,
                camera.position.y);

        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        background.update(spriteBatch);

        Profiler.stop(this, "prepare");

        Profiler.start(this, "rendering");
        spriteBatch.begin();

        for (Layer layer : renderTargets.values()) {
            layer.render(spriteBatch, shapeRenderer);
        }

        spriteBatch.end();
        Profiler.stop(this, "rendering");

        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.updateAndRender();

        Profiler.stop(this, "");
    }

    /**
     * Must be called before exiting.
     */

    @Override
    public void dispose() {
        spriteBatch.dispose();
        rayHandler.dispose();

    }


    public void setCullingSystem(CullingSystem system){
        //todo
    }

    /**
     * @return Returns an instance of the camera.
     */

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Background getBackground() {
        return background;
    }


}
