package com.jemge.resource;

public interface IResource {

	public enum ResourceType {
		TEXTURE, SOUND
	}

	public ResourceType getType();

	public String getName();
}
