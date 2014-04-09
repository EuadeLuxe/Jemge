package com.jemge.box2d.box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * Light is data container for all the light parameters You can create instance
 * of Light also with help of rayHandler addLight method
 */
public class ConeLight extends PositionalLight {

	float coneDegree;

	/**
	 * @param rays
	 * @param directionDegree
	 * @param distance
	 * @param color
	 * @param x
	 * @param y
	 * @param coneDegree
	 */
	public ConeLight(int rays, Color color, float distance, float x, float y,
			float directionDegree, float coneDegree) {

		super(rays, color, distance, x, y, directionDegree);
		setConeDegree(coneDegree);
		setDirection(this.direction);
		update();
	}

	public void setDirection(float direction) {

		this.direction = direction;
		for (int i = 0; i < this.rayNum; i++) {
			float angle = direction + this.coneDegree - 2f * this.coneDegree
					* i / (this.rayNum - 1f);
			final float s = this.sin[i] = MathUtils.sinDeg(angle);
			final float c = this.cos[i] = MathUtils.cosDeg(angle);
			this.endX[i] = this.distance * c;
			this.endY[i] = this.distance * s;
		}
		if (this.staticLight) {
			staticUpdate();
		}
	}

	/**
	 * @return the coneDegree
	 */
	public final float getConeDegree() {
		return this.coneDegree;
	}

	/**
	 * How big is the arc of cone. Arc angle = coneDegree * 2
	 * 
	 * @param coneDegree
	 *            the coneDegree to set
	 */
	public final void setConeDegree(float coneDegree) {
		if (coneDegree < 0) {
			coneDegree = 0;
		}
		if (coneDegree > 180) {
			coneDegree = 180;
		}
		this.coneDegree = coneDegree;
		setDirection(this.direction);
	}

	/**
	 * setDistance(float dist) MIN capped to 1cm
	 * 
	 * @param dist
	 */
	public void setDistance(float dist) {
		dist *= RayHandler.gammaCorrectionParameter;
		this.distance = dist < 0.01f ? 0.01f : dist;
		setDirection(this.direction);
	}

}
