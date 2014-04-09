package com.jemge.box2d.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class ShadowShader {
	public static final ShaderProgram createShadowShader() {
		final FileHandle VERTEXSHADER = Gdx.files
				.classpath("com/jemge/box2d/shaders/shadow.vertex.glsl");
		final FileHandle FRAGMENTSHADER = Gdx.files
				.internal("com/jemge/box2d/shaders/shadow.fragment.glsl");

		ShaderProgram.pedantic = false;
		ShaderProgram shadowShader = new ShaderProgram(VERTEXSHADER,
				FRAGMENTSHADER);
		if (shadowShader.isCompiled() == false) {
			Gdx.app.log("ERROR", shadowShader.getLog());

		}

		return shadowShader;
	}
}
