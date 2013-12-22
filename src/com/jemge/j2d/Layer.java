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

package com.jemge.j2d;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    private final List<RendererObject> rendererObjects;

    public Layer() {
        rendererObjects = new ArrayList<>();
    }

    public RendererObject addObject(RendererObject rend) {
        rendererObjects.add(rend);

        return rend;
    }

    public void deleteObject(RendererObject rend) {
        rendererObjects.remove(rend);
    }

    public List<RendererObject> getRendererObjects() {
        return rendererObjects;
    }
}
