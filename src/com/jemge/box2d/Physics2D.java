package com.jemge.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.jemge.core.EngineModule;

import java.util.HashMap;


public class Physics2D implements EngineModule{

    private static HashMap<String, World> worlds;

    public static int positionInteractions = 2;
    public static int velocityInteractions = 2;
    public static float timeStep = 1 / 30f;

    @Override
    public void init() {
        worlds = new HashMap(1);
        worlds.put("main", new World(new Vector2(0, -15f), true));
    }

    @Override
    public void update() {
        for(World world : worlds.values()){
            world.step(timeStep, velocityInteractions, positionInteractions);
        }
    }

    public static World newWorld(String name, World world){
        worlds.put(name, world);

        return world;
    }

    public static World getWorld(String name){
        return worlds.get(name);
    }

    public static World getMainWorld(){
        return worlds.get("main");
    }
}
