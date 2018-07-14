package com.github.schuettec.world;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.schuettec.math.Point;
import com.github.schuettec.math.Shape;
import com.github.schuettec.world.skills.Entity;

/**
 * This is the map data structure. This class makes all {@link Entity} objects
 * accessible that are currently existing in the world. The {@link Map} also
 * manages all the collisions for the current frame.
 * 
 * 
 * <p>
 * <b>Note: The map is intended to be used in the rendering thread or at least
 * in one thread managing all the entities calls. Therefore this {@link Map} is
 * not thread safe.</b>
 * </p>
 * 
 * @author Chris
 *
 */
public class Map {

	// this would be a useful regression test
	// public static void main(String[] args) {
	// Map map = new Map();
	// Entity e1 = new AbstractPolygonObstacle((Point) null, new EntityPoint[]
	// {});
	// Entity e2 = new AbstractPolygonObstacle((Point) null, new EntityPoint[]
	// {});
	// map.detectedCollision = new HashSet<Collision>();
	// map.detectedCollision.add(new Collision(e1, null, new
	// LinkedList<Point>()));
	// map.detectedCollision.add(new Collision(e1, e2, new
	// LinkedList<Point>()));
	// System.out.println(map.detectedCollision.size());
	// }

	protected Set<Entity> map;
	private CollisionMap detectedCollision;

	public Map() {
		this.map = new HashSet<Entity>();
		this.detectedCollision = new CollisionMap();
	}

	public void addEntity(Entity entity) {
		this.map.add(entity);
	}

	public void addEntity(Entity... entities) {
		for (Entity e : entities) {
			this.map.add(e);
		}
	}

	public void removeEntity(Entity entity) {
		this.map.remove(entity);
	}

	public void update() {
		Collisions.detectCollision(this.detectedCollision, map, true);
	}

	/**
	 * Checks if there was a collision reported by the collision detection where the
	 * specified entity is involved.
	 * 
	 * @param entity
	 *            The entity as one of the collision participant.
	 * @return Returns <code>true</code> if there was a collision detected,
	 *         otherwise <code>false</code> is returned.
	 */
	public boolean hasCollision(Entity entity) {
		return detectedCollision.hasCollision(entity);
	}

	public List<Point> getCollision(Shape shape, boolean all) {
		return Collisions.detectFirstCollision(shape, map, all);
	}

	public List<Point> getCollision(Shape shape, Set<Entity> ignore, boolean all) {
		Set<Entity> toCheck = filter(ignore);
		return Collisions.detectFirstCollision(shape, toCheck, all);
	}

	private Set<Entity> filter(Set<Entity> ignore) {
		Set<Entity> toCheck = new HashSet<>(map);
		if (ignore != null) {
			toCheck.removeAll(ignore);
		}
		return toCheck;
	}

	public boolean hasCollision(Shape shape, boolean all) {
		return Collisions.detectFirstCollision(shape, map, all) != null;
	}

	public boolean hasCollision(Shape shape, Set<Entity> ignore, boolean all) {
		Set<Entity> toCheck = filter(ignore);
		return Collisions.detectFirstCollision(shape, toCheck, all) != null;
	}

	/**
	 * Returns the calculated collision for the specified {@link Entity} if there is
	 * one.
	 * 
	 * @param entity
	 *            The entity to check for collisions.
	 * @return Returns the {@link Collision} object for the specified entity if it
	 *         collides with another entity, otherwise <code>null</code> is
	 *         returned.
	 */
	public Collision getCollision(Entity entity) {
		return detectedCollision.getCollision(entity);
	}

	public List<Collision> getCollisions() {
		return this.detectedCollision.getCollisions();
	}

}
