package com.jemge.j2d.culling;

import com.badlogic.gdx.math.Rectangle;
import com.jemge.j2d.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZoneBasedCulling {
    public static final float ZONE_SIZE = 1024;
    private final HashMap<CullingZone, ArrayList<Entity>> zone_map;
    private final ArrayList<Entity> dynamic_objects;
    private final ArrayList<Entity> final_render_list;

    public ZoneBasedCulling() {
        zone_map = new HashMap<>();
        final_render_list = new ArrayList<>(256);
        dynamic_objects = new ArrayList<>(32);



    }

    public void putObject(Entity object) {
        if (!object.getData("static")) {
            dynamic_objects.add(object);
            return;
        }

        if(existZone(object) == null){
            CullingZone zone = createZone(object);
            zone_map.put(zone, new ArrayList<Entity>());
            zone_map.get(zone).add(object);
            return;
        }else{
            zone_map.get(existZone(object)).add(object);
        }
    }

    public void removeObject(Entity object){
        if (!object.getData("static")) {
            dynamic_objects.remove(object);
        }else{
            zone_map.get(existZone(object)).remove(object);
        }
    }

    private CullingZone existZone(Entity object) {
        for(CullingZone zone : zone_map.keySet()){
            if(zone.overlaps(object.getRectangle())){
                return zone;
            }
        }

        return null;
    }

    private CullingZone createZone(Entity object){
        final CullingZone zone = new CullingZone(ZONE_SIZE, ZONE_SIZE, ZONE_SIZE, ZONE_SIZE);
        zone.setCenter(object.getX(), object.getY());

        return zone;
    }

    protected ArrayList<Entity> getMyList(CullingZone zone){
        return zone_map.get(zone);
    }

    public void cull(Rectangle camera_view){
        final_render_list.clear();
        for(CullingZone zone : zone_map.keySet()){
            if(zone.overlaps(camera_view)){
                final_render_list.addAll(zone.getCullingList(this));
            }
        }
        final_render_list.addAll(dynamic_objects);
    }

    public boolean testCull(Entity entity){
        return final_render_list.contains(entity);
    }

}
