package com.jemge.resource;

import java.util.HashMap;

public class ResourceManager {

    private final HashMap<String, Resource> resourceList;

    public ResourceManager(){
        resourceList = new HashMap<>();
    }

    public void addResource(String name, Resource resource){
        resourceList.put(name, resource);
    }

    public Resource getResource(String name){
        return resourceList.get(name);
    }

    public TextureResource getTexture(String name){
        if(resourceList.get(name) instanceof TextureResource){
            return (TextureResource) resourceList.get(name);
        }

        throw new NullPointerException();
    }
}
