package com.jemge.box2d.box2dLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.jemge.j2d.IEntity;

public abstract class PositionalLight extends Light {
	private Body body;
	private IEntity entity;
	private float bodyOffsetX;
	private float bodyOffsetY;
	protected final float[] SIN;
	protected final float[] COS;

	private final Vector2 START = new Vector2();
	protected final float[] ENDX;
	protected final float[] ENDY;

	/**
	 * attach positional light to automatically follow body. Position is fixed
	 * to given offset.
	 */
	@Override
	public void attachToBody(Body body, float offsetX, float offsetY) {
		this.body = body;
		this.bodyOffsetX = offsetX;
		this.bodyOffsetY = offsetY;
		if (this.staticLight) {
			staticUpdate();
		}
	}

	@Override
	public void attachToEntity(IEntity entity, float offsetX, float offsetY) {
		this.entity = entity;
		this.bodyOffsetX = offsetX;
		this.bodyOffsetY = offsetY;
		if (this.staticLight) {
			staticUpdate();
		}
	}

	@Override
	public Vector2 getPosition() {
		this.TMPPOSITION.x = this.START.x;
		this.TMPPOSITION.y = this.START.y;
		return this.TMPPOSITION;
	}

	public Body getBody() {
		return this.body;
	}

	/**
	 * horizontal starting position of light in world coordinates.
	 */
	@Override
	public float getX() {
		return this.START.x;
	}

	/**
	 * vertical starting position of light in world coordinates.
	 */
	@Override
	public float getY() {
		return this.START.y;
	}

	@Override
	public void setPosition(float x, float y) {
		this.START.x = x;
		this.START.y = y;
		if (this.staticLight) {
			staticUpdate();
		}
	}

	@Override
	public void setPosition(Vector2 position) {
		this.START.x = position.x;
		this.START.y = position.y;
		if (this.staticLight) {
			staticUpdate();
		}
	}

	@Override
	public void update() {
		final Vector2 TMPEND = new Vector2();
		if (this.body != null && !this.staticLight) {
			final Vector2 VEC = this.body.getPosition();
			final float ANGLE = this.body.getAngle();
			final float COS = MathUtils.cos(ANGLE);
			final float SIN = MathUtils.sin(ANGLE);
			final float DX = this.bodyOffsetX * COS - this.bodyOffsetY * SIN;
			final float DY = this.bodyOffsetX * SIN + this.bodyOffsetY * COS;
			this.START.x = VEC.x + DX;
			this.START.y = VEC.y + DY;
			setDirection(ANGLE * MathUtils.radiansToDegrees);
		} else if (this.entity != null && !this.staticLight) {
			final Vector2 VEC = new Vector2(this.entity.getX(),
					this.entity.getY());
			final float ANGLE = this.entity.getRotation();
			final float COS = MathUtils.cos(ANGLE);
			final float SIN = MathUtils.sin(ANGLE);
			final float DX = this.bodyOffsetX * COS - this.bodyOffsetY * SIN;
			final float DY = this.bodyOffsetX * SIN + this.bodyOffsetY * COS;
			this.START.x = VEC.x + DX;
			this.START.y = VEC.y + DY;
			setDirection(ANGLE * MathUtils.radiansToDegrees);
		}

		if (this.rayHandler.culling) {
			this.culled = ((!this.rayHandler.intersect(this.START.x,
					this.START.y, this.distance + this.softShadowLenght)));
			if (this.culled) {
				return;
			}
		}

		if (this.staticLight) {
			return;
		}

		for (int i = 0; i < this.rayNum; i++) {
			this.m_index = i;
			this.f[i] = 1f;
			TMPEND.x = this.ENDX[i] + this.START.x;
			this.mx[i] = TMPEND.x;
			TMPEND.y = this.ENDY[i] + this.START.y;
			this.my[i] = TMPEND.y;
			if (this.rayHandler.world != null && !this.xray) {
				this.rayHandler.world.rayCast(this.RAY, this.START, TMPEND);
			}
		}
		setMesh();
	}

	// TODO private?
	private void setMesh() {
		// ray starting point
		int size = 0;

		this.segments[size++] = this.START.x;
		this.segments[size++] = this.START.y;
		this.segments[size++] = this.colorF;
		this.segments[size++] = 1;
		// rays ending points.
		for (int i = 0; i < this.rayNum; i++) {
			this.segments[size++] = this.mx[i];
			this.segments[size++] = this.my[i];
			this.segments[size++] = this.colorF;
			this.segments[size++] = 1 - this.f[i];
		}
		this.lightMesh.setVertices(this.segments, 0, size);

		if (!this.soft || this.xray) {
			return;
		}

		size = 0;
		// rays ending points.

		for (int i = 0; i < this.rayNum; i++) {
			this.segments[size++] = this.mx[i];
			this.segments[size++] = this.my[i];
			this.segments[size++] = this.colorF;
			final float s = (1 - this.f[i]);
			this.segments[size++] = s;
			this.segments[size++] = this.mx[i] + s * this.softShadowLenght
					* this.COS[i];
			this.segments[size++] = this.my[i] + s * this.softShadowLenght
					* this.SIN[i];
			this.segments[size++] = ZERO;
			this.segments[size++] = 0f;
		}
		this.softShadowMesh.setVertices(this.segments, 0, size);
	}

	@Override
	public void render() {
		if (this.rayHandler.culling && this.culled) {
			return;
		}

		this.rayHandler.lightRenderedLastFrame++;

		this.lightMesh.render(this.rayHandler.lightShader,
				GL20.GL_TRIANGLE_FAN, 0, this.vertexNum);
		if (this.soft && !this.xray) {
			this.softShadowMesh.render(this.rayHandler.lightShader,
					GL20.GL_TRIANGLE_STRIP, 0, (this.vertexNum - 1) * 2);
		}
	}

	public PositionalLight(int rays, Color color, float distance, float x,
			float y, float directionDegree) {
		super(rays, color, directionDegree, distance);
		this.START.x = x;
		this.START.y = y;
		this.SIN = new float[rays];
		this.COS = new float[rays];
		this.ENDX = new float[rays];
		this.ENDY = new float[rays];

		this.lightMesh = new Mesh(VertexDataType.VertexArray, false,
				this.vertexNum, 0, new VertexAttribute(Usage.Position, 2,
						"vertex_positions"), new VertexAttribute(
						Usage.ColorPacked, 4, "quad_colors"),
				new VertexAttribute(Usage.Generic, 1, "s"));
		this.softShadowMesh = new Mesh(VertexDataType.VertexArray, false,
				this.vertexNum * 2, 0, new VertexAttribute(Usage.Position, 2,
						"vertex_positions"), new VertexAttribute(
						Usage.ColorPacked, 4, "quad_colors"),
				new VertexAttribute(Usage.Generic, 1, "s"));

		setMesh();
	}

	@Override
	public boolean contains(float x, float y) {
		// fast fail
		final float X_D = this.START.x - x;
		final float Y_D = this.START.y - y;
		final float DST2 = X_D * X_D + Y_D * Y_D;
		if (this.distance * this.distance <= DST2) {
			return false;
		}

		// actual check

		boolean oddNodes = false;
		float x2 = this.mx[this.rayNum] = this.START.x;
		float y2 = this.my[this.rayNum] = this.START.y;
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
		return oddNodes;
	}
}