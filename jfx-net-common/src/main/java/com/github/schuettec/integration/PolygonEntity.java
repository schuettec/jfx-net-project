package com.github.schuettec.integration;

import java.util.List;

import com.github.schuettec.math.Point;
import com.github.schuettec.world.AbstractEntity;
import com.github.schuettec.world.AbstractPolygonObstacle;
import com.github.schuettec.world.EntityPoint;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PolygonEntity extends Polygon implements JFxEntity {

	protected AbstractPolygonObstacle entity;

	public PolygonEntity(Point worldCoordinates, EntityPoint... entityPoints) {
		this(new AbstractPolygonObstacle(worldCoordinates, entityPoints));
	}

	public PolygonEntity(AbstractPolygonObstacle entity) {
		super();
		setFXProperties();
		this.entity = entity;

	}

	private void setFXProperties() {
		setFill(null);
		setStroke(Color.BLACK);
	}

	@Override
	public void synchronize() {
		com.github.schuettec.math.Polygon collisionShape = entity.getCollisionShape();
		// Synchronize points
		getPoints().clear();
		setTranslateX(0);
		setTranslateY(0);
		// Do not use the world view here: Otherwise the polygon is always
		// interpreted to have a size measured at 0|0.
		List<EntityPoint> entityPoints = collisionShape.getEntityPoints();
		for (EntityPoint e : entityPoints) {
			getPoints().addAll(e.getCoordinates().getX(), e.getCoordinates().getY());
		}
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

	public com.github.schuettec.math.Polygon getCollisionShape() {
		return entity.getCollisionShape();
	}

}
