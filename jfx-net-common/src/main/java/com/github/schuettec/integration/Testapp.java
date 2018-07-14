package com.github.schuettec.integration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.schuettec.astar.Algorithm;
import com.github.schuettec.math.Point;
import com.github.schuettec.world.AbstractCircleObstacle;
import com.github.schuettec.world.Collision;
import com.github.schuettec.world.EntityPoint;
import com.github.schuettec.world.Map;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Testapp extends Application {

	public static void main(String[] args) {
		Application.launch(Testapp.class, args);

	}

	protected int radius = 40;
	protected int viewWidth = 1024;
	protected int viewHeight = 786;

	public static Pane DEBUG;

	@Override
	public void start(Stage primaryStage) throws Exception {

		Pane sceneContent = new Pane();
		Pane debugContent = new Pane();
		StackPane stack = new StackPane(sceneContent, debugContent);
		DEBUG = debugContent;

		CircleEntity user = new CircleEntity(new AbstractCircleObstacle(new Point(800, 800), 1));
		user.setScale(65);

		CircleEntity c1 = new CircleEntity(new AbstractCircleObstacle(new Point(800, 700), 1));
		c1.setScale(65);

		PolygonEntity e1 = new PolygonEntity(new Point(300, 300), new EntityPoint(45d, 1d), new EntityPoint(135d, 1d),
				new EntityPoint(225d, 1d), new EntityPoint(315d, 1d));
		e1.setScale(65);

		PolygonEntity e21 = new PolygonEntity(new Point(300, 300), new EntityPoint(45d, 1d), new EntityPoint(135d, 1d),
				new EntityPoint(225d, 1d), new EntityPoint(315d, 1d));
		e21.setScale(65);

		PolygonEntity e2 = new PolygonEntity(new Point(200, 200), new EntityPoint(45d, 1d), new EntityPoint(135d, 1d),
				new EntityPoint(225d, 1d), new EntityPoint(315d, 1d));
		e2.setScale(65);
		PolygonEntity e3 = new PolygonEntity(new Point(400, 400), new EntityPoint(45d, 1d), new EntityPoint(135d, 1d),
				new EntityPoint(225d, 1d), new EntityPoint(315d, 1d));
		e3.setScale(65);
		PolygonEntity e4 = new PolygonEntity(new Point(250, 250), new EntityPoint(45d, 1d), new EntityPoint(135d, 1d),
				new EntityPoint(200d, 1d), new EntityPoint(235d, 1d), new EntityPoint(265d, 1d),
				new EntityPoint(300d, 1d), new EntityPoint(330d, 1d), new EntityPoint(360d, 1d));
		e4.setScale(65);

		PolygonEntity e5 = new PolygonEntity(new Point(650, 650), new EntityPoint(45d, 1d), new EntityPoint(135d, 1d),
				new EntityPoint(200d, 1d), new EntityPoint(235d, 1d), new EntityPoint(265d, 1d),
				new EntityPoint(300d, 1d), new EntityPoint(330d, 1d), new EntityPoint(360d, 1d));
		e5.setScale(65);

		PolygonEntity e6 = new PolygonEntity(new Point(350, 650), new EntityPoint(45d, 1d), new EntityPoint(135d, 1d),
				new EntityPoint(200d, 1d), new EntityPoint(235d, 1d), new EntityPoint(265d, 1d),
				new EntityPoint(300d, 1d), new EntityPoint(330d, 1d), new EntityPoint(360d, 1d));
		e6.setScale(65);

		Map map = new Map();
		map.addEntity(user, c1, e1, e21, e2, e3, e4, e5, e6);

		sceneContent.getChildren().addAll(user, c1, e1, e21, e2, e3, e4, e5, e6);

		synchronizeEntities(sceneContent);

		AtomicInteger times = new AtomicInteger(0);

		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				synchronizeEntities(sceneContent);

				map.update();

				// Detect full collision set to show them all
				List<Collision> detectCollision = map.getCollisions();

				for (Collision collision : detectCollision) {

					for (Point p : collision.getPoints()) {
						Circle c = new Circle(5);
						c.setFill(Color.RED);
						c.setTranslateX(p.x);
						c.setTranslateY(p.y);
						sceneContent.getChildren().add(c);
						Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10),
								new KeyValue(c.opacityProperty(), 100), new KeyValue(c.opacityProperty(), 0)));
						timeline.setOnFinished(e -> {
							sceneContent.getChildren().remove(c);
						});
						timeline.play();
					}
				}

				// Animate
				e1.rotate(1);
				e21.rotate(1);
				e2.rotate(1);
				e3.rotate(1);
				e4.rotate(1);

				times.incrementAndGet();

				if (times.get() == 100) {

					long before = System.nanoTime();

					List<Point> findPath = Algorithm.findPath(user, e1.getPosition(), map, user.getRadius() / 2d);
					System.out.println(findPath.size());

					for (Point p : findPath) {

						Circle c = new Circle(5);
						c.setFill(Color.GREEN);
						c.setTranslateX(p.x);
						c.setTranslateY(p.y);
						DEBUG.getChildren().add(c);

					}

					long after = System.nanoTime();

					System.out.println("Pathfinding took " + TimeUnit.NANOSECONDS.toMillis(after - before) + "ms.");

				}

			}

		};
		timer.start();

		List<Circle> paths = new LinkedList<>();

		// Find path:

		// Thread t = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// List<Point> findPath = Algorithm.findPath(user, e1.getPosition(),
		// map, user.getRadius() / 2d);
		// System.out.println(findPath.size());
		//
		// for (Point p : findPath) {
		//
		// Circle c = new Circle(5);
		// c.setFill(Color.GREEN);
		// c.setTranslateX(p.x);
		// c.setTranslateY(p.y);
		// paths.add(c);
		//
		// }
		// }
		// });
		// t.start();

		debugContent.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// user.setPosition(mouseEvent.getX(), mouseEvent.getY());
				// mouseEvent.consume();

			}
		});

		Scene scene = new Scene(stack, 1000, 1000);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void synchronizeEntities(Pane sceneContent) {
		// Call synchronize on JFxEntities
		Iterator<Node> it = sceneContent.getChildren().iterator();

		while (it.hasNext()) {
			Node node = it.next();
			if (node instanceof JFxEntity) {
				JFxEntity jFxEntity = (JFxEntity) node;
				jFxEntity.synchronize();
			}
		}
	}
}
