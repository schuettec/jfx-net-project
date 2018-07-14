package com.github.schuettec.astar;

class Coords {
	int x;
	int y;

	public Coords(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a new {@link Coords} from an array [y,x].
	 * 
	 * @param newCoordsYX
	 *            The new coords [y,x]
	 */
	protected Coords(int[] newCoordsYX) {
		this(newCoordsYX[1], newCoordsYX[2]);
	}

	protected int getX() {
		return x;
	}

	protected int getY() {
		return y;
	}

	protected int[] getYX() {
		return new int[] { y, x };
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Coords other = (Coords) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

	public Coords move(Direction direction) {
		return new Coords(x + direction.getDX(), y + direction.getDY());
	}

}
