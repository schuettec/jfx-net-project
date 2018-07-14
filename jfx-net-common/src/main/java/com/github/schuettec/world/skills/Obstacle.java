package com.github.schuettec.world.skills;

import com.github.schuettec.math.Shape;

/**
 * Defines the minimum amount of methods an obstacle must support to detect a
 * collision.
 * 
 * @author schuettec
 *
 */
public interface Obstacle extends Entity {

	/**
	 * @return Returns the {@link Shape} representing the collision shape of
	 *         this Entity. Implementations must make sure that the
	 *         {@link Shape} is translated to world coordinates and reflects all
	 *         properties like scaling etc.
	 */
	public Shape getCollisionShape();

}
