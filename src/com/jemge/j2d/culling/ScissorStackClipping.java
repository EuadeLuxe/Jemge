package com.jemge.j2d.culling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.jemge.core.Jemge;
import com.jemge.j2d.IEntity;

import java.util.ArrayList;
import java.util.List;

public class ScissorStackClipping implements ICullingSystem {
	private final List<IEntity> ENTITIES;
	final Rectangle SCISSORS = new Rectangle();

	public ScissorStackClipping() {
		ENTITIES = new ArrayList<IEntity>();
	}

	@Override
	public void putObject(IEntity entity) {
		ENTITIES.add(entity);
	}

	@Override
	public void removeObject(IEntity entity) {
		ENTITIES.remove(entity);
	}

	@Override
	public void cull(Rectangle camera_view) {
		ScissorStack.calculateScissors(Jemge.renderer2D.getCamera(),
				Jemge.renderer2D.getSpriteBatch().getTransformMatrix(),
				camera_view, SCISSORS);
		ScissorStack.pushScissors(SCISSORS);
	}

	@Override
	public List<IEntity> getFinalRenderList() {
		return ENTITIES;
	}

	@Override
	public void postRender() {
		ScissorStack.popScissors();
	}
}
