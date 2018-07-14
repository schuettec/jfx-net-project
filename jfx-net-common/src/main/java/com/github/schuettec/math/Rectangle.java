package com.github.schuettec.math;

import static com.github.schuettec.math.Math2D.getRectangle;

/**
 * This class defines a Rectangle, which is a special {@link Polygon}.
 * 
 * @author schuettec
 *
 */
public class Rectangle extends Polygon {

	private double width;
	private double height;

	public Rectangle(Point upperLeft, double width, double height) {
		super(getRectangle(upperLeft, width, height));
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
