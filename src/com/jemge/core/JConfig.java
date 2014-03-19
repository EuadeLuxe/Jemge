package com.jemge.core;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.files.FileHandle;

public class JConfig extends LwjglApplicationConfiguration {
	protected String settings = "";

    public JConfig() {
		this.title = "JEMGEngine";
	}
	
	public void setTitle(String name) {
		this.title = name;
	}
	
	public void setSize(int width, int height) {
		this.width	= width;
		this.height	= height;
	}
	
	public void setWidth(int width) {
		this.width	= width;
	}
	
	public void setHeight(int height) {
		this.height	= height;
	}
	
	public void setFullscreen() {
		this.fullscreen = true;
	}

	public void loadSettings(String file) {
		this.settings = file;
		loadSettings();
	}
	
	public void loadSettings() {
		if(Gdx.files == null) {
			Gdx.files = new LwjglFiles();
		}
		
		try {
			FileHandle handle	= Gdx.files.internal(this.settings);
			String content		= handle.readString().replace("\r", "");
			String[] lines		= content.split("\n");
			for(String line : lines) {
				String[] values	= line.split(" ");
				switch(values[0]) {
					case "Render.ResolutionWidth":
						this.width = Integer.parseInt(values[1]);
					break;
					case "Render.ResolutionHeight":
						this.height = Integer.parseInt(values[1]);
					break;
					case "Render.VSyncEnabled":
						this.vSyncEnabled = Integer.parseInt(values[1]) == 1;
					break;
					case "Render.VSync":
						//this.useCPUSynch = (Integer.parseInt(values[1]) == 1);
					break;
					case "Render.FullscreenEnabled":
						this.fullscreen = Integer.parseInt(values[1]) == 1;
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//this. = true;
		//
	}
}
