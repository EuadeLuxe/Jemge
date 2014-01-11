package com.jemge.resource;

import com.badlogic.gdx.audio.Sound;

public abstract class AudioResource implements Resource, Sound{
    private final String name;

    public AudioResource(String name){
        this.name = name;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.Sound;
    }

    @Override
    public String getName() {
        return name;
    }
}
