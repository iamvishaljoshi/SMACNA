/**
 * 
 */
package com.smacna.util;

/**
 * @author vishal.joshi
 * @version 1.0 
 * This utility will provide you two sides details as per the
 *          specification.
 */
public class DuctSides {

	/**
	 * 
	 * @param height
	 * @param width
	 * @return Integer[]
	 */
	public static int[] getSides(int height, int width) {

		int sides[] = new int[2];
		if (height > width) {

			sides[0] = height;
			sides[1] = width;

		} else {
			sides[0] = width;
			sides[1] = height;
		}

		return sides;
	}
}
