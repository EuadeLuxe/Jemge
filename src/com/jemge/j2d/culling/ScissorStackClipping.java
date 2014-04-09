package com.jemge.j2d.culling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.jemge.core.Jemge;
import com.jemge.j2d.IEntity;

import java.util.ArrayList;
import java.util.List;

public class ScissorStackClipping implements ICullingSystem {
	final private List<IEntity> entities;
	final Rectangle scissors = new Rectangle();

	public ScissorStackClipping() {
		entities = new ArrayList<>();
	}

	@Override
	public void putObject(IEntity entity) {
		entities.add(entity);
	}

	@Override
	public void removeObject(IEntity entity) {
		entities.remove(entity);
	}

	@Override
	public void cull(Rectangle camera_view) {
		ScissorStack.calculateScissors(Jemge.renderer2D.getCamera(),
				Jemge.renderer2D.getSpriteBatch().getTransformMatrix(),
				camera_view, scissors);
		ScissorStack.pushScissors(scissors);
	}

	@Override
	public List<IEntity> getFinalRenderList() {
		return entities;
	}

	@Override
	public void postRender() {
		ScissorStack.popScissors();
	}
}
