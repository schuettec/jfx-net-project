package com.github.schuettec.jfx;

import com.github.schuettec.math.Point;
import com.github.schuettec.world.AbstractCircleObstacle;
import com.github.schuettec.world.AbstractEntity;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class JFxCircleEntity extends Circle implements JFxEntity {

	protected AbstractCircleObstacle entity;

	public JFxCircleEntity(Point worldCoordinates, double radius) {
		this(new AbstractCircleObstacle(worldCoordinates, radius));
	}

	public JFxCircleEntity(AbstractCircleObstacle entity) {
		super();
		setFXProperties();
		this.entity = entity;

	}

	public AbstractCircleObstacle getEntity() {
		return entity;
	}

	private void setFXProperties() {
		setFill(null);
		setStroke(Color.BLACK);
	}

	@Override
	public void synchronize() {
		com.github.schuettec.math.Circle collisionShape = entity.getCollisionShape();
		setRadius(collisionShape.getRadius());
		setTranslateX(collisionShape.getPosition().x);
		setTranslateY(collisionShape.getPosition().y);
	}

	@Override
	public Point getPosition() {
		return entity.getPosition();
	}

	public void setPosition(int x, int y) {
		entity.setPosition(x, y);
	}

	public void setPosition(double x, double y) {
		entity.setPosition(x, y);
	}

	@Override
	public void setPosition(Point worldCoordinates) {
		entity.setPosition(worldCoordinates);
	}

	@Override
	public void setDegrees(double degrees) {
		entity.setDegrees(degrees);
	}

	@Override
	public void setScale(double scale) {
		entity.setScale(scale);
	}

	@Override
	public double getScale() {
		return entity.getScale();
	}

	@Override
	public double getDegrees() {
		return entity.getDegrees();
	}

	public AbstractEntity rotate(double degrees) {
		return entity.rotate(degrees);
	}

	public AbstractEntity translate(Point translation) {
		return entity.translate(translation);
	}

	public AbstractEntity scale(double scaleFactor) {
		return entity.scale(scaleFactor);
	}

	@Override
	public com.github.schuettec.math.Circle getCollisionShape() {
		return entity.getCollisionShape();
	}

}
