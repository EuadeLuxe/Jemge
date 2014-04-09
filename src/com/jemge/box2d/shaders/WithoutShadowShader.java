package com.jemge.box2d.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class WithoutShadowShader {
	public static final ShaderProgram createWithoutShadowShader() {
		final FileHandle VERTEXSHADER = Gdx.files
				.classpath("com/jemge/box2d/shaders/withoutshadow.vertex.glsl");
		final FileHandle FRAGMENTSHADER = Gdx.files
				.internal("com/jemge/box2d/shaders/withoutshadow.fragment.glsl");

		ShaderProgram.pedantic = false;
		ShaderProgram withoutShadowShader = new ShaderProgram(VERTEXSHADER,
				FRAGMENTSHADER);
		if (withoutShadowShader.isCompiled() == false) {
			Gdx.app.log("ERROR", withoutShadowShader.getLog());

		}

		return withoutShadowShader;
	}
}
