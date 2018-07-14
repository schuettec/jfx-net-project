package com.github.schuettec.world;

import java.util.ArrayList;
import java.util.List;

import com.github.schuettec.math.Point;
import com.github.schuettec.math.Polygon;
import com.github.schuettec.world.skills.Entity;
import com.github.schuettec.world.skills.Obstacle;

/**
 * Represents an abstract obstacle in the world. This is the most possible
 * abstract class for obstacle entities implementing common functions.
 * 
 * <p>
 * This class assumes that the collision hull for an entity is always
 * represented by a polygon. Therefore a list of entity points and collision
 * lines is managed by this object. Shapes that do not need a polygon for
 * collision detection (like circles - they can do it more efficiently) can
 * either override affected methods or can implement the skill interfaces
 * {@link Obstacle} and {@link Entity} themselves.
 * <p>
 * 
 * 
 * 
 * @author schuettec
 */
public class AbstractPolygonObstacle extends AbstractEntity implements Obstacle {

	protected Polygon polygon;

	public AbstractPolygonObstacle(Point worldCoordinates, EntityPoint... entityPoints) {
		super(worldCoordinates);
		this.polygon = new Polygon(entityPoints);
	}

	public AbstractPolygonObstacle(Point worldCoordinates, Point... points) {
		super(worldCoordinates);

		List<EntityPoint> entityPoints = new ArrayList<EntityPoint>(points.length);
		for (Point p : points) {
			entityPoints.add(new EntityPoint(p));
		}

		this.polygon = new Polygon(entityPoints);
	}

	@Override
	public Polygon getCollisionShape() {
		return polygon.clone().scale(scaling).rotate(degrees).translate(worldCoordinates);
	}

}
