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

package com.jemge.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jemge.core.EngineModule;
import com.jemge.core.Jemge;

import java.util.ArrayList;
import java.util.List;

public class InputManager implements EngineModule {
    private static final Vector3 input_position = new Vector3();
    private static final Vector2 position_by_cam = new Vector2();

    private List<InputListener> listeners;

    @Override
    public void init() {
        listeners = new ArrayList<>();

    }

    public void addListener(InputListener listener){
        listeners.add(listener);
    }

    public void removeListener(InputListener listener){
        listeners.remove(listener);
    }

    public static Vector2 getPositionByCam(){
        input_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        Jemge.renderer2D.getCamera().unproject(input_position);
        position_by_cam.set(input_position.x, input_position.y);

        return position_by_cam;
    }

    @Override
    public void update() {
        if(Gdx.input.isTouched()){
            for(InputListener listener : listeners){
                if(listener.getRectangle().contains(getPositionByCam())){
                    listener.clicked();
                }
            }
        }

    }
}
