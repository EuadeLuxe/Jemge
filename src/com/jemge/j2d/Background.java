package com.jemge.j2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jemge.core.Jemge;

public class Background {
	private final Color COLOR;
	private JSprite texture;
	private MODE mode;

	private enum MODE {
		TEXTURE, COLOR
	}

	public Background() {
		this.COLOR = new Color();
		this.COLOR.set(Color.BLACK);
	}

	public void update(SpriteBatch spriteBatch) {
		if (this.mode == MODE.TEXTURE) {
			final OrthographicCamera CAMERA = Jemge.renderer2D.getCamera();

			this.texture.setBounds(CAMERA.position.x - CAMERA.viewportWidth,
					CAMERA.position.y - CAMERA.viewportHeight,
					CAMERA.viewportWidth * 2, CAMERA.viewportHeight * 2);

			this.texture.render(spriteBatch);
		}
	}

	public Color getColor() {
		return this.COLOR;
	}

	public void setColor(Color color) {
		this.COLOR.set(color);
		this.mode = MODE.COLOR;
	}

	public void setTexture(Texture texture) {
		if (this.texture == null) {
			this.texture = new JSprite(texture, 0, 0);
		} else {
			this.texture.setTexture(texture);
		}

		Jemge.renderer2D.addLayer(new Layer(), -1);
		this.mode = MODE.TEXTURE;
	}
}