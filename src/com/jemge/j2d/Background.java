package com.jemge.j2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jemge.core.Jemge;

public class Background {
    private Color color;
    private JSprite texture;
    private MODE mode;

    private enum MODE {
        TEXTURE, COLOR
    }

    public Background() {
        color = new Color();
        color.set(Color.BLACK);
    }

    public void update(SpriteBatch spriteBatch) {
        if (mode == MODE.TEXTURE) {
            final OrthographicCamera camera = Jemge.renderer2D.getCamera();

            texture.setBounds(camera.position.x - camera.viewportWidth, camera.position.y - camera.viewportHeight
                    , camera.viewportWidth * 2, camera.viewportHeight * 2);

            texture.render(spriteBatch);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);

        mode = MODE.COLOR;
    }

    public void setTexture(Texture texture) {
        if(this.texture == null){
            this.texture = new JSprite(texture, 0, 0);
        }else{
            this.texture.setTexture(texture);
        }

        Jemge.renderer2D.addLayer(new Layer(), -1);
        mode = MODE.TEXTURE;
    }
}
