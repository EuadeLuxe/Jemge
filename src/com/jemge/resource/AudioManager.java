package com.jemge.resource;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.jemge.core.EngineModule;
import com.jemge.core.Jemge;

import java.util.ArrayList;
import java.util.HashMap;

public class AudioManager implements EngineModule {
    private ArrayList<AudioResource> sound_list;

    private static final Vector2 temp1 = new Vector2();

    @Override
    public void init() {
        sound_list = new ArrayList<AudioResource>();
    }

    public Long playSoundAtPosition(AudioResource sound, Vector2 position){
        if((sound == null) || (position == null)){
            throw new NullPointerException("Position and/or sound can't be null!");
        }
        Long id = sound.play();

        Jemge.renderer2D.cameraView.getCenter(temp1);

        if(position.x > temp1.x){
            sound.setPan(id, 1, 0.5f);
        }else if(position.x < temp1.x){
            sound.setPan(id, 0.5f, 1);
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
