package com.jemge.resource;

public interface Resource {

    public enum ResourceType{
        Texture, Sound
    }

    public ResourceType getType();

    public String getPathAndName();
}
