package com.github.schuettec.world;

import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.math.Point;
import com.github.schuettec.world.skills.Entity;

/**
 * Instances of this class represent a collision of two {@link Obstacle}s. This
 * class is the data transfer object for collisions detected by the {@link Map}.
 * 
 * @author Chris
 *
 */
public class Collision {

	/**
	 * This is the entity this object uses for its hash code.
	 */
	protected Entity entity;
	/**
	 * This is the collision opponent.
	 */
	protected Entity opponent;
	/**
	 * This is the list of collision points.
	 */
	protected List<Point> points;

	protected Collision(Entity entity, Entity opponent, Point points) {
		this(entity, opponent, new LinkedList<>());
	}

	protected Collision(Entity entity, Entity opponent, List<Point> points) {
		super();
		this.entity = entity;
		this.opponent = opponent;
		this.points = points;
	}

	/**
	 * @return Returns the first collision as {@link Point} that was reported for
	 *         the colliding entities. If this object does not have any collision
	 *         points this call returns <code>null</code>.
	 */
	public Point getFirstCollision() {
		if (points.isEmpty()) {
			return null;
		} else {
			return points.get(0);
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public Entity getOpponent() {
		return opponent;
	}

	public List<Point> getPoints() {
		return points;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Collision other = (Collision) obj;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		return true;
	}

}
