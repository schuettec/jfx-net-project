package com.github.schuettec.world;

import static com.github.schuettec.world.Collisions.detectCollision;
import static com.github.schuettec.world.skills.Skill.asSkill;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.schuettec.math.Point;
import com.github.schuettec.math.Shape;
import com.github.schuettec.world.skills.Camera;
import com.github.schuettec.world.skills.Entity;
import com.github.schuettec.world.skills.Obstacle;
import com.github.schuettec.world.skills.Renderable;
import com.github.schuettec.world.skills.Skill;
import com.github.schuettec.world.skills.Updatable;

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

	protected Set<Entity> allEntities;

	protected Set<Obstacle> obstacles;
	protected Set<Updatable> updateables;
	protected Set<Renderable> renderables;
	protected Set<Camera> cameras;

	private CollisionMap detectedCollision;
	private CollisionMap cameraCollisions;

	public Map() {
		this.allEntities = new HashSet<Entity>();
		this.obstacles = new HashSet<>();
		this.updateables = new HashSet<>();
		this.renderables = new HashSet<>();
		this.cameras = new HashSet<>();
		this.detectedCollision = new CollisionMap();
		this.cameraCollisions = new CollisionMap();
	}

	public void addEntity(Entity... entities) {
		for (Entity e : entities) {
			addEntity(e);
		}
	}

	public void addEntity(Entity entity) {
		this.allEntities.add(entity);
		addEntityBySkill(entity);
	}

	private void addEntityBySkill(Entity entity) {
		addOnDemand(Obstacle.class, this.obstacles, entity);
		addOnDemand(Updatable.class, this.updateables, entity);
		addOnDemand(Renderable.class, this.renderables, entity);
		addOnDemand(Camera.class, this.cameras, entity);
	}

	public void removeEntity(Entity... entities) {
		for (Entity e : entities) {
			removeEntity(e);
		}
	}

	public void removeEntity(Entity entity) {
		this.allEntities.remove(entity);
		removeEntityBySkill(entity);
	}

	private void removeEntityBySkill(Entity entity) {
		removeOnDemand(Obstacle.class, this.obstacles, entity);
		removeOnDemand(Updatable.class, this.updateables, entity);
		removeOnDemand(Renderable.class, this.renderables, entity);
		removeOnDemand(Camera.class, this.cameras, entity);
	}

	private <S extends Skill> void addOnDemand(Class<S> skillType, Set<S> obstacles, Entity entity) {
		Optional<S> asSkill = asSkill(skillType, entity);
		if (asSkill.isPresent()) {
			obstacles.add(asSkill.get());
		}
	}

	private <S extends Skill> void removeOnDemand(Class<S> skillType, Set<S> obstacles, Entity entity) {
		Optional<S> asSkill = asSkill(skillType, entity);
		if (asSkill.isPresent()) {
			obstacles.remove(asSkill.get());
		}
	}

	public void update() {
		updateables.stream().forEach(Updatable::update);
		// Detect all collisions in the set of obstacles
		detectCollision(this.detectedCollision, obstacles, true, true);

		// Detect all collisions in the set of renderables with cameras
		detectCollision(this.detectedCollision, cameras, renderables, false, false);
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

	// START: Ad-hoc collision detection support.

	public List<Point> getCollision(Shape shape, boolean all) {
		return Collisions.detectFirstCollision(shape, obstacles, all);
	}

	public List<Point> getCollision(Shape shape, Set<? extends Obstacle> ignore, boolean all) {
		Set<? extends Obstacle> toCheck = filter(ignore);
		return Collisions.detectFirstCollision(shape, toCheck, all);
	}

	public boolean hasCollision(Shape shape, boolean all) {
		return Collisions.detectFirstCollision(shape, obstacles, all) != null;
	}

	public boolean hasCollision(Shape shape, Set<? extends Obstacle> ignore, boolean all) {
		Set<? extends Obstacle> toCheck = filter(ignore);
		return Collisions.detectFirstCollision(shape, toCheck, all) != null;
	}

	private Set<? extends Obstacle> filter(Set<? extends Obstacle> ignore) {
		Set<? extends Obstacle> toCheck = new HashSet<>(obstacles);
		if (ignore != null) {
			toCheck.removeAll(ignore);
		}
		return toCheck;
	}

	// END: Ad-hoc collision detection support.

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
