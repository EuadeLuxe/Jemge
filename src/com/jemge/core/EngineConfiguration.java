package com.jemge.core;

import com.jemge.j2d.culling.CullingSystem;
import com.jemge.j2d.culling.ZoneBasedCulling;

public class EngineConfiguration {

    public static CullingSystem cullingSystem = new ZoneBasedCulling();

    public static boolean debugMode = false;
}
