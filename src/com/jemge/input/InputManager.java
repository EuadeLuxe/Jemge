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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
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
    private static final Vector3 INPUT_POSITION = new Vector3();
    private static final Vector2 POSITION_BY_CAM = new Vector2();

    private List<IInputListener> listeners;

    private HashMap<Method, Object> keyDownListener;
    private HashMap<Method, Object> keyUpListener;
    private HashMap<Method, Object> whileDownListener;
    private HashMap<Method, Object> touchDownListener;
    private HashMap<Method, Object> touchUpListener;

    @Override
    public void init() {
        this.listeners = new ArrayList<>();
        this.keyDownListener = new HashMap<>();
        this.keyUpListener = new HashMap<>();
        this.whileDownListener = new HashMap<>();
        this.touchDownListener = new HashMap<>();
        this.touchUpListener = new HashMap<>();

        Gdx.input.setInputProcessor(this);
    }

    public void addListener(IInputListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(IInputListener listener) {
        this.listeners.remove(listener);
    }

    public void addKeyListener(IKeyListener listener) {

        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(ListenKeyDown.class)) {
                this.keyDownListener.put(method, listener);
            }
            if (method.isAnnotationPresent(ListenKeyUp.class)) {
                this.keyUpListener.put(method, listener);
            }
            if (method.isAnnotationPresent(ListenWhilePressed.class)) {
                this.whileDownListener.put(method, listener);
            }
            if (method.isAnnotationPresent(ListenTouchDown.class)) {
                this.touchDownListener.put(method, listener);
            }
            if(method.isAnnotationPresent(ListenTouchUp.class)){
                this.touchUpListener.put(method, listener);
            }
        }
    }

    public void removeKeyListener(IKeyListener listener) {

        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(ListenKeyDown.class)) {
                this.keyDownListener.remove(method);
            }
            if (method.isAnnotationPresent(ListenKeyUp.class)) {
                this.keyUpListener.remove(method);
            }
            if (method.isAnnotationPresent(ListenWhilePressed.class)) {
                this.whileDownListener.remove(method);
            }
            if (method.isAnnotationPresent(ListenTouchDown.class)) {
                this.touchDownListener.remove(method);
            }
            if(method.isAnnotationPresent(ListenTouchUp.class)){
                this.touchUpListener.put(method, listener);
            }
        }
    }

    public static Vector2 getInputPosition() {
        INPUT_POSITION.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        ((Camera) Jemge.renderer2D.getCamera()).unproject(INPUT_POSITION);
        POSITION_BY_CAM.set(INPUT_POSITION.x, INPUT_POSITION.y);

        return POSITION_BY_CAM;
    }

    @Override
    public void update() {
        if (Gdx.app.getType() == Application.ApplicationType.Android
                || Gdx.app.getType() == Application.ApplicationType.iOS) {
            return;
        }

        for (Method method : this.whileDownListener.keySet()) {
            if (Gdx.input.isKeyPressed(method.getAnnotation(
                    ListenWhilePressed.class).key())) {
                try {
                    method.invoke(this.whileDownListener.get(method));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int i) {
        Jemge.engine.getJUIManager().getStage().keyDown(i);
        for (Method method : this.keyDownListener.keySet()) {
            if (method.getAnnotation(ListenKeyDown.class).key() == i) {
                try {
                    method.invoke(this.keyDownListener.get(method));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        Jemge.engine.getJUIManager().getStage().keyUp(i);
        for (Method method : this.keyUpListener.keySet()) {
            if (method.getAnnotation(ListenKeyUp.class).key() == i) {
                try {
                    method.invoke(this.keyUpListener.get(method));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        Jemge.engine.getJUIManager().getStage().keyTyped(c);
        return true;
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        Jemge.engine.getJUIManager().getStage().touchDown(i, i2, i3, i4);
        for (int a = 0; a < this.listeners.size(); a++) {
            if (this.listeners.get(a).getRectangle()
                    .contains(getInputPosition())) {
                this.listeners.get(a).clicked();
            }
        }

        for (Method method : this.touchDownListener.keySet()) {
            try {
                method.invoke(this.touchDownListener.get(method));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        Jemge.engine.getJUIManager().getStage().touchUp(i, i2, i3, i4);

        for (Method method : this.touchUpListener.keySet()) {
            try {
                method.invoke(this.touchUpListener.get(method));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        Jemge.engine.getJUIManager().getStage().touchDragged(i, i2, i3);
        return true;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        Jemge.engine.getJUIManager().getStage().mouseMoved(i, i2);
        return true;
    }

    @Override
    public boolean scrolled(int i) {
        Jemge.engine.getJUIManager().getStage().scrolled(i);
        return true;
    }
}