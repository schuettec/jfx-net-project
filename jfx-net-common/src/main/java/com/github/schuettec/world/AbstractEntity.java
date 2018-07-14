package com.github.schuettec.world;

import com.github.schuettec.math.Point;
import com.github.schuettec.math.Shape;
import com.github.schuettec.world.skills.Entity;

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
 * @author schuettec
 */
public abstract class AbstractEntity implements Entity, Shape {
	/**
	 * Holds the position of this entity in world coordinates.
	 */
	protected final Point worldCoordinates;

	/**
	 * Holds the current rotation of the entity in the world.
	 */
	protected double degrees;

	/**
	 * Holds the scaling factor of this entity.
	 */
	protected double scaling;

	public AbstractEntity() {
		this(new Point(0, 0));
	}

	public AbstractEntity(Point worldCoordinates) {
		this.worldCoordinates = worldCoordinates;
	}

	@Override
	public Point getPosition() {
		return worldCoordinates;
	}

	public void setPosition(int x, int y) {
		this.worldCoordinates.setLocation(x, y);
	}

	public void setPosition(double x, double y) {
		this.worldCoordinates.setLocation(x, y);
	}

	@Override
	public void setPosition(Point worldCoordinates) {
		this.worldCoordinates.setLocation(worldCoordinates);
	}

	@Override
	public void setDegrees(double degrees) {
		this.degrees = degrees;
	}

	@Override
	public void setScale(double scale) {
		this.scaling = scale;
	}

	@Override
	public double getScale() {
		return this.scaling;
	}

	@Override
	public double getDegrees() {
		return degrees;
	}

	@Override
	public AbstractEntity rotate(double degrees) {
		this.degrees += degrees;
		return this;
	}

	@Override
	public AbstractEntity translate(Point translation) {
		this.worldCoordinates.translate(translation);
		return this;
	}

	@Override
	public AbstractEntity scale(double scaleFactor) {
		this.scaling += scaleFactor;
		return this;
	}

}
