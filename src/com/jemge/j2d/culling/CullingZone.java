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
import com.jemge.j2d.IEntity;

import java.util.List;

public class CullingZone extends Rectangle {
	public CullingZone(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public void getCullingList(ZoneBasedCulling culling, List<IEntity> list) {
		for (IEntity entity : culling.getMyList(this)) {
			if (entity.needRender()) {
				list.add(entity);
			}
		}
	}
}