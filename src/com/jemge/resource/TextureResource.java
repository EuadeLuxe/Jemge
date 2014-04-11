package com.jemge.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureResource extends Texture implements IResource {
	private final String NAME;

	public TextureResource(String name) {
		super(Gdx.files.internal(name));
		this.NAME = name;
	}

	@Override
	public ResourceType getType() {
		return ResourceType.TEXTURE;
	}

	@Override
	public String getName() {
		return this.NAME;
	}
}