package utils;

/**
 * Used because this game is so dependent on the seperate 4 ordinal directions
 * 
 * The program tries to associate the integers to the dir:
 * 
 * 0-n
 * 1-e
 * 2-s
 * 3-w
 * 
 * @author diego
 */
public enum Dir4 {
	NORTH,
	EAST,
	SOUTH,
	WEST,
	NONE;
	
	public static Dir4 get(int i) {
		return Dir4.values()[i];
	}
}
