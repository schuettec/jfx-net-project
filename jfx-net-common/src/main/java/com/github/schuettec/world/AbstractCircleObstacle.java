package com.github.schuettec.world;

import com.github.schuettec.math.Circle;
import com.github.schuettec.math.Point;
import com.github.schuettec.world.skills.Obstacle;

/**
 * Represents an abstract obstacle in the world that manages a circle as
 * collision hull.
 * 
 * @author schuettec
 */
public class AbstractCircleObstacle extends AbstractEntity implements Obstacle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Holds the collision shape of this entity.
	 */
	protected Circle circle;

	public AbstractCircleObstacle(Point worldCoordinates, double radius) {
		super(worldCoordinates);
		this.circle = new Circle(new Point(0, 0), radius);
	}

	@Override
	public Circle getCollisionShape() {
		return circle.clone().scale(scaling).rotate(degrees).translate(worldCoordinates);
	}

	public double getRadius() {
		return circle.getRadius();
	}

}
