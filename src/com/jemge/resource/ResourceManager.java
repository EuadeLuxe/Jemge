/*
 * Copyright [2013] @author file
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

    public AudioResource getSound(String name){
        if(resourceList.get(name) instanceof AudioResource){
            return (AudioResource) resourceList.get(name);
        }

        throw new NullPointerException();
    }
}
