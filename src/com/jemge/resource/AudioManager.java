package com.jemge.resource;

import com.badlogic.gdx.math.Vector2;
import com.jemge.core.EngineModule;
import com.jemge.core.Jemge;

public class AudioManager extends EngineModule {
	private static final Vector2 TEMP1 = new Vector2();

	@Override
	public void init() {
	}

	public long playSoundAtPosition(AudioResource sound, Vector2 position) {
		if ((sound == null) || (position == null)) {
			throw new NullPointerException(
					"Position and/or sound can't be null!");
		}
		final long id = sound.getSound().play(1.0f);

		Jemge.renderer2D.CAMERAVIEW.getCenter(TEMP1);

		if (position.x > TEMP1.x) {
			sound.getSound().setPan(id, 0.25f, 0.8f); // TODO Should be relative
														// to difference...
		} else if (position.x < TEMP1.x) {
			sound.getSound().setPan(id, -0.25f, 0.8f);
		}

		return id;
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
	}
}