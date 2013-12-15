package com.jemge.core.system;

import com.jemge.j2d.Entity;
import com.jemge.j2d.RendererObject;

public interface UpdateListener<T> {

    public void update(T object);
}
