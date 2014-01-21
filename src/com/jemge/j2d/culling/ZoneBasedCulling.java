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

package com.jemge.j2d.culling;

import com.badlogic.gdx.math.Rectangle;
import com.jemge.core.debug.Profiler;
import com.jemge.j2d.Entity;

import java.util.ArrayList;
import java.util.HashMap;

public class ZoneBasedCulling implements CullingSystem {
    public static float ZONE_SIZE = 1024;
    private final HashMap<CullingZone, ArrayList<Entity>> zone_map;
    private final ArrayList<Entity> dynamic_objects;
    private final ArrayList<Entity> final_render_list;

    public ZoneBasedCulling() {
        zone_map = new HashMap<>();
        final_render_list = new ArrayList<>(256);
        dynamic_objects = new ArrayList<>(32);

    }

    public void putObject(Entity object) {
        Profiler.start(this, "new object");

        if (!object.getData("static")) {
            dynamic_objects.add(object);
            return;
        }

        if(existZone(object) == null){
            CullingZone zone = createZone(object);
            zone_map.put(zone, new ArrayList<Entity>());
            zone_map.get(zone).add(object);
        }else{
            zone_map.get(existZone(object)).add(object);
        }

        Profiler.stop(this, "new object");
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
        Profiler.start(this, "cull");

        final_render_list.clear();
        for(CullingZone zone : zone_map.keySet()){
            if(zone.overlaps(camera_view)){
                zone.getCullingList(this, final_render_list);
            }
        }
        if(dynamic_objects.size() != 0){
            final_render_list.addAll(dynamic_objects);
        }

        Profiler.stop(this, "cull");
    }

    public ArrayList<Entity> getFinalRenderList(){
        return final_render_list;
    }

}
