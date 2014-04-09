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
import com.jemge.j2d.IEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class ZoneBasedCulling implements ICullingSystem {
	public static float ZONE_SIZE = 1024;
	private final HashMap<CullingZone, ArrayList<IEntity>> zone_map;
	private final ArrayList<IEntity> dynamic_objects;
	private final ArrayList<IEntity> final_render_list;

	public ZoneBasedCulling() {
		this.zone_map = new HashMap<>();
		this.final_render_list = new ArrayList<>(256);
		this.dynamic_objects = new ArrayList<>(32);
	}

	public void putObject(IEntity object) {
		Profiler.start(this, "new object");

		if (!object.getData("static")) {
			this.dynamic_objects.add(object);
			return;
		}

		if (existZone(object) == null) {
			CullingZone zone = createZone(object);
			this.zone_map.put(zone, new ArrayList<IEntity>());
			this.zone_map.get(zone).add(object);
		} else {
			this.zone_map.get(existZone(object)).add(object);
		}

		Profiler.stop(this, "new object");
	}

	public void removeObject(IEntity object) {
		if (!object.getData("static")) {
			this.dynamic_objects.remove(object);
		} else {
			this.zone_map.get(existZone(object)).remove(object);
		}
	}

	private CullingZone existZone(IEntity object) {
		for (CullingZone zone : this.zone_map.keySet()) {
			if (zone.overlaps(object.getRectangle())) {
				return zone;
			}
		}

		return null;
	}

	private CullingZone createZone(IEntity object) {
		final CullingZone zone = new CullingZone(ZONE_SIZE, ZONE_SIZE,
				ZONE_SIZE, ZONE_SIZE);
		zone.setCenter(object.getX(), object.getY());

		return zone;
	}

	protected ArrayList<IEntity> getMyList(CullingZone zone) {
		return this.zone_map.get(zone);
	}

	public void cull(Rectangle camera_view) {
		Profiler.start(this, "cull");

		this.final_render_list.clear();
		for (CullingZone zone : this.zone_map.keySet()) {
			if (zone.overlaps(camera_view)) {
				zone.getCullingList(this, this.final_render_list);
			}
		}
		if (!this.dynamic_objects.isEmpty()) {
			this.final_render_list.addAll(this.dynamic_objects);
		}

		Profiler.stop(this, "cull");
	}

	public ArrayList<IEntity> getFinalRenderList() {
		return this.final_render_list;
	}

	@Override
	public void postRender() {
	}

}
