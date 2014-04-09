package com.jemge.box2d.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.jemge.box2d.box2dLight.RayHandler;

public final class LightShader {
	public static final ShaderProgram createLightShader() {
		String gamma = "";
		if (RayHandler.getGammaCorrection())
			gamma = "sqrt";

		// final FileHandle VERTEXSHADER =
		// Gdx.files.classpath("com/jemge/box2d/shaders/light.vertex.glsl");
		// final FileHandle FRAGMENTSHADER =
		// Gdx.files.internal("com/jemge/box2d/shaders/light.fragment.glsl");

		final String VERTEXSHADER = "attribute vec4 vertex_positions;\n"
				+ "attribute vec4 quad_colors;\n" + "attribute float s;\n"
				+ "uniform mat4 u_projTrans;\n" + "varying vec4 v_color;\n"
				+ "void main()\n" + "{\n" + "   v_color = s * quad_colors;\n"
				+ "   gl_Position =  u_projTrans * vertex_positions;\n" + "}\n";

		final String FRAGMENTSHADER = "#ifdef GL_ES\n"
				+ "precision lowp float;\n" + "#define MED mediump\n"
				+ "#else\n" + "#define MED \n" + "#endif\n"
				+ "varying vec4 v_color;\n" + "void main()\n" + "{\n"
				+ "  gl_FragColor = " + gamma + "(v_color);\n" + "}";

		ShaderProgram.pedantic = false;
		ShaderProgram lightShader = new ShaderProgram(VERTEXSHADER,
				FRAGMENTSHADER);
		if (lightShader.isCompiled() == false) {
			Gdx.app.log("ERROR", lightShader.getLog());
		}

		return lightShader;
	}
}
