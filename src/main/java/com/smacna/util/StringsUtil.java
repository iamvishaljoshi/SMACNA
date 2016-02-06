package com.smacna.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author vishal.joshi
 * @version 1.0
 * This class is used for String formatting
 */
public class StringsUtil {

	public static void main(String args[]) {
		System.out.println(StringsUtil.stripGarbageSpace(new java.util.Date()
				.toString()));
		Date date = new Date();
		System.out.println(new Timestamp(date.getTime()));
		System.out.println(stripGarbageT25("20 ga (R)").length);

	}
	/**
	 * @param aString
	 * @return String Array of the given string parameter
	 */
	public static String[] stripGarbage(String aString) {
		String[] splittArray = null;
		if (aString != null && !aString.equalsIgnoreCase("")) {
			splittArray = aString.split("-");
			System.out.println(aString + " " + splittArray.length);
		}
		return splittArray;
	}

	/**
	 * @param aString
	 * @return String Array of the given string parameter
	 */
	public static String[] stripGarbageT25(String aString) {
		String[] splittArray = null;
		if (aString != null && !aString.equalsIgnoreCase("")) {
			splittArray = aString.split(" ");
			System.out.println(aString + " " + splittArray.length);
		}
		return splittArray;
	}

	/**
	 * @param aString
	 * @return String Array of the given string parameter
	 */
	public static String stripGarbageSpace(String aString) {
		String[] splittArray = null;
		String s = "";
		if (aString != null && !aString.equalsIgnoreCase("")) {
			splittArray = aString.split(" ");
		}
		System.out.println(splittArray.length);
		for (int i = 0; i < splittArray.length; i++) {
			s = s.concat(splittArray[i]);
			System.out.println(s);

		}
		return s;
	}

	/**
	 * @param aString
	 * @return String Array
	 */
	public static String[] stripRecipient(String aString) {
        String[] splittArray = null;
		if (aString != null && !aString.equalsIgnoreCase("")) {
            splittArray = aString.split(",");
            System.out.println(aString + " " + splittArray.length);
        }
        return splittArray;
    }

}
