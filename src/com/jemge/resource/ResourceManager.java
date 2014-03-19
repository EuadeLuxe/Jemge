/*
 * Copyright [2014] @author file
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jemge.resource;

import com.jemge.core.EngineModule;

import java.util.HashMap;

public class ResourceManager extends EngineModule {

    private final HashMap<String, Resource> resourceList;

    public ResourceManager(){
        this.resourceList = new HashMap<>();
    }

    public void addResource(String name, Resource resource){
        this.resourceList.put(name, resource);
    }

    public Resource getResource(String name){
        return this.resourceList.get(name);
    }

    public TextureResource getTexture(String name){
        if(this.resourceList.get(name) instanceof TextureResource){
            return (TextureResource) this.resourceList.get(name);
        }

        throw new NullPointerException("This texture doesn't exist.");
    }

    public AudioResource getSound(String name){
        if(this.resourceList.get(name) instanceof AudioResource){
            return (AudioResource) this.resourceList.get(name);
        }

        throw new NullPointerException("This sound doesn't exist.");
    }

    @Override
    public void init() {}

    @Override
    public void update() {}

    @Override
    public void dispose() {
        for(Resource resource : this.resourceList.values()){
            if(resource instanceof TextureResource){
                ((TextureResource) resource).dispose();
            }
            if(resource instanceof AudioResource){
                ((AudioResource) resource).getSound().dispose();
            }
        }
    }
}
