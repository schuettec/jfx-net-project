package com.github.schuettec.astar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.github.schuettec.math.Circle;
import com.github.schuettec.math.Math2D;
import com.github.schuettec.math.Point;
import com.github.schuettec.world.AbstractCircleObstacle;
import com.github.schuettec.world.Map;
import com.github.schuettec.world.skills.Obstacle;

public class Algorithm {

	public static List<Point> findPath(AbstractCircleObstacle entity, Point target, Map map, double stepRadius) {

		PriorityQueue<Point> openList = new PriorityQueue<Point>(new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return Double.compare(Math2D.getEntfernung(o1, target), Math2D.getEntfernung(o2, target));
			}
		});

		Set<Point> closedList = new HashSet<>();

		Point startNode = entity.getPosition();

		openList.add(startNode);

		Point current = null;

		double targetRadius = entity.getRadius() * 3; // TODO: REMOVE THE
														// FACTOR, THIS IS ONLY
														// FOR TESTING

		// Create a visited-map.
		DynamicArray<Boolean> visitedMap = new DynamicArray<>(Boolean.class);
		getData(startNode).visitedMapCoords = visitedMap.getCursor();
		// The current step is already visited.
		visitedMap.setCurrent(true);

		do {
			System.out.println("Läuft");
			// for (Point p : openList) {
			// javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(5);
			// c.setFill(Color.GRAY);
			// c.setTranslateX(p.x);
			// c.setTranslateY(p.y);
			// Testapp.DEBUG.getChildren().add(c);
			// }

			// try {
			// Thread.sleep(200);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			current = openList.poll();

			// final Point finalCurrent = current;
			// javafx.scene.shape.Circle t = new javafx.scene.shape.Circle(targetRadius);
			// t.setFill(null);
			// t.setStroke(Color.MAGENTA);
			// t.setTranslateX(target.x);
			// t.setTranslateY(target.y);
			// Testapp.DEBUG.getChildren().add(t);

			// javafx.scene.shape.Circle c = new javafx.scene.shape.Circle(5);
			// c.setFill(Color.DARKCYAN);
			// c.setTranslateX(finalCurrent.x);
			// c.setTranslateY(finalCurrent.y);
			// Testapp.DEBUG.getChildren().add(c);

			if (Math2D.isInCircle(current, target, targetRadius)) {
				break;
			}

			closedList.add(current);

			expandNode(visitedMap, current, entity, target, map, openList, closedList, stepRadius);

		} while (!openList.isEmpty());

		// Reconstruct path
		List<Point> path = new LinkedList<>();

		while ((current = getData(current).next) != null) {
			path.add(current);
		}

		return path;
	}

	private static double heuristic(Point current, Point successor, AbstractCircleObstacle start, Point end, Map map) {
		return Math2D.getEntfernung(successor, end);
	}

	private static void expandNode(DynamicArray<Boolean> visitedMap, Point current, AbstractCircleObstacle start,
			Point end, Map map, PriorityQueue<Point> openList, Set<Point> closedList, double radius) {

		visitedMap.setCursor(getData(current).visitedMapCoords);

		List<Point> successors = new ArrayList<>();

		for (Direction direction : Direction.values()) {

			if (visitedMap.hasCurrent(direction)) {
				// If the visited map already knows about this field, drop it,
				// we already visited it.
				continue;
			}

			double dx = direction.getDX() * radius;
			double dy = direction.getDY() * radius;
			Point stepPoint = current.clone();
			stepPoint.translate(dx, dy);
			Coords visited = visitedMap.getCursor().move(direction);
			getData(stepPoint).visitedMapCoords = visited;

			// Create collision shape: A line from current point to the next
			// step.
			Circle stepShape = start.getCollisionShape().clone().setPosition(stepPoint);
			Set<Obstacle> ignore = new HashSet<>();
			ignore.add(start);
			boolean hasCollision = map.adHocDetection().hasCollision(stepShape, ignore, false);
			if (!hasCollision) {
				successors.add(stepPoint);
				visitedMap.set(visited, true);
			}
		}

		for (Point successor : successors) {

			if (closedList.contains(successor)) {
				continue;
			}

			double tentative_g = getData(current).priority + 1;

			if (openList.contains(successor) && tentative_g >= getData(successor).g) {
				continue;
			}

			getData(successor).next = current;
			getData(successor).g = tentative_g;

			double f = tentative_g + heuristic(current, successor, start, end, map);

			if (openList.contains(successor)) {
				openList.remove(successor);
			}
			getData(successor).priority = f;
			openList.add(successor);

		}

	}

	public static AStarData getData(Point p) {
		if (p.userObject == null) {
			p.userObject = new AStarData();
		}
		return (AStarData) p.userObject;
	}

}
