package com.jemge.j2d.shapes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.jemge.core.Jemge;
import com.jemge.core.system.IUpdateListener;
import com.jemge.j2d.IEntity;
import com.jemge.j2d.IShape;

public class CircleShape implements IShape, IEntity {
	public float radius;
	public Rectangle rectangle;

	public IUpdateListener<CircleShape> listener;

	public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Filled;

	public CircleShape(float x, float y, float radius) {
		this.rectangle = new Rectangle(x, y, radius, radius);
		this.radius = radius;
	}

	public CircleShape(Vector2 position, float radius) {
		this.rectangle = new Rectangle(position.x, position.y, radius, radius);
		this.radius = radius;
	}

	public CircleShape(CircleShape circleShape) {
		this.rectangle = new Rectangle(circleShape.rectangle.x,
				circleShape.rectangle.y, circleShape.radius, circleShape.radius);
		this.radius = circleShape.radius;
	}

	@Override
	public float getX() {
		return this.rectangle.x;
	}

	@Override
	public float getY() {
		return this.rectangle.y;
	}

	@Override
	public float getRotation() {
		return 0;
	}

	@Override
	public float getHeight() {
		return this.radius;
	}

	@Override
	public float getWidth() {
		return this.radius;
	}

	@Override
	public boolean needRender() {
		// Inside the camera view?
		return Jemge.renderer2D.CAMERAVIEW.overlaps(this.rectangle);
	}

	@Override
	public boolean getData(String name) {
		return false;
	}

	@Override
	public Rectangle getRectangle() {
		return this.rectangle;
	}

	@Override
	public void renderShape(ShapeRenderer renderer) {
		if (this.listener != null) {
			this.listener.update(this);
		}

		renderer.begin(this.shapeType);
		renderer.circle(this.rectangle.x, this.rectangle.y, this.radius);
		renderer.end();
	}
}