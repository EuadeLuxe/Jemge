package com.jemge.core.debug;

import com.badlogic.gdx.Gdx;

import java.util.Collections;
import java.util.HashMap;

public class Profiler {
    private static HashMap<String, ProfilerObject> results = new HashMap<>();
    public static boolean debugOn = true;
    public static boolean logPerSecond = false;

    private static long startTime = System.currentTimeMillis();;

    public static void start(Object object, String name){
        if(!debugOn){
            return;
        }
        if(logPerSecond){
            if (System.currentTimeMillis() - startTime > 1000) {
                getResults();
                startTime = System.currentTimeMillis();
            }
        }

        ProfilerObject profiler = null;
        if(results.keySet().contains(object.getClass().getSimpleName() + name)){
            profiler = results.get(object.getClass().getSimpleName() + name);
        }else{
            profiler = new ProfilerObject();
        }
        profiler.name = name;
        profiler.class_name = object.getClass().getSimpleName();
        profiler.start_time = System.nanoTime();

        results.put(object.getClass().getSimpleName() + name, profiler);
    }

    public static void stop(Object object, String name){
        if(!debugOn){
            return;
        }
        results.get(object.getClass().getSimpleName() + name).final_time = System.nanoTime()
                - results.get(object.getClass().getSimpleName() + name).start_time;
    }

    public static void getResults(){
        if(!debugOn){
            return;
        }

        Gdx.app.log("Profiler List size", String.valueOf(results.size()));
        for(ProfilerObject object : results.values()){
            Gdx.app.log(object.class_name + " " + object.name, String.valueOf(object.final_time / 100));
        }
        //todo: not nice output a.t.m. ...
    }

}
