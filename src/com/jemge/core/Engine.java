/*
 * Copyright [2013] @author file
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


import com.jemge.box2d.Physics2D;
import com.jemge.j2d.Renderer2D;

/**
 * The core of the engine. Initializes the components of the engine and dispose them.
 *
 * @author MrBarsack
 * @see Jemge
 */

public class Engine {
    private final EngineModule[] modules;

    public Engine(JGame game) {
        modules = new EngineModule[]{
                new Physics2D()
        };
        for(EngineModule module : modules){
            module.init();
        }

        Jemge.engine = this;
        Jemge.renderer2D = new Renderer2D();
        //game.setScreen(new Splash(game));
    }

    public void update() {
        for(EngineModule module : modules){
            module.update();
        }

    }

    public void dispose() {

        Renderer2D.getRenderer2D().dispose();
    }
}
