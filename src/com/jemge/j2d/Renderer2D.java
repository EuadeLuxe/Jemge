/*
 * Copyright [2013] @author file
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
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * The renderer class. The rendering is done automatically, you just have to add the objects.
 *
 * @author MrBarsack
 * @see RendererObject
 */

public class Renderer2D implements Disposable {

    //Private

    private static Renderer2D renderer2D;

    private final List<RendererObject> renderTargets;
    private final SpriteBatch spriteBatch;
    private final OrthographicCamera camera;

    private RenderMode renderMode;

    //Protected

    protected final Rectangle cameraView;

    public enum RenderMode {
        INACTIVE, ENABLED, DISABLED
    }


    public Renderer2D() {
        renderer2D = this;

        renderTargets = new ArrayList<RendererObject>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cameraView = new Rectangle(0, 0, camera.viewportWidth, camera.viewportHeight);

        spriteBatch = new SpriteBatch();
    }

    //Public

    /**
     * Adds a new object to the renderer.
     */

    public RendererObject add(RendererObject rendererObject) {
        renderTargets.add(rendererObject);

        return rendererObject;
    }

    /**
     * Deletes an object from the renderer.
     */


    public void remove(RendererObject rendererObject) {
        renderTargets.remove(rendererObject);

    }

    /**
     * Draws all objects from the render list.
     */

    public void render() {

        Gdx.gl20.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
      /*  cameraView.setCenter(camera.position.x,
                camera.position.y);
*/
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        renderMode = RenderMode.INACTIVE;

        for (RendererObject rend : renderTargets) {
            if(!rend.needRender()) continue;

            if (rend.hasTransparent() && !(renderMode == RenderMode.ENABLED)) {    //with blending
                spriteBatch.enableBlending();

                renderMode = RenderMode.ENABLED;
            } else if (!rend.hasTransparent() && !(renderMode == RenderMode.DISABLED)) {  //without blending
                spriteBatch.disableBlending();

                renderMode = RenderMode.DISABLED;
            }
            rend.render(spriteBatch);

        }
        spriteBatch.end();
    }

    /**
     * Must be called before exiting.
     */

    @Override
    public void dispose() {
        spriteBatch.dispose();

        for (RendererObject rend : renderTargets) {
            rend.dispose();
        }
    }

    /**
     * @return Returns an instance of the renderer.
     */

    public static Renderer2D getRenderer2D() {
        if (renderer2D == null) {
            new Renderer2D();
        }

        return renderer2D;
    }

    /**
     * @return Returns an instance of the camera.
     */

    public Camera getCamera() {
        return camera;
    }


}
