package com.jemge.j2d.culling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.jemge.core.Jemge;
import com.jemge.j2d.Entity;

import java.util.ArrayList;
import java.util.List;

public class ScissorStackClipping implements CullingSystem{
    final private List<Entity> entities;
    final Rectangle scissors = new Rectangle();;

    public ScissorStackClipping(){
        entities = new ArrayList<>();
    }

    @Override
    public void putObject(Entity entity) {
        entities.add(entity);

    }

    @Override
    public void removeObject(Entity entity) {
        entities.remove(entity);

    }

    @Override
    public void cull(Rectangle camera_view) {
        ScissorStack.calculateScissors(Jemge.renderer2D.getCamera(), Jemge.renderer2D.getSpriteBatch().getTransformMatrix(),
                camera_view, scissors);
        ScissorStack.pushScissors(scissors);
    }

    @Override
    public List<Entity> getFinalRenderList() {
        return entities;
    }

    @Override
    public void postRender(){
        ScissorStack.popScissors();
    }
}
