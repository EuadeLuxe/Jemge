package com.jemge.j2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Background {
    private Color color;
    private Texture texture;

    public Background(){
        color = new Color();
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color.set(color);
    }

    public void setTexture(Texture texture){
        this.texture = texture;

        Gdx.app.log("JEMGEngine:", "Not supported yet.");
    }
}
