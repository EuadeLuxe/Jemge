package com.jemge.resource;

import com.badlogic.gdx.math.Vector2;
import com.jemge.core.EngineModule;
import com.jemge.core.Jemge;

public class AudioManager extends EngineModule {
	private static final Vector2 CENTER = new Vector2();

	@Override
	public void init() {
	}

	public long playSoundAtPosition(AudioResource sound, Vector2 position) {
		if ((sound == null) || (position == null)) {
			throw new NullPointerException(
					"Position and/or sound can't be null!");
		}
		final long id = sound.getSound().play(1.0f);

		Jemge.renderer2D.CAMERAVIEW.getCenter(CENTER);

		float dx = (position.x - CENTER.x)
				/ (Jemge.renderer2D.CAMERAVIEW.width / 2);
		float dvolume = dx < 0 ? dx + 1 : 1 - dx;

		// System.out.println(dx + "," + dvolume); //DEBUG
		sound.getSound().setPan(id, dx, 0.8f * dvolume);

		return id;
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
	}
}