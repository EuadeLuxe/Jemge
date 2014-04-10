package com.jemge.box2d.box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class PointLight extends PositionalLight {
	/**
	 * @param rays
	 * @param color
	 * @param distance
	 * @param x
	 * @param y
	 */
	public PointLight(int rays, Color color, float distance, float x, float y) {
		super(rays, color, distance, x, y, 0f);
		setEndPoints();
		update();
	}

	/**
	 * @param rays
	 *            Note default values: Color:WHITE Distance:15 Position:origo
	 */
	public PointLight(int rays) {
		this(rays, Light.DEFAULTCOLOR, 15f, 0f, 0f);
	}

	private final void setEndPoints() {
		float angleNum = 360f / (this.rayNum - 1);
		for (int i = 0; i < this.rayNum; i++) {
			final float angle = angleNum * i;
			this.SIN[i] = MathUtils.sinDeg(angle);
			this.COS[i] = MathUtils.cosDeg(angle);
			this.ENDX[i] = this.distance * this.COS[i];
			this.ENDY[i] = this.distance * this.SIN[i];
		}
	}

	@Override
	public void setDirection(float directionDegree) {
	}

	/**
	 * setDistance(float dist) MIN capped to 1cm
	 * 
	 * @param dist
	 */
	public void setDistance(float dist) {
		dist *= RayHandler.gammaCorrectionParameter;
		this.distance = dist < 0.01f ? 0.01f : dist;
		setEndPoints();
		if (this.staticLight) {
			staticUpdate();
		}
	}
}