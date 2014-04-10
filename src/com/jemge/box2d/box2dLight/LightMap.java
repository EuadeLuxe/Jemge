package com.jemge.box2d.box2dLight;

import com.jemge.box2d.shaders.Shader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

class LightMap {
	private ShaderProgram shadowShader;
	FrameBuffer frameBuffer;
	private Mesh lightMapMesh;

	private FrameBuffer pingPongBuffer;

	private RayHandler rayHandler;
	private ShaderProgram withoutShadowShader;
	private ShaderProgram blurShader;
	private ShaderProgram diffuseShader;

	boolean lightMapDrawingDisabled;

	private int fboWidth;
	private int fboHeight;

	public void render() {
		boolean needed = this.rayHandler.lightRenderedLastFrame > 0;
		// this way lot less binding
		if (needed && this.rayHandler.blur) {
			gaussianBlur();
		}

		if (this.lightMapDrawingDisabled) {
			return;
		}
		this.frameBuffer.getColorBufferTexture().bind(0);

		// at last lights are rendered over scene
		if (this.rayHandler.shadows) {

			final Color c = this.rayHandler.ambientLight;
			ShaderProgram shader = this.shadowShader;
			if (RayHandler.isDiffuse) {
				shader = this.diffuseShader;
				shader.begin();
				Gdx.gl20.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_SRC_COLOR);
				shader.setUniformf("ambient", c.r, c.g, c.b, c.a);
			} else {
				shader.begin();
				Gdx.gl20.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
				shader.setUniformf("ambient", c.r * c.a, c.g * c.a, c.b * c.a,
						1f - c.a);
			}
			// shader.setUniformi("u_texture", 0);
			this.lightMapMesh.render(shader, GL20.GL_TRIANGLE_FAN);
			shader.end();
		} else if (needed) {

			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			this.withoutShadowShader.begin();
			// withoutShadowShader.setUniformi("u_texture", 0);
			this.lightMapMesh.render(this.withoutShadowShader,
					GL20.GL_TRIANGLE_FAN);
			this.withoutShadowShader.end();
		}

		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}

	public void gaussianBlur() {
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		for (int i = 0; i < this.rayHandler.blurNum; i++) {
			this.frameBuffer.getColorBufferTexture().bind(0);
			// horizontal
			this.pingPongBuffer.begin();
			{
				this.blurShader.begin();
				// blurShader.setUniformi("u_texture", 0);
				this.blurShader.setUniformf("dir", 1f, 0f);
				// TODO wird benötigt?
				this.blurShader.setUniformf("FBO_W", (float) this.fboWidth);
				this.blurShader.setUniformf("FBO_H", (float) this.fboHeight);
				this.blurShader.setUniformi("isDiffuse",
						RayHandler.isDiffuse ? 1 : 0);
				this.blurShader.setUniformi("centerLight",
						Light.CENTERLIGHT ? 1 : 0);
				this.lightMapMesh.render(this.blurShader, GL20.GL_TRIANGLE_FAN,
						0, 4);
				this.blurShader.end();
			}
			this.pingPongBuffer.end();

			this.pingPongBuffer.getColorBufferTexture().bind(0);
			// vertical
			this.frameBuffer.begin();
			{
				this.blurShader.begin();
				// blurShader.setUniformi("u_texture", 0);
				this.blurShader.setUniformf("dir", 0f, 1f);
				this.blurShader.setUniformf("FBO_W", (float) this.fboWidth);
				this.blurShader.setUniformf("FBO_H", (float) this.fboHeight);
				this.blurShader.setUniformi("isDiffuse",
						RayHandler.isDiffuse ? 1 : 0);
				this.blurShader.setUniformi("centerLight",
						Light.CENTERLIGHT ? 1 : 0);
				this.lightMapMesh.render(this.blurShader, GL20.GL_TRIANGLE_FAN,
						0, 4);
				this.blurShader.end();

			}
			this.frameBuffer.end();
		}

		Gdx.gl20.glEnable(GL20.GL_BLEND);
	}

	public LightMap(RayHandler rayHandler, int fboWidth, int fboHeight) {
		this.rayHandler = rayHandler;
		this.fboWidth = fboWidth;
		this.fboHeight = fboHeight;

		if (fboWidth <= 0) {
			fboWidth = 1;
		}
		if (fboHeight <= 0) {
			fboHeight = 1;
		}
		this.frameBuffer = new FrameBuffer(Format.RGBA8888, fboWidth,
				fboHeight, false);
		this.pingPongBuffer = new FrameBuffer(Format.RGBA8888, fboWidth,
				fboHeight, false);

		this.lightMapMesh = createLightMapMesh();

		this.shadowShader = Shader.createShader("shadow");
		this.diffuseShader = Shader.createShader("diffuse");

		this.withoutShadowShader = Shader.createShader("withoutshadow");

		this.blurShader = Shader.createShader("gaussian");
	}

	void dispose() {
		this.shadowShader.dispose();
		this.blurShader.dispose();
		this.lightMapMesh.dispose();
		this.frameBuffer.dispose();
		this.pingPongBuffer.dispose();
	}

	private Mesh createLightMapMesh() {
		float[] verts = new float[16]; // VERT_SIZE
		// vertex coord
		verts[0] = -1; // X1
		verts[1] = -1; // Y1

		verts[4] = 1; // X2
		verts[5] = -1; // Y2

		verts[8] = 1; // X3
		verts[9] = 1; // Y3

		verts[12] = -1; // X4
		verts[13] = 1; // Y4

		// tex coords
		verts[2] = 0f; // U1
		verts[3] = 0f; // V1

		verts[6] = 1f; // U2
		verts[7] = 0f; // V2

		verts[10] = 1f; // U3
		verts[11] = 1f; // V3

		verts[14] = 0f; // U4
		verts[15] = 1f; // V4

		Mesh tmpMesh = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position,
				2, "a_position"), new VertexAttribute(Usage.TextureCoordinates,
				2, "a_texCoord"));

		tmpMesh.setVertices(verts);
		return tmpMesh;
	}
}
