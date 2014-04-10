package com.jemge.box2d.box2dLight;

/**
 * @author kalle_h
 *
 */

import com.jemge.box2d.shaders.Shader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class RayHandler implements Disposable {
	protected boolean culling = true;
	protected boolean shadows = true;
	protected boolean blur = true;

	protected int blurNum = 1;
	protected Color ambientLight = new Color();

	protected World world;
	protected ShaderProgram lightShader;

	/**
	 * gles1.0 shadows mesh
	 */
	private Mesh box;

	/**
	 * @param combined
	 *            matrix that include projection and translation matrices
	 */
	private final Matrix4 COMBINED = new Matrix4();

	/**
	 * camera matrix corners
	 */
	protected float x1, x2, y1, y2;

	private LightMap lightMap;

	/**
	 * This Array contain all the lights.
	 * <p/>
	 * NOTE: DO NOT MODIFY THIS LIST
	 */
	public final Array<Light> LIGHTLIST = new Array<Light>(false, 16);
	/**
	 * This Array contain all the disabled lights.
	 * <p/>
	 * NOTE: DO NOT MODIFY THIS LIST
	 */
	public final Array<Light> DISABLEDLIGHTS = new Array<Light>(false, 16);

	/**
	 * how many lights passed culling and rendered to scene
	 */
	public int lightRenderedLastFrame = 0;

	/**
	 * Construct handler that manages everything related to updating and
	 * rendering the lights MINIMUM parameters needed are world where collision
	 * geometry is taken.
	 * <p/>
	 * Default setting: culling = true, shadows = true, blur =
	 * true(GL2.0),blurNum = 1, ambientLight = 0.0f;
	 * <p/>
	 * NOTE1: rays number per lights are capped to 1023. For different size use
	 * other constructor
	 * <p/>
	 * NOTE2: On GL 2.0 FBO size is 1/4 * screen size and used by default. For
	 * different sizes use other constructor
	 * 
	 * @param world
	 */
	public RayHandler(World world) {
		this(world, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
	}

	/**
	 * Construct handler that manages everything related to updating and
	 * rendering the lights MINIMUM parameters needed are world where collision
	 * geometry is taken.
	 * <p/>
	 * Default setting: culling = true, shadows = true, blur =
	 * true(GL2.0),blurNum = 1, ambientLight = 0.0f;
	 * 
	 * @param world
	 * @param fboWidth
	 * @param fboHeight
	 */
	public RayHandler(World world, int fboWidth, int fboHeight) {
		this.world = world;

		this.lightMap = new LightMap(this, fboWidth, fboHeight);
		this.lightShader = Shader.createShader("light");
	}

	/**
	 * Set combined camera matrix. Matrix will be copied and used for rendering
	 * lights, culling. Matrix must be set to work in box2d coordinates. Matrix
	 * has to be updated every frame(if camera is changed)
	 * <p/>
	 * <p/>
	 * NOTE: Matrix4 is assumed to be orthogonal for culling and directional
	 * lights.
	 * <p/>
	 * If any problems detected Use: [public void setCombinedMatrix(Matrix4
	 * combined, float x, float y, float viewPortWidth, float viewPortHeight)]
	 * Instead
	 * 
	 * @param combined
	 *            matrix that include projection and translation matrices
	 */
	public void setCombinedMatrix(Matrix4 combined) {
		System.arraycopy(combined.val, 0, this.COMBINED.val, 0, 16);

		// updateCameraCorners
		final float INVWIDTH = combined.val[Matrix4.M00];

		final float HALFVIEWPORTWIDTH = 1f / INVWIDTH;
		final float X = -HALFVIEWPORTWIDTH * combined.val[Matrix4.M03];
		this.x1 = X - HALFVIEWPORTWIDTH;
		this.x2 = X + HALFVIEWPORTWIDTH;

		final float INVHEIGHT = combined.val[Matrix4.M11];

		final float HALFVIEWPORTHEIGHT = 1f / INVHEIGHT;
		final float Y = -HALFVIEWPORTHEIGHT * combined.val[Matrix4.M13];
		this.y1 = Y - HALFVIEWPORTHEIGHT;
		this.y2 = Y + HALFVIEWPORTHEIGHT;
	}

	/**
	 * EXPERT USE Set combined camera matrix. Matrix will be copied and used for
	 * rendering lights, culling. Matrix must be set to work in box2d
	 * coordinates. Matrix has to be updated every frame(if camera is changed)
	 * <p/>
	 * NOTE: this work with rotated cameras.
	 * 
	 * @param combined
	 *            matrix that include projection and translation matrices
	 * @param x
	 *            combined matrix position
	 * @param y
	 *            combined matrix position
	 * @param viewPortWidth
	 *            NOTE!! use actual size, remember to multiple with zoom value
	 *            if pulled from OrthoCamera
	 * @param viewPortHeight
	 *            NOTE!! use actual size, remember to multiple with zoom value
	 *            if pulled from OrthoCamera
	 */
	public void setCombinedMatrix(Matrix4 combined, float x, float y,
			float viewPortWidth, float viewPortHeight) {
		System.arraycopy(combined.val, 0, this.COMBINED.val, 0, 16);
		// updateCameraCorners
		final float HALFVIEWPORTWIDTH = viewPortWidth * 0.5f;
		this.x1 = x - HALFVIEWPORTWIDTH;
		this.x2 = x + HALFVIEWPORTWIDTH;

		final float HALFVIEWPORTHEIGHT = viewPortHeight * 0.5f;
		this.y1 = y - HALFVIEWPORTHEIGHT;
		this.y2 = y + HALFVIEWPORTHEIGHT;
	}

	// TODO protected?
	protected boolean intersect(float x, float y, float side) {
		return (this.x1 < (x + side) && this.x2 > (x - side)
				&& this.y1 < (y + side) && this.y2 > (y - side));
	}

	/**
	 * Remember setCombinedMatrix(Matrix4 combined) before drawing.
	 * <p/>
	 * Don't call this inside of any begin/end statements. Call this method
	 * after you have rendered background but before UI. Box2d bodies can be
	 * rendered before or after depending how you want x-ray light interact with
	 * bodies
	 */
	public final void updateAndRender() {
		update();
		render();
	}

	/**
	 * Manual update method for all lights. Use this if you have less physic
	 * steps than rendering steps.
	 */
	public final void update() {
		final int SIZE = this.LIGHTLIST.size;
		for (int j = 0; j < SIZE; j++) {
			this.LIGHTLIST.get(j).update();
		}
	}

	/**
	 * Manual rendering method for all lights.
	 * <p/>
	 * NOTE! Remember to call updateRays if you use this method. * Remember
	 * setCombinedMatrix(Matrix4 combined) before drawing.
	 * <p/>
	 * <p/>
	 * Don't call this inside of any begin/end statements. Call this method
	 * after you have rendered background but before UI. Box2d bodies can be
	 * rendered before or after depending how you want x-ray light interact with
	 * bodies
	 */
	public void render() {
		this.lightRenderedLastFrame = 0;

		Gdx.gl.glDepthMask(false);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		renderWithShaders();
	}

	// TODO private?
	private void renderWithShaders() {
		if (this.shadows || this.blur) {
			this.lightMap.frameBuffer.begin();
			Gdx.gl20.glClearColor(0f, 0f, 0f, 0f);
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		this.lightShader.begin();
		{
			this.lightShader.setUniformMatrix("u_projTrans", this.COMBINED);
			this.lightShader.setUniformi("gamma", getGammaCorrection() ? 1 : 0);
			for (int i = 0, size = this.LIGHTLIST.size; i < size; i++) {
				this.LIGHTLIST.get(i).render();
			}
		}
		this.lightShader.end();

		if (this.shadows || this.blur) {
			this.lightMap.frameBuffer.end();
			this.lightMap.render();
		}
	}

	/**
	 * Checks whether the given point is inside of any light volume.
	 * 
	 * @param x
	 * @param y
	 * @return true if point intersect any light volume
	 */
	public boolean pointAtLight(float x, float y) {
		for (int i = 0, size = this.LIGHTLIST.size; i < size; i++) {
			if (this.LIGHTLIST.get(i).contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the given point outside of all light volumes.
	 * 
	 * @param x
	 * @param y
	 * @return true if point intersect any light volume
	 */
	public boolean pointAtShadow(float x, float y) {
		for (int i = 0, size = this.LIGHTLIST.size; i < size; i++) {
			if (this.LIGHTLIST.get(i).contains(x, y)) {
				return false;
			}
		}
		return true;
	}

	private void alphaChannelClear() {
		Gdx.gl20.glClearColor(0f, 0f, 0f, this.ambientLight.a);
		Gdx.gl20.glColorMask(false, false, false, true);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glColorMask(true, true, true, true);
		Gdx.gl20.glClearColor(0f, 0f, 0f, 0f);
	}

	public void dispose() {
		for (int i = 0; i < this.LIGHTLIST.size; i++) {
			this.LIGHTLIST.get(i).lightMesh.dispose();
			this.LIGHTLIST.get(i).softShadowMesh.dispose();
		}
		this.LIGHTLIST.clear();

		for (int i = 0; i < this.DISABLEDLIGHTS.size; i++) {
			this.DISABLEDLIGHTS.get(i).lightMesh.dispose();
			this.DISABLEDLIGHTS.get(i).softShadowMesh.dispose();
		}
		this.DISABLEDLIGHTS.clear();

		if (this.lightMap != null) {
			this.lightMap.dispose();
		}
		if (this.lightShader != null) {
			this.lightShader.dispose();
		}
	}

	public void removeAll() {
		while (this.LIGHTLIST.size > 0) {
			this.LIGHTLIST.pop().remove();
		}

		while (this.DISABLEDLIGHTS.size > 0) {
			this.DISABLEDLIGHTS.pop().remove();
		}
	}

	private void setShadowBox() {
		int i = 0;
		// This need some work, maybe camera matrix would needed
		float c = Color.toFloatBits(0, 0, 0, 1);
		float[] m_segments = new float[12];
		m_segments[i++] = -1000000f;
		m_segments[i++] = -1000000f;
		m_segments[i++] = c;
		m_segments[i++] = -1000000f;
		m_segments[i++] = 1000000f;
		m_segments[i++] = c;
		m_segments[i++] = 1000000f;
		m_segments[i++] = 1000000f;
		m_segments[i++] = c;
		m_segments[i++] = 1000000f;
		m_segments[i++] = -1000000;
		m_segments[i++] = c;
		this.box.setVertices(m_segments, 0, i);
	}

	/**
	 * Disables/enables culling. This save cpu and gpu time when world is bigger
	 * than screen.
	 * <p/>
	 * Default = true
	 * 
	 * @param culling
	 *            the culling to set
	 */
	public final void setCulling(boolean culling) {
		this.culling = culling;
	}

	/**
	 * Disables/enables gaussian blur. This make lights much more softer and
	 * realistic look but also cost some precious shader time. With default fbo
	 * size on android cost around 1ms
	 * <p/>
	 * default = true;
	 * 
	 * @param blur
	 *            the blur to set
	 */
	public final void setBlur(boolean blur) {
		this.blur = blur;
	}

	/**
	 * Set number of gaussian blur passes. Blurring can be pretty heavy weight
	 * operation, 1-3 should be safe. Setting this to 0 is same as
	 * setBlur(false)
	 * <p/>
	 * default = 1
	 * 
	 * @param blurNum
	 *            the blurNum to set
	 */
	public final void setBlurNum(int blurNum) {
		this.blurNum = blurNum;
	}

	/**
	 * Disables/enables shadows. NOTE: If gl1.1 android you need to change
	 * render target to contain alpha channel* default = true
	 * 
	 * @param shadows
	 *            the shadows to set
	 */
	public final void setShadows(boolean shadows) {
		this.shadows = shadows;
	}

	/**
	 * Ambient light is how dark are the shadows. clamped to 0-1
	 * <p/>
	 * default = 0;
	 * 
	 * @param ambientLight
	 *            the ambientLight to set
	 */
	public final void setAmbientLight(float ambientLight) {
		if (ambientLight < 0) {
			ambientLight = 0;
		}
		if (ambientLight > 1) {
			ambientLight = 1;
		}
		this.ambientLight.a = ambientLight;
	}

	/**
	 * Ambient light color is how dark and what colored the shadows are. clamped
	 * to 0-1 NOTE: color is changed only in gles2.0 default = 0;
	 * 
	 * @param r
	 *            , g, b, a the ambientLight to set
	 */
	public final void setAmbientLight(float r, float g, float b, float a) {
		this.ambientLight.r = r;
		this.ambientLight.g = g;
		this.ambientLight.b = b;
		this.ambientLight.a = a;
	}

	/**
	 * Ambient light color is how dark and what colored the shadows are. clamped
	 * to 0-1 NOTE: color is changed only in gles2.0 default = 0,0,0,0;
	 * 
	 * @param ambientLightColor
	 *            the ambientLight to set
	 */
	public final void setAmbientLight(Color ambientLightColor) {
		this.ambientLight.set(ambientLightColor);
	}

	/**
	 * @param world
	 *            the world to set
	 */
	public final void setWorld(World world) {
		this.world = world;
	}

	private static boolean gammaCorrection = false;
	protected static float gammaCorrectionParameter = 1f;
	public static boolean isDiffuse = false;
	private static final float GAMMA_COR = 0.625f;

	/**
	 * return is gamma correction enabled
	 * 
	 * @return
	 */
	public static boolean getGammaCorrection() {
		return gammaCorrection;
	}

	/**
	 * set gammaCorrection. This need to be done before creating instance of
	 * rayHandler. NOTE: this do nothing on gles1.0. NOTE2: for match the
	 * visuals with gamma uncorrected lights light distance parameters is
	 * modified internal.
	 * 
	 * @param gammeCorrectionWanted
	 */
	public static void setGammaCorrection(boolean gammeCorrectionWanted) {
		gammaCorrection = gammeCorrectionWanted;
		if (gammaCorrection) {
			gammaCorrectionParameter = GAMMA_COR;
		} else {
			gammaCorrectionParameter = 1f;
		}
	}

	/**
	 * If this is set to true and shadow are on lights are blended with diffuse
	 * algorithm. this preserve colors but might look bit darker. This is more
	 * realistic model than normally used This might improve performance
	 * slightly
	 * 
	 * @param useDiffuse
	 */
	public static void useDiffuseLight(boolean useDiffuse) {
		isDiffuse = useDiffuse;
	}

	/**
	 * enable/disable lightMap automatic rendering. Default is true If set to
	 * false user need use getLightMapTexture() and render that or use it as a
	 * light map when rendering. Example shader for spriteBatch is given. This
	 * is faster way to do if there is not that much overdrawing or if just
	 * couple object need light/shadows.
	 * 
	 * @param isAutomatic
	 */
	public void setLightMapRendering(boolean isAutomatic) {
		this.lightMap.lightMapDrawingDisabled = !isAutomatic;
	}

	/**
	 * Expert functionality
	 * 
	 * @return Texture that contain lightmap texture that can be used as light
	 *         texture in your shaders
	 */
	public Texture getLightMapTexture() {
		return this.lightMap.frameBuffer.getColorBufferTexture();
	}

	/**
	 * Expert functionality, no support given
	 * 
	 * @return FrameBuffer that contains lightMap
	 */
	public FrameBuffer getLightMapBuffer() {
		return this.lightMap.frameBuffer;
	}
}