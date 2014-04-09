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
import com.jemge.input.IInputListener;
import com.jemge.input.InputManager;

import java.util.HashMap;

/**
 * The renderer class. The rendering is done automatically, you just have to add
 * the objects.
 * 
 * @author MrBarsack
 * @see RendererObject
 */

public class Renderer2D implements Disposable {
	// Private
	public final HashMap<Integer, Layer> RENDERTARGETS;
	private final SpriteBatch SPRITEBATCH;
	private final ShapeRenderer SHAPERENDERER;
	private final OrthographicCamera CAMERA;
	private final Background BACKGROUND;

	private final InputManager INPUTMANAGER;
	private final JUIManager JUIMANAGER;

	private RayHandler rayHandler;

	// Protected

	public final Rectangle CAMERAVIEW;

	public enum RenderMode {
		INACTIVE, ENABLED, DISABLED
	}

	public Renderer2D() {
		this.RENDERTARGETS = new HashMap<>();

		this.CAMERA = new OrthographicCamera();
		this.CAMERA.setToOrtho(false, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		this.CAMERAVIEW = new Rectangle(0, 0, this.CAMERA.viewportWidth,
				this.CAMERA.viewportHeight);

		this.SPRITEBATCH = new SpriteBatch();
		this.SHAPERENDERER = new ShapeRenderer();
		this.BACKGROUND = new Background();

		this.INPUTMANAGER = Jemge.engine.getInputManager();
		this.JUIMANAGER = Jemge.engine.getJUIManager();

		this.RENDERTARGETS.put(0, new Layer());
	}

	// Public

	public Layer addLayer(Layer layer, int num) {
		this.RENDERTARGETS.put(num, layer);

		return layer;
	}

	public void deleteLayer(int num) {
		this.RENDERTARGETS.remove(num);
	}

	/**
	 * Adds a new object to the renderer.
	 */

	public Object add(int layer, Object rendererObject) {
		if (!this.RENDERTARGETS.keySet().contains(layer)) {
			this.RENDERTARGETS.put(layer, new Layer());
		}
		this.RENDERTARGETS.get(layer).addObject(rendererObject);

		if (rendererObject instanceof IInputListener) {
			this.INPUTMANAGER.addListener((IInputListener) rendererObject);
		}

		return rendererObject;
	}

	/**
	 * Deletes an object from the renderer.
	 */

	public void remove(int layer, Object rendererObject) {
		this.RENDERTARGETS.get(layer).deleteObject(rendererObject);

		if (rendererObject instanceof IInputListener) {
			this.INPUTMANAGER.removeListener((IInputListener) rendererObject);
		}
	}

	/**
	 * Adds a new object to the renderer.
	 */

	public Object add(Object rendererObject) {
		this.RENDERTARGETS.get(0).addObject(rendererObject);

		if (rendererObject instanceof IInputListener) {
			this.INPUTMANAGER.addListener((IInputListener) rendererObject);
		}

		return rendererObject;
	}

	/**
	 * Deletes an object from the renderer.
	 */

	public void remove(Object rendererObject) {
		this.RENDERTARGETS.get(0).deleteObject(rendererObject);

		if (rendererObject instanceof IInputListener) {
			this.INPUTMANAGER.removeListener((IInputListener) rendererObject);
		}
	}

	/**
	 * Draws all objects from the render list.
	 */

	public void render() {
		Profiler.start(this, "");

		Profiler.start(this, "prepare");
		Gdx.gl20.glClearColor(this.BACKGROUND.getColor().r,
				this.BACKGROUND.getColor().g, this.BACKGROUND.getColor().b, 0);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.CAMERA.update();
		this.CAMERAVIEW.setSize(this.CAMERA.viewportWidth,
				this.CAMERA.viewportHeight);
		this.CAMERAVIEW.setCenter(this.CAMERA.position.x,
				this.CAMERA.position.y);

		this.SPRITEBATCH.setProjectionMatrix(this.CAMERA.combined);
		this.SHAPERENDERER.setProjectionMatrix(this.CAMERA.combined);

		Profiler.stop(this, "prepare");

		Profiler.start(this, "rendering");
		this.SPRITEBATCH.begin();
		this.BACKGROUND.update(this.SPRITEBATCH);

		for (Layer layer : this.RENDERTARGETS.values()) {
			layer.render(this.SPRITEBATCH, this.SHAPERENDERER);
		}

		this.SPRITEBATCH.end();
		Profiler.stop(this, "rendering");

		if (this.rayHandler != null) {
			this.rayHandler.setCombinedMatrix(this.CAMERA.combined);
			this.rayHandler.updateAndRender();
		}
		JUIMANAGER.render();

		Profiler.stop(this, "");
	}

	public void initLightSystem() {
		this.rayHandler = new RayHandler(Physics2D.getMainWorld());
	}

	public RayHandler getRayHandler() {
		if (this.rayHandler == null) {
			initLightSystem();
		}

		return this.rayHandler;
	}

	/**
	 * Must be called before exiting.
	 */

	@Override
	public void dispose() {
		this.SPRITEBATCH.dispose();
		if (this.rayHandler != null) {
			this.rayHandler.dispose();
		}
	}

	/**
	 * @return Returns an instance of the camera.
	 */

	public OrthographicCamera getCamera() {
		return this.CAMERA;
	}

	public Background getBackground() {
		return this.BACKGROUND;
	}

	public ShapeRenderer getShapeRenderer() {
		return SHAPERENDERER;
	}

	public SpriteBatch getSpriteBatch() {
		return SPRITEBATCH;
	}

}
