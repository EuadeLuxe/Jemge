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
	public static final float ZONE_SIZE = 1024;
	private final HashMap<CullingZone, ArrayList<IEntity>> ZONE_MAP;
	private final ArrayList<IEntity> DYNAMIC_OBJECTS;
	private final ArrayList<IEntity> FINAL_RENDER_LIST;

	public ZoneBasedCulling() {
		this.ZONE_MAP = new HashMap<CullingZone, ArrayList<IEntity>>();
		this.DYNAMIC_OBJECTS = new ArrayList<IEntity>(32);
		this.FINAL_RENDER_LIST = new ArrayList<IEntity>(256);
	}

	public void putObject(IEntity object) {
		Profiler.start(this, "new object");

		if (!object.getData("static")) {
			this.DYNAMIC_OBJECTS.add(object);
			return;
		}

		if (existZone(object) == null) {
			CullingZone zone = createZone(object);
			this.ZONE_MAP.put(zone, new ArrayList<IEntity>());
			this.ZONE_MAP.get(zone).add(object);
		} else {
			this.ZONE_MAP.get(existZone(object)).add(object);
		}

		Profiler.stop(this, "new object");
	}

	public void removeObject(IEntity object) {
		if (!object.getData("static")) {
			this.DYNAMIC_OBJECTS.remove(object);
		} else {
			this.ZONE_MAP.get(existZone(object)).remove(object);
		}
	}

	private CullingZone existZone(IEntity object) {
		for (CullingZone zone : this.ZONE_MAP.keySet()) {
			if (zone.overlaps(object.getRectangle())) {
				return zone;
			}
		}

		return null;
	}

	private CullingZone createZone(IEntity object) {
		final CullingZone ZONE = new CullingZone(ZONE_SIZE, ZONE_SIZE,
				ZONE_SIZE, ZONE_SIZE);
		ZONE.setCenter(object.getX(), object.getY());

		return ZONE;
	}

	protected ArrayList<IEntity> getMyList(CullingZone zone) {
		return this.ZONE_MAP.get(zone);
	}

	public void cull(Rectangle camera_view) {
		Profiler.start(this, "cull");

		this.FINAL_RENDER_LIST.clear();
		for (CullingZone zone : this.ZONE_MAP.keySet()) {
			if (zone.overlaps(camera_view)) {
				zone.getCullingList(this, this.FINAL_RENDER_LIST);
			}
		}
		if (!this.DYNAMIC_OBJECTS.isEmpty()) {
			this.FINAL_RENDER_LIST.addAll(this.DYNAMIC_OBJECTS);
		}

		Profiler.stop(this, "cull");
	}

	public ArrayList<IEntity> getFinalRenderList() {
		return this.FINAL_RENDER_LIST;
	}

	@Override
	public void postRender() {
	}

}
