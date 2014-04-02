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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jemge.core.EngineConfiguration;
import com.jemge.core.Jemge;
import com.jemge.j2d.culling.CullingSystem;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    private final List<Object> rendererObjects;
    protected CullingSystem cullingSystem;

    private static Renderer2D.RenderMode renderMode;

    public Layer() {
        this.rendererObjects = new ArrayList<>();
        try {
            this.cullingSystem = EngineConfiguration.cullingSystem.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object addObject(Object rend) {
        if(rend instanceof Entity){
            this.cullingSystem.putObject((Entity) rend);
        }else{
            this.rendererObjects.add(rend);
        }

        return rend;
    }

    public void deleteObject(Object rend) {
        if(rend instanceof Entity){
            this.cullingSystem.removeObject((Entity) rend);
        }else if(rend instanceof Actor) {
            ((Actor) rend).remove();
        }else{
            this.rendererObjects.remove(rend);
        }
    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer){
        this.cullingSystem.cull(Jemge.renderer2D.cameraView);

        renderMode = Renderer2D.RenderMode.INACTIVE;
        for(Entity entity : this.cullingSystem.getFinalRenderList()){
            if(entity instanceof Shape){
                ((Shape) entity).renderShape(shapeRenderer);
                continue;
            }
            if (!(entity instanceof RendererObject)) {
                continue;
            }

            if (((RendererObject) entity).hasTransparent() && !(renderMode == Renderer2D.RenderMode.ENABLED)) {    //with blending
                spriteBatch.enableBlending();

                renderMode = Renderer2D.RenderMode.ENABLED;
            } else if (!((RendererObject) entity).hasTransparent() && !(renderMode == Renderer2D.RenderMode.DISABLED)) {  //without blending
                spriteBatch.disableBlending();

                renderMode = Renderer2D.RenderMode.DISABLED;
            }
            ((RendererObject) entity).render(spriteBatch);

            if(entity instanceof Actor){
                ((Actor) entity).draw(spriteBatch, 1.0f);
            }

        }

        for(Object object : this.rendererObjects){
            if(object instanceof Shape){
                ((Shape) object).renderShape(shapeRenderer);
                continue;
            }
            if (!(object instanceof RendererObject)) {
                continue;
            }

            if (((RendererObject) object).hasTransparent() && !(renderMode == Renderer2D.RenderMode.ENABLED)) {    //with blending
                spriteBatch.enableBlending();

                renderMode = Renderer2D.RenderMode.ENABLED;
            } else if (!((RendererObject) object).hasTransparent() && !(renderMode == Renderer2D.RenderMode.DISABLED)) {  //without blending
                spriteBatch.disableBlending();

                renderMode = Renderer2D.RenderMode.DISABLED;
            }
            ((RendererObject) object).render(spriteBatch);
        }

        this.cullingSystem.postRender();
    }
}
