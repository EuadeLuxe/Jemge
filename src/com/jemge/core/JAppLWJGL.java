package com.jemge.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;

import java.awt.*;

public class JAppLWJGL extends LwjglApplication {
	public JAppLWJGL(ApplicationListener listener) {
		super(listener);
	}
	
	public JAppLWJGL(ApplicationListener listener, boolean useGL2, Canvas canvas) {
		super(listener, useGL2, canvas);
	}
	
	public JAppLWJGL(ApplicationListener listener, JConfig config) {
		super(listener, config);
	}
	
	public JAppLWJGL(ApplicationListener listener, JConfig config, Canvas canvas) {
		super(listener, config, canvas);
	}
	
	public JAppLWJGL(ApplicationListener listener, JConfig config, LwjglGraphics graphics) {
		super(listener, config, graphics);
	}
	
	public JAppLWJGL(ApplicationListener listener, String title, int width, int height, boolean useGL2) {
		super(listener, title, width, height, useGL2);
	}
}
