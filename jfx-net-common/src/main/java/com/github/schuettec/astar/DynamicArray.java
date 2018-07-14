package com.github.schuettec.astar;

import java.lang.reflect.Array;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class defines a dynamic data structure that represents the possible
 * steps. Every step creates fields for the next possible movements. The field
 * can contain values used for the astar algorithm.
 * 
 * @author cschu
 *
 */
public class DynamicArray<T> {

	public static void main(String[] args) {
		DynamicArray<Coords> array = new DynamicArray<>(Coords.class);
		Coords coords = array.getCursor();
		array.setCurrent(coords);
		for (Direction d : Direction.values()) {
			Coords move = coords.move(d);
			array.set(move, move);
		}
		Coords[][] realArray = array.toArray();
		for (int y = 0; y < realArray.length; y++) {
			for (int x = 0; x < realArray[y].length; x++) {
				System.out.printf("%12s", realArray[y][x]);
			}
		}
	}

	private Class<T> type;
	private Map<Coords, T> array;
	private int minX = 0;
	private int minY = 0;
	private int maxX = 0;
	private int maxY = 0;

	private Coords current;

	public DynamicArray(Class<T> type) {
		this.type = type;
		this.array = new Hashtable<>();
		this.current = new Coords(0, 0);
	}

	public T[][] toArray() {
		int rootY = Math.abs(minY);
		int rows = rootY + 1 + maxY;
		int rootX = Math.abs(minX);
		int cols = rootX + 1 + maxX;
		@SuppressWarnings("unchecked")
		T[][] retArray = (T[][]) Array.newInstance(type, rows, cols);
		Iterator<Coords> it = array.keySet().iterator();
		while (it.hasNext()) {
			Coords deltaCoords = it.next();
			int x = rootX + deltaCoords.x;
			int y = rootY + deltaCoords.y;
			retArray[y][x] = get(deltaCoords);
		}
		return retArray;
	}

	/**
	 * @return Returns the position of the cursor.
	 */
	public Coords getCursor() {
		return current;
	}

	/**
	 * Sets the current cursor to the specified position.
	 * 
	 * @param current
	 *            The new cursor position.
	 */
	public void setCursor(Coords current) {
		this.current = current;
	}

	/**
	 * @return Returns the number of elements this collection is currently
	 *         storing.
	 */
	public int size() {
		return array.size();
	}

	/**
	 * Moves the current cursor to the specified direction. Note that this
	 * operation does not check that an element is available at this position.
	 * 
	 * @param direction
	 *            The direction to move.
	 */
	public void move(Direction direction) {
		this.current = current.move(direction);
	}

	/**
	 * Checks if this arrays has an element at the current cursor position if
	 * moved one step in the specified direction.
	 * <p>
	 * <b>This method does not change the current cursor.</b>
	 * </p>
	 * 
	 * @param direction
	 *            The direction to check from the current cursor position.
	 * @return Returns <code>true</code> if there is an element,
	 *         <code>false</code> otherwise.
	 */
	public boolean hasCurrent(Direction direction) {
		Coords check = current.move(direction);
		return has(check);
	}

	/**
	 * @return Returns the element the cursor currently is pointing to if such
	 *         an element exists.
	 */
	public T getCurrent() {
		return get(current);
	}

	/**
	 * @param value
	 *            Sets this value at the current cursor position.
	 */
	public void setCurrent(T value) {
		set(current, value);
	}

	/**
	 * @return Returns <code>true</code> if there is an element at the current
	 *         cursor position, <code>false</code> otherwise.
	 * 
	 */
	public boolean hasCurrent() {
		return has(current);
	}

	/**
	 * @return Returns <code>true</code> if there is an element at the specified
	 *         coords, <code>false</code> otherwise.
	 *
	 */
	public boolean has(Coords coords) {
		return array.containsKey(coords);
	}

	/**
	 * @param coords
	 *            The specified position.
	 * @return Returns the element the specified coords are pointing to if such
	 *         an element exists.
	 */
	public T get(Coords coords) {
		if (array.containsKey(coords)) {
			return array.get(coords);
		} else {
			throw new NoSuchElementException(String.format("Element does not exists @ %s.", coords));
		}
	}

	/**
	 * Sets the value at the specified coordinates.
	 * 
	 * @param coords
	 *            The coords.
	 * @param value
	 *            The value to set.
	 */
	public void set(Coords coords, T value) {
		if (coords == null) {
			throw new IllegalArgumentException("Coords may not be null.");
		} else {
			array.put(coords, value);
			updateMinMax(coords);
		}
	}

	private void updateMinMax(Coords coords) {
		if (coords.x < minX) {
			minX = coords.x;
		}
		if (coords.y < minY) {
			minY = coords.y;
		}
		if (coords.x > maxX) {
			maxX = coords.x;
		}
		if (coords.y > maxY) {
			maxY = coords.y;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		T[][] realArray = toArray();
		for (int y = 0; y < realArray.length; y++) {
			for (int x = 0; x < realArray[y].length; x++) {
				String string = String.valueOf(realArray[y][x]);
				if (string.length() > 12) {
					string.subSequence(0, 11);
				}
				builder.append(String.format("%12s", string));
			}
			builder.append("\n");
		}
		return builder.toString();
	}

}
