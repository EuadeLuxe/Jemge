package com.jemge.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureResource extends Texture implements Resource{
    private final String name;

    public TextureResource(String name){
        super(Gdx.files.internal(name));
        this.name = name;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.Texture;
    }

    @Override
    public String getPathAndName() {
        return name;
    }

}
