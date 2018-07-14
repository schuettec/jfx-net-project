package com.github.schuettec.astar;

public enum Direction {
	//@formatter:off
	NORTH_WEST(-1, -1), NORTH(-1, 0), NORTH_EAST(-1, 1),
	      WEST( 0, -1),CENTER( 0, 0),       EAST( 0, 1),
	SOUTH_WEST( 1, -1), SOUTH(1, 0), SOUTH_EAST(1, 1);
    //@formatter:on

	private final int dx;
	private final int dy;

	private Direction(int dy, int dx) {
		this.dy = dy;
		this.dx = dx;
	}

	protected int getDY() {
		return dy;
	}

	protected int getDX() {
		return dx;
	}

	/**
	 * Checks if the specified direction is allowed to go from this
	 * {@link Direction}.
	 * 
	 * @param toDirection
	 *            The direction to move to.
	 * @return Returns <code>true</code> if the movement is valid, otherwise
	 *         <code>false</code> is returned.
	 */
	public boolean canMove(Direction toDirection) {
		try {
			toCoordinates(toDirection);
			return true;
		} catch (InvalidDirectionException e) {
			return false;
		}
	}

	/**
	 * Returns the {@link Direction} next to this {@link Direction} if it is
	 * allowed to move there.
	 * 
	 * @param toDirection
	 *            The direction to move to.
	 * @return Returns the resulting direction.
	 */
	public Direction move(Direction toDirection) {
		return fromCoordinates(toCoordinates(toDirection));
	}

	protected Coords toCoordinates(Direction toDirection) {
		int i = ordinal();
		int toAddX = toDirection.getDX();
		int toAddY = toDirection.getDY();
		int[] newCoords = new int[] { (i / 3) + toAddY, (i % 3) + toAddX };
		denyInvalidCoords(newCoords, toDirection);
		return new Coords(newCoords);
	}

	private static Direction fromCoordinates(Coords coords) {
		int[] yx = coords.getYX();

		if (yx[0] > 2)
			throw new IllegalArgumentException(String.format("%d >= 2 for y which is not allowed!"));
		if (yx[1] > 2)
			throw new IllegalArgumentException(String.format("%d >= 2 for x which is not allowed!"));

		int index = yx[0] * 3 + yx[1];
		return Direction.values()[index];
	}

	private void denyInvalidCoords(int[] yx, Direction to) {
		if (!isValid(yx)) {
			throw new InvalidDirectionException(
					String.format("Invalid direction going from %s to %s.", this.toString(), to.toString()));
		}
	}

	private static boolean isValid(int[] yx) {
		return isValid(yx[1], yx[0]);

	}

	private static boolean isValid(int x, int y) {
		return _isValid(x) && _isValid(y);
	}

	private static boolean _isValid(int xy) {
		return xy >= 0 && xy < 3;
	}

}
