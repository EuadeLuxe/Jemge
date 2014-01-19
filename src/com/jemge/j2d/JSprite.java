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
import com.jemge.core.system.UpdateListener;
import com.jemge.utils.StringHelper;

/**
 * Default object for drawing textures.
 *
 * @author MrBarsack
 * @see RendererObject
 */

public class JSprite extends Sprite implements RendererObject, Entity{

    //for static jsprites
    private boolean isStatic = false;
    private boolean hasTransparent = false;
    private Rectangle cachedBound;
    public UpdateListener listener;

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
        return hasTransparent;
    }

    public JSprite setOpaque(boolean is){
        hasTransparent = is;

        return this;
    }


    /**
     * Render this jsprite
     */

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);

        if(listener != null){
            listener.update(this);
        }

    }

    /**
     * Dispose the native
     */
    @Override
    public void dispose() {
    }

    @Override
    public boolean needRender() {
        if (isStatic) {
            return Jemge.renderer2D.cameraView.overlaps(cachedBound);
        }

        //Inside the camera view?
        return Jemge.renderer2D.cameraView.overlaps(getBoundingRectangle());
    }

    @Override
    public boolean getData(String name) {
        return StringHelper.equals(name, "static") && isStatic;
    }

    @Override
    public Rectangle getRectangle() {
        if (isStatic) {
            return cachedBound;
        }

        return getBoundingRectangle();
    }

    /**
     * Static items do *not* update bounding rectangle.
     * @param set
     * @return
     */

    public JSprite setStatic(boolean set) {
        isStatic = set;

        if (set && cachedBound == null) {
            cachedBound = getBoundingRectangle();
        }

        return this;
    }

}
