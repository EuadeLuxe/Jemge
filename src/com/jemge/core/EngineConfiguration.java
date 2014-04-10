package com.jemge.core;

import com.jemge.j2d.culling.ICullingSystem;
import com.jemge.j2d.culling.ZoneBasedCulling;

public class EngineConfiguration {
	public static ICullingSystem cullingSystem = new ZoneBasedCulling();
	public static boolean debugMode = false;
	public static boolean debugPhysic = false;
}