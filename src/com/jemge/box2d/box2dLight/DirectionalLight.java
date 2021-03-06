package com.jemge.box2d.box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.jemge.j2d.IEntity;

public class DirectionalLight extends Light {
	private float sin;
	private float cos;
	private final Vector2[] start;
	private final Vector2[] end;

	/**
	 * Directional lights simulate light source that locations is at infinite
	 * distance. Direction and intensity is same everywhere. -90 direction is
	 * straight from up.
	 * 
	 * @param rays
	 * @param color
	 * @param directionDegree
	 */
	public DirectionalLight(int rays, Color color, float directionDegree) {
		super(rays, color, directionDegree, Float.POSITIVE_INFINITY);

		this.vertexNum = (this.vertexNum - 1) * 2;

		this.start = new Vector2[this.rayNum];
		this.end = new Vector2[this.rayNum];
		for (int i = 0; i < this.rayNum; i++) {
			this.start[i] = new Vector2();
			this.end[i] = new Vector2();
		}
		setDirection(this.direction);

		this.lightMesh = new Mesh(VertexDataType.VertexArray, this.staticLight,
				this.vertexNum, 0, new VertexAttribute(Usage.Position, 2,
						"vertex_positions"), new VertexAttribute(
						Usage.ColorPacked, 4, "quad_colors"),
				new VertexAttribute(Usage.Generic, 1, "s"));
		this.softShadowMesh = new Mesh(VertexDataType.VertexArray,
				this.staticLight, this.vertexNum, 0, new VertexAttribute(
						Usage.Position, 2, "vertex_positions"),
				new VertexAttribute(Usage.ColorPacked, 4, "quad_colors"),
				new VertexAttribute(Usage.Generic, 1, "s"));
		update();
	}

	@Override
	public void setDirection(float direction) {
		super.direction = direction;
		this.sin = MathUtils.sinDeg(direction);
		this.cos = MathUtils.cosDeg(direction);
		if (this.staticLight) {
			staticUpdate();
		}
	}

	// private float lastX;

	@Override
	public void update() {
		if (this.staticLight) {
			return;
		}

		final float WIDTH = (this.rayHandler.x2 - this.rayHandler.x1);
		final float HEIGHT = (this.rayHandler.y2 - this.rayHandler.y1);

		final float SIZEOFSCREEN = WIDTH > HEIGHT ? WIDTH : HEIGHT;

		float xAxelOffSet = SIZEOFSCREEN * this.cos;
		float yAxelOffSet = SIZEOFSCREEN * this.sin;

		// preventing length <0 assertion error on box2d.
		if ((xAxelOffSet * xAxelOffSet < 0.1f)
				&& (yAxelOffSet * yAxelOffSet < 0.1f)) {
			xAxelOffSet = 1;
			yAxelOffSet = 1;
		}

		final float WIDTHOFFSET = SIZEOFSCREEN * -this.sin;
		final float HEIGHTOFFSET = SIZEOFSCREEN * this.cos;

		float x = (this.rayHandler.x1 + this.rayHandler.x2) * 0.5f
				- WIDTHOFFSET;
		float y = (this.rayHandler.y1 + this.rayHandler.y2) * 0.5f
				- HEIGHTOFFSET;

		final float PORTIONX = 2f * WIDTHOFFSET / (this.rayNum - 1);
		x = (MathUtils.floor(x / (PORTIONX * 2))) * PORTIONX * 2;
		final float PORTIONY = 2f * HEIGHTOFFSET / (this.rayNum - 1);
		y = (MathUtils.ceil(y / (PORTIONY * 2))) * PORTIONY * 2;
		for (int i = 0; i < this.rayNum; i++) {

			final float STEPPEDX = i * PORTIONX + x;
			final float STEPPEDY = i * PORTIONY + y;
			this.m_index = i;
			this.start[i].x = STEPPEDX - xAxelOffSet;
			this.start[i].y = STEPPEDY - yAxelOffSet;

			this.mx[i] = this.end[i].x = STEPPEDX + xAxelOffSet;
			this.my[i] = this.end[i].y = STEPPEDY + yAxelOffSet;

			if (this.rayHandler.world != null && !this.xray) {
				this.rayHandler.world.rayCast(this.RAY, this.start[i],
						this.end[i]);
			}
		}

		// update light mesh
		// ray starting point
		int size = 0;
		final int ARRAYSIZE = this.rayNum;

		for (int i = 0; i < ARRAYSIZE; i++) {
			this.segments[size++] = this.start[i].x;
			this.segments[size++] = this.start[i].y;
			this.segments[size++] = this.colorF;
			this.segments[size++] = 1f;
			this.segments[size++] = this.mx[i];
			this.segments[size++] = this.my[i];
			this.segments[size++] = this.colorF;
			this.segments[size++] = 1f;
		}

		this.lightMesh.setVertices(this.segments, 0, size);

		if (!this.soft || this.xray) {
			return;
		}

		size = 0;
		for (int i = 0; i < ARRAYSIZE; i++) {
			this.segments[size++] = this.mx[i];
			this.segments[size++] = this.my[i];
			this.segments[size++] = this.colorF;
			this.segments[size++] = 1f;

			this.segments[size++] = this.mx[i] + this.softShadowLenght
					* this.cos;
			this.segments[size++] = this.my[i] + this.softShadowLenght
					* this.sin;
			this.segments[size++] = ZERO;
			this.segments[size++] = 1f;
		}
		this.softShadowMesh.setVertices(this.segments, 0, size);
	}

	@Override
	public void render() {
		this.rayHandler.lightRenderedLastFrame++;

		this.lightMesh.render(this.rayHandler.lightShader,
				GL20.GL_TRIANGLE_STRIP, 0, this.vertexNum);
		if (this.soft && !this.xray) {
			this.softShadowMesh.render(this.rayHandler.lightShader,
					GL20.GL_TRIANGLE_STRIP, 0, this.vertexNum);
		}
	}

	@Override
	public final void attachToBody(Body body, float offsetX, float offSetY) {
	}

	@Override
	public final void attachToEntity(IEntity entity, float offsetX,
			float offSetY) {
	}

	@Override
	public void setPosition(float x, float y) {
	}

	@Override
	public Body getBody() {
		return null;
	}

	@Override
	public float getX() {
		return 0;
	}

	@Override
	public float getY() {
		return 0;
	}

	@Override
	public void setPosition(Vector2 position) {
	}

	@Override
	public boolean contains(float x, float y) {
		boolean oddNodes = false;
		float x2 = this.mx[this.rayNum] = this.start[0].x;
		float y2 = this.my[this.rayNum] = this.start[0].y;
		float x1, y1;
		for (int i = 0; i <= this.rayNum; x2 = x1, y2 = y1, ++i) {
			x1 = this.mx[i];
			y1 = this.my[i];
			if (((y1 < y) && (y2 >= y)) || (y1 >= y) && (y2 < y)) {
				if ((y - y1) / (y2 - y1) * (x2 - x1) < (x - x1)) {
					oddNodes = !oddNodes;
				}
			}
		}
		for (int i = 0; i < this.rayNum; x2 = x1, y2 = y1, ++i) {
			x1 = this.start[i].x;
			y1 = this.start[i].y;
			if (((y1 < y) && (y2 >= y)) || (y1 >= y) && (y2 < y)) {
				if ((y - y1) / (y2 - y1) * (x2 - x1) < (x - x1)) {
					oddNodes = !oddNodes;
				}
			}
		}
		return oddNodes;
	}
}