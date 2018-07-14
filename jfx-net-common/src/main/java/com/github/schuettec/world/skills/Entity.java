package com.github.schuettec.world.skills;

import com.github.schuettec.math.Point;

/**
 * This interface is implemented by objects that "live" in a world and can
 * either be rendered or have special skills in a game world.
 * 
 * @author schuettec
 *
 */
public interface Entity {
	/**
	 * Returns the position of this entity.
	 * <p>
	 * Most of the entities will return their center points to make positioning
	 * easy. If your entity does not have an unambiguous enter point return the
	 * upper left point of the hull rectangle to make positioning intuitive.
	 * </p>
	 * 
	 * @return Returns the position of this entity as {@link Point}.
	 * 
	 */
	public Point getPosition();

	/**
	 * Sets the position of this entity in world coordinates.
	 * 
	 * @param worldCoordinates
	 *            Word coordinates.
	 */
	public void setPosition(Point worldCoordinates);

	/**
	 * @param scale
	 *            Sets the scaling factor.
	 */
	public void setScale(double scale);

	/**
	 * @return Returns the scaling factor.
	 */
	public double getScale();

	/**
	 * This method set the degrees of the entity rotation.
	 * 
	 * @param degrees
	 *            The degrees of the entity rotation.
	 */
	public void setDegrees(double degrees);

	/**
	 * @return Returns the current rotation of the entity.
	 */
	public double getDegrees();

}