package com.jemge.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jemge.core.Jemge;
import com.jemge.j2d.Renderer2D;

import java.util.ArrayList;
import java.util.List;

public class InputManager {
    private final List<InputListener> listeners;

    public InputManager(){
        listeners = new ArrayList<>();
    }

    public void addListener(InputListener listener){
        listeners.add(listener);

    }

    public void removeListener(InputListener listener){
        listeners.remove(listener);
    }

    public void testAll(){
        if(Gdx.input.isTouched()){
            for(InputListener listener : listeners){
                if(listener.getRectangle().contains(getPositionByCam())){
                   listener.clicked();
                }
            }
        }
    }

    protected Vector2 getPositionByCam(){
        final Vector3 input_position = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        final Vector2 position_by_cam = new Vector2();
        Jemge.renderer2D.getCamera().unproject(input_position);
        position_by_cam.set(input_position.x, input_position.y);

        return position_by_cam;
    }
}
