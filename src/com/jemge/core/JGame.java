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

package com.jemge.core;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.jemge.j2d.Renderer2D;

/**
 * Use this instead of {@link Game}. Calls the required functions of the engine.
 *
 * @author MrBarsack
 */

public class JGame extends Game {
    @Override
    public void create() {
        new Engine(this);
    }

    @Override
    public void render() {
    	super.render();
        Jemge.engine.update();
        Jemge.renderer2D.render();
    }

    @Override
    public void dispose() {
        Jemge.engine.dispose();
    }

    public Camera getCamera() {
        return Jemge.renderer2D.getCamera();
    }
}
