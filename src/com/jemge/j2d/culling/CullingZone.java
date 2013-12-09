package com.jemge.j2d.culling;

import com.badlogic.gdx.math.Rectangle;
import com.jemge.j2d.Entity;

import java.util.ArrayList;

public class CullingZone extends Rectangle {
    public CullingZone(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public ArrayList<Entity> getCullingList(ZoneBasedCulling culling){
        final ArrayList<Entity> final_render_list = new ArrayList<>(128);
        for(Entity entity : culling.getMyList(this)){
            if(entity.needRender()){
                final_render_list.add(entity);
            }
        }

        return final_render_list;
    }
}
