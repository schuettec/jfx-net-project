package com.github.schuettec.world;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.schuettec.math.Circle;
import com.github.schuettec.math.Line;
import com.github.schuettec.math.Math2D;
import com.github.schuettec.math.Point;
import com.github.schuettec.math.Polygon;
import com.github.schuettec.math.Shape;
import com.github.schuettec.world.skills.Entity;
import com.github.schuettec.world.skills.Obstacle;

/**
 * A public util class with methods to perform a collision detection.
 * 
 * @author schuettec
 *
 */
public class Collisions {

	/**
	 * Calculates the collision of all entities in the specified set.
	 * 
	 * @param collisionMap
	 *            A collision map to store the detected collisions.
	 * @param map
	 *            The set of entities to detect collisions for.
	 * @param all
	 *            Specified if all collision points should be calculated. If
	 *            <code>false</code> only the first collision point will be
	 *            calculated. If <code>true</code> all the other points will be
	 *            calculated.
	 */
	public static void detectCollision(CollisionMap collisionMap, Set<Entity> map, boolean all) {
		collisionMap.clearCollisions();

		for (Entity c1 : new HashSet<>(map)) {
			if (!(c1 instanceof Obstacle)) {
				continue;
			}

			for (Entity c2 : new HashSet<>(map)) {
				if (!(c2 instanceof Obstacle)) {
					continue;
				}
				if (c1 == c2)
					continue;

				Obstacle o1 = (Obstacle) c1;
				Obstacle o2 = (Obstacle) c2;
				{
					Collision collision = detectCollision(o1, o2, all);
					// Collision may be null if there is none
					if (collision != null) {
						Collision reverse = detectCollision(o2, o1, all);
						collisionMap.addCollisionsBidirectional(collision, reverse);
					}
				}

			}
		}
	}

	/**
	 * Calculates the first collision of the specified shape with all entities in
	 * the specified set.
	 * 
	 * @param shape
	 *            The shape used to check collisions.
	 * @param map
	 *            The set of entities to detect collisions with the shape.
	 * @param all
	 *            Specified if all collision points should be calculated. If
	 *            <code>false</code> only the first collision point will be
	 *            calculated. If <code>true</code> all the other points will be
	 *            calculated.
	 * @return Returns the collision points on the shape as a list.
	 */
	public static List<Point> detectFirstCollision(Shape shape, Set<Entity> map, boolean all) {

		for (Entity c1 : new HashSet<>(map)) {
			if (!(c1 instanceof Obstacle)) {
				continue;
			}
			Obstacle o1 = (Obstacle) c1;
			List<Point> collision = detectCollision(shape, o1.getCollisionShape(), all);
			// Collision may be null if there is none
			if (collision != null) {
				collision = detectCollision(o1.getCollisionShape(), shape, all);
			}
			if (!collision.isEmpty()) {
				return collision;
			}
		}
		return null;
	}

	/**
	 * Detects collisions for two {@link Obstacle}s.
	 * 
	 * @param e1
	 *            The obstacle.
	 * @param e2
	 *            the other obstacle. The set of entities to detect collisions with
	 *            the shape.
	 * @param all
	 *            Specified if all collision points should be calculated. If
	 *            <code>false</code> only the first collision point will be
	 *            calculated. If <code>true</code> all the other points will be
	 *            calculated.
	 * @return Returns a {@link Collision} object that manages the list of collision
	 *         points.
	 */
	public static Collision detectCollision(Obstacle e1, Obstacle e2, boolean all) {
		Shape s1 = e1.getCollisionShape();
		Shape s2 = e2.getCollisionShape();
		List<Point> collisions = detectCollision(s1, s2, all);
		return new Collision(e1, e2, collisions);
	}

	public static List<Point> detectCollision(Shape s1, Shape s2, boolean all) {
		List<Point> collisions = null;

		if (s1 instanceof Polygon && s2 instanceof Polygon) {
			collisions = _detectCollision((Polygon) s1, (Polygon) s2, all);
		} else if (s1 instanceof Circle && s2 instanceof Circle) {
			collisions = _detectCollision((Circle) s1, (Circle) s2, all);
		} else if (s1 instanceof Circle && s2 instanceof Polygon) {
			collisions = _detectCollision((Circle) s1, (Polygon) s2, all);
		} else {
			collisions = _detectCollision((Circle) s2, (Polygon) s1, all);
		}

		return collisions;
	}

	private static List<Point> _detectCollision(Circle c1, Circle c2, boolean all) {
		List<Point> collisions = new LinkedList<>();

		double d = Math2D.getEntfernung(c1.getPosition(), c2.getPosition());

		double r0 = c1.getRadius();
		double r1 = c2.getRadius();
		if (d > r0 + r1) {
			return collisions;
		} else if (d < Math.abs(r0 - r1)) {
			return collisions;
		} else if (d == 0 && r0 == r1) {
			Point p = c1.getPosition().clone();
			collisions.add(p);
			return collisions;
		}

		double a = (Math.pow(r0, 2) - Math.pow(r1, 2) + Math.pow(d, 2)) / (2d * d);
		double h = Math.sqrt(Math.pow(r0, 2) - Math.pow(a, 2));

		Point p0 = c1.getPosition();
		Point p1 = c2.getPosition();
		Point p2 = new Point(p0.getX() + a * (p1.getX() - p0.getX()) / d, p0.getY() + a * (p1.getY() - p0.getY()) / d);

		Point p31 = new Point(p2.getX() + h * (p1.getY() - p0.getY()) / d, p2.getY() - h * (p1.getX() - p0.getX()) / d);
		Point p32 = new Point(p2.getX() - h * (p1.getY() - p0.getY()) / d, p2.getY() + h * (p1.getX() - p0.getX()) / d);

		collisions.add(p31);
		collisions.add(p32);

		return collisions;

	}

	private static List<Point> _detectCollision(Circle p1, Polygon p2, boolean all) {
		List<Point> collisions = new LinkedList<>();
		List<Line> h2 = p2.getLines();
		for (Line l2 : h2) {
			// Kreisgleichung:
			// (x - xM)² + ([m * x + n] - yM)² = r²
			// =>
			// x² - ((xm + cm) / (m² + 1)) x + (xm² + c² - r²) / (m² + 1) = 0
			double xm = p1.getPosition().x;
			double ym = p1.getPosition().y;
			double r = p1.getRadius();
			double m = l2.getM();
			double n = l2.getB();

			double p = ((-2d * xm + 2d * m * n - 2d * m * ym) / (Math.pow(m, 2) + 1d));
			double q = ((Math.pow(xm, 2) + Math.pow(n - ym, 2) - Math.pow(r, 2)) / (Math.pow(m, 2) + 1d));

			double[] results = Math2D.pqFormula(p, q);

			if (results.length > 0) {
				for (double result : results) {
					if (result >= 0) {
						double x = result;
						double y = m * x + n;
						Point point = new Point(x, y);
						if (l2.isDefined(point)) {
							collisions.add(point);
						}
					}
				}
			}
		}

		return collisions;
	}

	/**
	 * Calculates the collision of two polygons.
	 * 
	 * @param p1
	 *            A Polygon
	 * @param p2
	 *            the other polygon
	 * @param all
	 *            Specified if all collision points should be calculated. If
	 *            <code>false</code> only the first collision point will be
	 *            calculated. If <code>true</code> all the other points will be
	 *            calculated.
	 * 
	 * @return Returns the list of collision points. How much collision points will
	 *         be calculated is specified with the <code>all</code> parameter.
	 */
	private static List<Point> _detectCollision(Polygon p1, Polygon p2, boolean all) {
		List<Point> collisions = new LinkedList<>();
		List<Line> h1 = p1.getLines();
		List<Line> h2 = p2.getLines();
		for (Line l1 : h1) {
			for (Line l2 : h2) {
				Point intersection = l1.intersects(l2);
				if (intersection != null) {
					collisions.add(intersection);
					if (!all) {
						return collisions;
					}
				}
			}
		}
		return collisions;
	}

}
