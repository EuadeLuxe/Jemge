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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jemge.core.EngineModule;
import com.jemge.core.Jemge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputManager extends EngineModule implements InputProcessor {
    private static final Vector3 input_position = new Vector3();
    private static final Vector2 position_by_cam = new Vector2();

    private List<InputListener> listeners;

    private HashMap<Method, Object> keyDownListener;
    private HashMap<Method, Object> keyUpListener;

    @Override
    public void init() {
        listeners = new ArrayList<>();
        keyDownListener = new HashMap<>();
        keyUpListener = new HashMap<>();

        Gdx.input.setInputProcessor(this);
    }

    public void addListener(InputListener listener){
        listeners.add(listener);
    }

    public void removeListener(InputListener listener){
        listeners.remove(listener);
    }

    public void addKeyListener(KeyListener listener){

        for(Method method : listener.getClass().getMethods()){
            if(method.isAnnotationPresent(ListenKeyDown.class)){
                keyDownListener.put(method, listener);
            }
            if(method.isAnnotationPresent(ListenKeyUp.class)){
                keyUpListener.put(method, listener);
            }
        }
    }

    public void removeKeyListener(KeyListener listener){

        for(Method method : listener.getClass().getMethods()){
            if(method.isAnnotationPresent(ListenKeyDown.class)){
                keyDownListener.remove(method);
            }
            if(method.isAnnotationPresent(ListenKeyUp.class)){
                keyUpListener.remove(method);
            }
        }
    }

    public static Vector2 getInputPosition(){
        input_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        Jemge.renderer2D.getCamera().unproject(input_position);
        position_by_cam.set(input_position.x, input_position.y);

        return position_by_cam;
    }

    @Override
    public void update() {
        if(Gdx.input.isTouched()){
            for(InputListener listener : listeners){
                if(listener.getRectangle().contains(getInputPosition())){
                    listener.clicked();
                }
            }
        }
    }

    @Override
    public void dispose(){

    }

    @Override
    public boolean keyDown(int i) {
        for(Method method : keyDownListener.keySet()){
            if(method.getAnnotation(ListenKeyDown.class).key() == i){
                try {
                    method.invoke(keyDownListener.get(method));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        for(Method method : keyUpListener.keySet()){
            if(method.getAnnotation(ListenKeyUp.class).key() == i){
                try {
                    method.invoke(keyUpListener.get(method));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return true;
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        return true;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return true;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return true;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return true;
    }

    @Override
    public boolean scrolled(int i) {
        return true;
    }
}
