package com.jemge.j2d.jscene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jemge.j2d.IEntity;
import com.jemge.j2d.JSprite;
import com.jemge.j2d.IRendererObject;

public class JSpriteActor extends Actor implements IEntity, IRendererObject {
	private final JSprite JSPRITE;

	public JSpriteActor(Texture texture) {
		this.JSPRITE = new JSprite(texture);
	}

	public JSpriteActor(Texture texture, float x, float y) {
		this.JSPRITE = new JSprite(texture);
		setPosition(x, y);
	}

	public JSpriteActor(Texture texture, float x, float y, float width,
			float height) {
		this.JSPRITE = new JSprite(texture);
		setBounds(x, y, width, height);
	}

	public JSpriteActor(TextureRegion region) {
		this.JSPRITE = new JSprite(region);
	}

	public JSpriteActor(JSprite sprite) {
		this.JSPRITE = new JSprite(sprite);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		this.JSPRITE.setBounds(getX(), getY(), getWidth(), getHeight());
		this.JSPRITE.render((SpriteBatch) batch);
	}

	@Override
	public boolean needRender() {
		return this.JSPRITE.needRender();
	}

	@Override
	public boolean getData(String name) {
		return this.JSPRITE.getData(name);
	}

	@Override
	public Rectangle getRectangle() {
		return this.JSPRITE.getRectangle();
	}

	public void setTexture(Texture texture) {
		this.JSPRITE.setTexture(texture);
	}

	public JSpriteActor setStatic(boolean value) {
		this.JSPRITE.setStatic(value);
		return this;
	}

	@Override
	public boolean hasTransparent() {
		return this.JSPRITE.hasTransparent();
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		// TODO no need for it - bad solution, don't like it
	}
}