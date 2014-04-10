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

package com.jemge.core;

import com.jemge.box2d.Physics2D;
import com.jemge.core.debug.Profiler;
import com.jemge.input.InputManager;
import com.jemge.j2d.JUIManager;
import com.jemge.j2d.Renderer2D;
import com.jemge.resource.AudioManager;
import com.jemge.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * The core of the engine. Initializes the components of the engine and dispose
 * them.
 * 
 * @author MrBarsack
 * @see Jemge
 */

public class Engine {
	private final List<EngineModule> MODULES;

	public Engine() {
		this.MODULES = new ArrayList<>();
		this.MODULES.add(new Physics2D());
		this.MODULES.add(new InputManager());
		this.MODULES.add(new ResourceManager());
		this.MODULES.add(new JUIManager());

		for (EngineModule module : this.MODULES) {
			module.init();
		}

		Jemge.engine = this;
		Jemge.renderer2D = new Renderer2D();
		Jemge.manager = getResourceManager();
		Jemge.inputManager = getInputManager();
		Jemge.audio = new AudioManager();
		// game.setScreen(new Splash(game));
	}

	public void update() {
		for (EngineModule module : this.MODULES) {
			module.update();
		}
	}

	public void dispose() {
		Jemge.renderer2D.dispose();

		for (EngineModule module : this.MODULES) {
			module.dispose();
		}

		Profiler.getResults();
	}

	public Physics2D getPhysics2D() {
		for (EngineModule module : this.MODULES) {
			if (module instanceof Physics2D) {
				return (Physics2D) module;
			}
		}

		throw new NullPointerException(
				"Did not found the Physics2d Module, where is it?");
	}

	public InputManager getInputManager() {
		for (EngineModule module : this.MODULES) {
			if (module instanceof InputManager) {
				return (InputManager) module;
			}
		}

		throw new NullPointerException(
				"Did not found the InputManager, where is it?");
	}

	public JUIManager getJUIManager() {
		for (EngineModule module : this.MODULES) {
			if (module instanceof JUIManager) {
				return (JUIManager) module;
			}
		}

		throw new NullPointerException(
				"Did not found the InputManager, where is it?");
	}

	public ResourceManager getResourceManager() {
		for (EngineModule module : this.MODULES) {
			if (module instanceof ResourceManager) {
				return (ResourceManager) module;
			}
		}

		throw new NullPointerException(
				"Did not found the ResourceManager, where is it?");
	}

	public EngineModule getModule(String name) {
		for (EngineModule module : this.MODULES) {
			if (module.getName().equals(name)) {
				return module;
			}
		}

		throw new NullPointerException("Did not found the module, where is it?");
	}

	public EngineModule addModule(EngineModule module) {
		this.MODULES.add(module);

		return module;
	}
}