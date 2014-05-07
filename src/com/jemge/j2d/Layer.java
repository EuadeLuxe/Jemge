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
import com.jemge.j2d.culling.ICullingSystem;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	private final List<Object> RENDEREROBJECTS;
	protected ICullingSystem cullingSystem;
	private static Renderer2D.RenderMode renderMode;

	public Layer() {
		this.RENDEREROBJECTS = new ArrayList<Object>();
		try {
			this.cullingSystem = EngineConfiguration.cullingSystem.getClass()
					.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public Object addObject(Object rend) {
		if (rend instanceof IEntity) {
			this.cullingSystem.putObject((IEntity) rend);
		} else {
			this.RENDEREROBJECTS.add(rend);
		}
		return rend;
	}

	public void deleteObject(Object rend) {
		if (rend instanceof IEntity) {
			this.cullingSystem.removeObject((IEntity) rend);
		} else if (rend instanceof Actor) {
			((Actor) rend).remove();
		} else {
			this.RENDEREROBJECTS.remove(rend);
		}
	}

	public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		this.cullingSystem.cull(Jemge.renderer2D.CAMERAVIEW);

		renderMode = Renderer2D.RenderMode.INACTIVE;
		ArrayList<IRendererObject> transparentObjects = new ArrayList<IRendererObject>();
		
		spriteBatch.disableBlending();
		renderMode = Renderer2D.RenderMode.DISABLED;
		
		for (IEntity entity : this.cullingSystem.getFinalRenderList()) {
			if (entity instanceof IShape) {
				((IShape) entity).renderShape(shapeRenderer);
				continue;
			}
			if (!(entity instanceof IRendererObject)) {
				continue;
			}

			if (((IRendererObject) entity).hasTransparent()) {
				transparentObjects.add((IRendererObject) entity);
				continue; // <- Continue if the object is translucent.
			}
			((IRendererObject) entity).render(spriteBatch);

			if (entity instanceof Actor) {
				((Actor) entity).draw(spriteBatch, 1.0f);
			}
		}

		for (Object object : this.RENDEREROBJECTS) {
			if (object instanceof IShape) {
				((IShape) object).renderShape(shapeRenderer);
				continue;
			}
			if (!(object instanceof IRendererObject)) {
				continue;
			}

			if (((IRendererObject) object).hasTransparent()) {
				transparentObjects.add((IRendererObject) object);
				continue;
			}
			((IRendererObject) object).render(spriteBatch);
		}
		
		// Now do the alpha pass:
		
		for(IRendererObject object : transparentObjects)
		{
			object.render(spriteBatch);
			if(object instanceof Actor)
				((Actor) object).draw(spriteBatch, 1.0f);
		}

		this.cullingSystem.postRender();
	}
}