package com.jemge.j2d.culling;

import com.badlogic.gdx.math.Rectangle;
import com.jemge.j2d.Entity;

import java.util.List;

public interface CullingSystem {
    public void putObject(Entity entity);

    public void removeObject(Entity entity);

    public void cull(Rectangle camera_view);

    public List<Entity> getFinalRenderList();
}
