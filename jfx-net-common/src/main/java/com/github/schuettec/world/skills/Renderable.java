package com.github.schuettec.world.skills;

/**
 * Marker-interface for {@link Obstacle}s that should be visible for
 * link{Camera}s. A {@link Renderable} is visible if its collision shape
 * returned by {@link Obstacle#getCollisionShape()} collides with the shape of
 * the {@link Camera}.
 * 
 * @author schuettec
 *
 */
public interface Renderable extends Obstacle {

}
