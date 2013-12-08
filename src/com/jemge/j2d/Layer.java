package com.jemge.j2d;

import java.util.ArrayList;
import java.util.List;

public class Layer {

    private final List<RendererObject> rendererObjects;

    public Layer() {
        rendererObjects = new ArrayList<RendererObject>(1);
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
