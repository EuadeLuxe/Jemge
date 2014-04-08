package com.jemge.core.debug;

class ProfilerObject {
	protected long start_time;
	protected long final_time;

	protected String name;
	protected String class_name;

	public ProfilerObject() {
		this.start_time = System.nanoTime();
	}
}
