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

package com.jemge.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.jemge.core.EngineModule;

import java.util.HashMap;

public class Physics2D extends EngineModule {
	private static HashMap<String, World> worlds;
	public static int positionInteractions = 2;
	public static int velocityInteractions = 2;
	public static float timeStep = 1f / 30f;
	private static String main_world = "main";

	@Override
	public void init() {
		worlds = new HashMap<String, World>(1);
		worlds.put("main", new World(new Vector2(0, -20f), true));
	}

	@Override
	public void update() {
		for (World world : worlds.values()) {
			world.step(timeStep, velocityInteractions, positionInteractions);
		}
	}

	public static World newWorld(String name, World world) {
		worlds.put(name, world);

		return world;
	}

	public static World getWorld(String name) {
		return worlds.get(name);
	}

	public static World getMainWorld() {
		return worlds.get(main_world);
	}

	public static void setMainWorld(String name) {
		main_world = name;
	}

	@Override
	public void dispose() {
		for (World world : worlds.values()) {
			world.dispose();
		}
	}
}