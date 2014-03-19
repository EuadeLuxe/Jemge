package com.jemge.j2d.jscene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jemge.j2d.Entity;
import com.jemge.j2d.JSprite;
import com.jemge.j2d.RendererObject;

public class JSpriteActor extends Actor implements Entity, RendererObject {

    private final JSprite jSprite;

    public JSpriteActor(Texture texture) {
        this.jSprite = new JSprite(texture);
    }

    public JSpriteActor(Texture texture, float x, float y) {
        this.jSprite = new JSprite(texture);
        setPosition(x, y);
    }

    public JSpriteActor(Texture texture, float x, float y, float width, float height) {
        this.jSprite = new JSprite(texture);
        setBounds(x, y, width, height);

    }

    public JSpriteActor(TextureRegion region) {
        this.jSprite = new JSprite(region);
    }

    public JSpriteActor(JSprite sprite) {
        this.jSprite = new JSprite(sprite);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        this.jSprite.setBounds(getX(), getY(), getWidth(), getHeight());
        this.jSprite.draw(batch);
    }

    @Override
    public boolean needRender() {
        return this.jSprite.needRender();
    }

    @Override
    public boolean getData(String name) {
        return this.jSprite.getData(name);
    }

    @Override
    public Rectangle getRectangle() {
        return this.jSprite.getRectangle();
    }

    public void setTexture(Texture texture){
        this.jSprite.setTexture(texture);
    }

    public JSpriteActor setStatic(boolean value){
        this.jSprite.setStatic(value);

        return this;
    }

    @Override
    public boolean hasTransparent() {
        return this.jSprite.hasTransparent();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        //todo no need for it - bad solution, don't like it

    }
}
