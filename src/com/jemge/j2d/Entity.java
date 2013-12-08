package com.jemge.j2d;

import com.badlogic.gdx.math.Rectangle;

public interface Entity {

    public float getX();
    public float getY();

    public float getHeight();
    public float getWidth();

    public boolean needRender();
    public boolean getData(String name);
    public Rectangle getRectangle();

}
