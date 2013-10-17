package com.jemge.core;
import java.awt.Canvas;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;

public class JApp extends LwjglApplication {
	public JApp(ApplicationListener listener) {
		super(listener);
	}
	
	public JApp(ApplicationListener listener, boolean useGL2, Canvas canvas) {
		super(listener, useGL2, canvas);
	}
	
	public JApp(ApplicationListener listener, JConfig config) {
		super(listener, config);
	}
	
	public JApp(ApplicationListener listener, JConfig config, Canvas canvas) {
		super(listener, config, canvas);
	}
	
	public JApp(ApplicationListener listener, JConfig config, LwjglGraphics graphics) {
		super(listener, config, graphics);
	}
	
	public JApp(ApplicationListener listener, String title, int width, int height, boolean useGL2) {
		super(listener, title, width, height, useGL2);
	}
}
