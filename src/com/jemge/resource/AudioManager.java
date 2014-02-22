package com.jemge.resource;

import com.badlogic.gdx.math.Vector2;
import com.jemge.core.EngineModule;
import com.jemge.core.Jemge;

public class AudioManager extends EngineModule {
    private static final Vector2 temp1 = new Vector2();

    @Override
    public void init() {
    }

    public Long playSoundAtPosition(AudioResource sound, Vector2 position){
        if((sound == null) || (position == null)){
            throw new NullPointerException("Position and/or sound can't be null!");
        }
        Long id = sound.getSound().play(1.0f);

        Jemge.renderer2D.cameraView.getCenter(temp1);

        if(position.x > temp1.x){
            sound.getSound().setPan(id, 0.25f, 0.8f); //todo Should be relative to difference...
        }else if(position.x < temp1.x){
            sound.getSound().setPan(id, -0.25f, 0.8f);
        }

        return id;
    }

    @Override
    public void update(){

    }

    @Override
    public void dispose() {

    }
}
