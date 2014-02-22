package com.jemge.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jemge.j2d.Renderer2D;

public abstract class JScreen implements Screen {
    private Renderer2D renderer2D;

    @Override
    public void resize(int i, int i2) {

    }

    @Override
    public void show() {
        if(renderer2D == null){
            renderer2D = new Renderer2D();
        }
        Jemge.renderer2D = renderer2D;
    }

    @Override
    public void hide(){

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public final void dispose() {
        Jemge.engine.dispose();
    }

    public OrthographicCamera getCamera() {
        return Jemge.renderer2D.getCamera();
    }

    public Renderer2D getRenderer2D(){
        return renderer2D;
    }
}
