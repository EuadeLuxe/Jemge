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

package com.jemge.resource;

import com.jemge.core.EngineModule;

import java.util.HashMap;

public class ResourceManager extends EngineModule {
	private final HashMap<String, IResource> RESOURCELIST;

	public ResourceManager() {
		this.RESOURCELIST = new HashMap<>();
	}

	public void addResource(String name, IResource resource) {
		this.RESOURCELIST.put(name, resource);
	}

	public IResource getResource(String name) {
		return this.RESOURCELIST.get(name);
	}

	public TextureResource getTexture(String name) {
		if (this.RESOURCELIST.get(name) instanceof TextureResource) {
			return (TextureResource) this.RESOURCELIST.get(name);
		}

		throw new NullPointerException("This texture doesn't exist.");
	}

	public AudioResource getSound(String name) {
		if (this.RESOURCELIST.get(name) instanceof AudioResource) {
			return (AudioResource) this.RESOURCELIST.get(name);
		}

		throw new NullPointerException("This sound doesn't exist.");
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
		for (IResource resource : this.RESOURCELIST.values()) {
			if (resource instanceof TextureResource) {
				((TextureResource) resource).dispose();
			}
			if (resource instanceof AudioResource) {
				((AudioResource) resource).getSound().dispose();
			}
		}
	}
}