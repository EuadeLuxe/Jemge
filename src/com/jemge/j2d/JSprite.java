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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.jemge.core.Jemge;
import com.jemge.core.system.IUpdateListener;

/**
 * Default object for drawing textures.
 * 
 * @author MrBarsack
 * @see RendererObject
 */

public class JSprite extends Sprite implements IRendererObject, IEntity {
	// for static jsprites
	private boolean isStatic = false;
	private boolean hasTransparent = false;
	private Rectangle cachedBound;
	public IUpdateListener<JSprite> listener;

	public JSprite(Texture texture) {
		super(texture);
	}

	public JSprite(Texture texture, float x, float y) {
		super(texture);
		setPosition(x, y);
	}

	public JSprite(Texture texture, float x, float y, float width, float height) {
		super(texture);
		setBounds(x, y, width, height);
	}

	public JSprite(TextureRegion region) {
		super(region);
	}

	public JSprite(JSprite sprite) {
		super(sprite);
	}

	/**
	 * @return Is this jsprite transparent? Override.
	 */

	@Override
	public boolean hasTransparent() {
		return this.hasTransparent;
	}

	public JSprite setOpaque(boolean is) {
		this.hasTransparent = is;
		return this;
	}

	/**
	 * Render this jsprite
	 */

	@Override
	public void render(SpriteBatch spriteBatch) {
		draw(spriteBatch);

		if (this.listener != null) {
			this.listener.update(this);
		}
	}

	@Override
	public boolean needRender() {
		if (this.isStatic) {
			return Jemge.renderer2D.CAMERAVIEW.overlaps(this.cachedBound);
		}

		// Inside the camera view?
		return Jemge.renderer2D.CAMERAVIEW.overlaps(getBoundingRectangle());
	}

	@Override
	public boolean getData(String name) {
		return name.equals("static") && this.isStatic;
	}

	@Override
	public Rectangle getRectangle() {
		if (this.isStatic) {
			return this.cachedBound;
		}
		return getBoundingRectangle();
	}

	/**
	 * Static items do *not* update bounding rectangle.
	 * 
	 * @param set
	 * @return
	 */

	public JSprite setStatic(boolean set) {
		this.isStatic = set;

		if (set && this.cachedBound == null) {
			this.cachedBound = getBoundingRectangle();
		}
		return this;
	}
}