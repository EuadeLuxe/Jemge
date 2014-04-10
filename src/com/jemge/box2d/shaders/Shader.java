package com.jemge.box2d.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class Shader {
	public static final ShaderProgram createShader(String name) {
		final FileHandle VERTEXSHADER = Gdx.files
				.classpath("com/jemge/box2d/shaders/" + name + ".vertex.glsl");
		final FileHandle FRAGMENTSHADER = Gdx.files
				.internal("com/jemge/box2d/shaders/" + name + ".fragment.glsl");

		ShaderProgram.pedantic = false;
		ShaderProgram shader = new ShaderProgram(VERTEXSHADER, FRAGMENTSHADER);
		if (shader.isCompiled() == false) {
			Gdx.app.log("ERROR", shader.getLog());
		}

		return shader;
	}
}
