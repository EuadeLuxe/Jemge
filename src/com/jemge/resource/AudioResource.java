package com.jemge.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioResource implements IResource {
	private final String NAME;
	private final Sound SOUND;

	public AudioResource(String name) {
		this.NAME = name;
		this.SOUND = Gdx.audio.newSound(Gdx.files.internal(name));
	}

	@Override
	public ResourceType getType() {
		return ResourceType.SOUND;
	}

	@Override
	public String getName() {
		return this.NAME;
	}

	public Sound getSound() {
		return this.SOUND;
	}
}