package com.jemge.box2d.box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.jemge.core.Jemge;
import com.jemge.j2d.IEntity;

/**
 * @author kalle
 */
public abstract class Light {

	public static final class Quality {
		public static final int POOR = 90;
		public static final int NORMAL = 180;
		public static final int NICE = 360;
		public static final int VERY_NICE = 720;
	}

	protected static final Color DEFAULTCOLOR = new Color(0.75f, 0.75f, 0.5f,
			0.75f);
	protected static final float ZERO = Color.toFloatBits(0f, 0f, 0f, 0f);
	public static boolean centerLight = false;
	private boolean active = true;
	protected boolean soft = true;
	protected boolean xray = false;
	protected boolean staticLight = false;
	protected float softShadowLenght = 2.5f;

	protected RayHandler rayHandler;
	protected boolean culled = false;
	protected int rayNum;
	protected int vertexNum;
	protected float distance;
	protected float direction;

	protected Color color = new Color();
	protected Mesh lightMesh;
	protected Mesh softShadowMesh;

	protected float colorF;

	private static final int MIN_RAYS = 3;

	protected float[] segments;
	protected float[] mx;
	protected float[] my;
	protected float[] f;
	protected int m_index = 0;

	public Light(int rays, Color color, float directionDegree, float distance) {
		Jemge.renderer2D.getRayHandler().LIGHTLIST.add(this);
		this.rayHandler = Jemge.renderer2D.getRayHandler();
		setRayNum(rays);
		this.direction = directionDegree;
		distance *= RayHandler.gammaCorrectionParameter;
		this.distance = distance < 0.01f ? 0.01f : distance;
		setColor(color);
	}

	/**
	 * setColor(Color newColor) { rgb set the color and alpha set intesity NOTE:
	 * you can also use colorless light with shadows(EG 0,0,0,1)
	 * 
	 * @param newColor
	 */
	public void setColor(Color newColor) {
		if (newColor != null) {
			this.color.set(newColor);
			this.colorF = this.color.toFloatBits();
		} else {
			this.color = DEFAULTCOLOR;
			this.colorF = DEFAULTCOLOR.toFloatBits();
		}
		if (this.staticLight) {
			staticUpdate();
		}
	}

	/**
	 * set Color(float r, float g, float b, float a) rgb set the color and alpha
	 * set intensity NOTE: you can also use colorless light with shadows(EG
	 * 0,0,0,1)
	 * 
	 * @param r
	 *            red
	 * @param g
	 *            green
	 * @param b
	 *            blue
	 * @param a
	 *            intensity
	 */
	public void setColor(float r, float g, float b, float a) {
		this.color.r = r;
		this.color.g = g;
		this.color.b = b;
		this.color.a = a;
		this.colorF = this.color.toFloatBits();
		if (this.staticLight) {
			staticUpdate();
		}
	}

	/**
	 * setDistance(float dist) MIN capped to 1cm
	 * 
	 * @param dist
	 */
	public void setDistance(float dist) {
	}

	public abstract void update();

	public abstract void render();

	public abstract void setDirection(float directionDegree);

	public void remove() {
		this.rayHandler.LIGHTLIST.removeValue(this, false);
		this.lightMesh.dispose();
		this.softShadowMesh.dispose();
	}

	/**
	 * attach positional light to automatically follow body. Position is fixed
	 * to given offset
	 * <p/>
	 * NOTE: does absolute nothing if directional light
	 */
	public abstract void attachToBody(Body body, float offsetX, float offsetY);

	/**
	 * attach positional light to automatically follow entity. Position is fixed
	 * to given offset
	 * <p/>
	 * NOTE: does absolute nothing if directional light
	 */
	public abstract void attachToEntity(IEntity body, float offsetX,
			float offsetY);

	/**
	 * @return attached body or null if not set.
	 *         <p/>
	 *         NOTE: directional light always return null
	 */
	public abstract Body getBody();

	/**
	 * set light starting position
	 * <p/>
	 * NOTE: does absolute nothing if directional light
	 */
	public abstract void setPosition(float x, float y);

	/**
	 * set light starting position
	 * <p/>
	 * NOTE: does absolute nothing if directional light
	 */
	public abstract void setPosition(Vector2 position);

	protected final Vector2 TMPPOSITION = new Vector2();

	/**
	 * starting position of light in world coordinates. directional light return
	 * zero vector.
	 * <p/>
	 * NOTE: changing this vector does nothing
	 * 
	 * @return posX
	 */
	public Vector2 getPosition() {
		return this.TMPPOSITION;
	}

	/**
	 * horizontal starting position of light in world coordinates. directional
	 * light return 0
	 */
	/**
	 * @return posX
	 */
	public abstract float getX();

	/**
	 * vertical starting position of light in world coordinates. directional
	 * light return 0
	 */
	/**
	 * @return posY
	 */
	public abstract float getY();

	// TODO protected or public?
	protected void staticUpdate() {
		boolean tmp = this.rayHandler.culling;
		this.staticLight = !this.staticLight;
		this.rayHandler.culling = false;
		update();
		this.rayHandler.culling = tmp;
		this.staticLight = !this.staticLight;
	}

	public final boolean isActive() {
		return this.active;
	}

	/**
	 * disable/enables this light updates and rendering.
	 * 
	 * @param active
	 */
	public final void setActive(boolean active) {
		if (active == this.active) {
			return;
		}

		if (active) {
			this.rayHandler.LIGHTLIST.add(this);
			this.rayHandler.DISABLEDLIGHTS.removeValue(this, true);
		} else {
			this.rayHandler.DISABLEDLIGHTS.add(this);
			this.rayHandler.LIGHTLIST.removeValue(this, true);
		}

		this.active = active;
	}

	/**
	 * do light beams go through obstacles
	 * 
	 * @return
	 */
	public final boolean isXray() {
		return this.xray;
	}

	/**
	 * disable/enables xray beams. enabling this will allow beams go through
	 * obstacles this reduce cpu burden of light about 70%. Use combination of
	 * xray and non xray lights wisely
	 * 
	 * @param xray
	 */
	public final void setXray(boolean xray) {
		this.xray = xray;
		if (this.staticLight) {
			staticUpdate();
		}
	}

	/**
	 * return is this light static. Static light do not get any automatic
	 * updates but setting any parameters will update it. Static lights are
	 * useful for lights that you want to collide with static geometry but
	 * ignore all the dynamic objects.
	 * 
	 * @return
	 */
	public final boolean isStaticLight() {
		return this.staticLight;
	}

	/**
	 * disables/enables staticness for light. Static light do not get any
	 * automatic updates but setting any parameters will update it. Static
	 * lights are useful for lights that you want to collide with static
	 * geometry but ignore all the dynamic objects. Reduce CPU burden of light
	 * about 90%.
	 * 
	 * @param staticLight
	 */
	public final void setStaticLight(boolean staticLight) {
		this.staticLight = staticLight;
		if (staticLight) {
			staticUpdate();
		}
	}

	/**
	 * is tips of light beams soft
	 * 
	 * @return
	 */
	public final boolean isSoft() {
		return this.soft;
	}

	/**
	 * disable/enables softness on tips of lights beams.
	 * 
	 * @param soft
	 */
	public final void setSoft(boolean soft) {
		this.soft = soft;
		if (this.staticLight) {
			staticUpdate();
		}
	}

	/**
	 * return how much is softness used in tip of the beams. default 2.5
	 * 
	 * @return
	 */
	public final float getSoftShadowLenght() {
		return this.softShadowLenght;
	}

	/**
	 * set how much is softness used in tip of the beams. default 2.5
	 * 
	 * @param softShadowLenght
	 */
	public final void setSoftnessLenght(float softShadowLenght) {
		this.softShadowLenght = softShadowLenght;
		if (this.staticLight) {
			staticUpdate();
		}
	}

	private final void setRayNum(int rays) {
		if (rays < MIN_RAYS) {
			rays = MIN_RAYS;
		}

		this.rayNum = rays;
		this.vertexNum = rays + 1;

		this.segments = new float[this.vertexNum * 8];
		this.mx = new float[this.vertexNum];
		this.my = new float[this.vertexNum];
		this.f = new float[this.vertexNum];
	}

	/**
	 * Color getColor
	 * 
	 * @return current lights color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * float getDistance()
	 * 
	 * @return light rays distance.
	 */
	public float getDistance() {
		return this.distance / RayHandler.gammaCorrectionParameter;
	}

	/**
	 * method for checking is given point inside of this light
	 */
	public boolean contains(float x, float y) {
		return false;
	}

	protected final RayCastCallback RAY = new RayCastCallback() {
		@Override
		public final float reportRayFixture(Fixture fixture, Vector2 point,
				Vector2 normal, float fraction) {
			if ((filterA != null) && !contactFilter(fixture)) {
				return -1;
			}
			// if (fixture.isSensor())
			// return -1;
			Light.this.mx[Light.this.m_index] = point.x;
			Light.this.my[Light.this.m_index] = point.y;
			Light.this.f[Light.this.m_index] = fraction;
			return fraction;
		}
	};

	private final boolean contactFilter(Fixture fixtureB) {
		Filter filterB = fixtureB.getFilterData();

		if (filterA.groupIndex == filterB.groupIndex && filterA.groupIndex != 0) {
			return filterA.groupIndex > 0;
		}

		return (filterA.maskBits & filterB.categoryBits) != 0
				&& (filterA.categoryBits & filterB.maskBits) != 0;
	}

	/**
	 * light filter *
	 */
	private static Filter filterA = null;

	/**
	 * set given contact filter for ALL LIGHTS
	 * 
	 * @param filter
	 */
	public static void setContactFilter(Filter filter) {
		filterA = filter;
	}

	/**
	 * create new contact filter for ALL LIGHTS with give parameters
	 * 
	 * @param categoryBits
	 * @param groupIndex
	 * @param maskBits
	 */
	public static void setContactFilter(short categoryBits, short groupIndex,
			short maskBits) {
		filterA = new Filter();
		filterA.categoryBits = categoryBits;
		filterA.groupIndex = groupIndex;
		filterA.maskBits = maskBits;
	}
}