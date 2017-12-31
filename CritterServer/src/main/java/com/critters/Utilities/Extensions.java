package com.critters.Utilities;

import java.util.Collection;

/**
 * Created by Jeremy on 12/30/2017.
 */
public class Extensions {

	public static boolean isNullOrEmpty(String object)  {
		return object == null || object.length() == 0;
	}

	public static <T extends Collection> boolean isNullOrEmpty(T object)  {
		return object == null || object.size() == 0;
	}

	public static boolean isNullOrEmpty(Object[] object)  {
		return object == null || object.length == 0;
	}

	public static boolean isNullOrEmpty(int[] object)  {
		return object == null || object.length == 0;
	}

	public static boolean isNullOrEmpty(double[] object)  {
		return object == null || object.length == 0;
	}

	public static boolean isNullOrEmpty(float[] object)  {
		return object == null || object.length == 0;
	}

	public static boolean isNullOrEmpty(long[] object)  {
		return object == null || object.length == 0;
	}

	public static boolean isNullOrEmpty(char[] object)  {
		return object == null || object.length == 0;
	}

	public static boolean isNullOrEmpty(boolean[] object)  {
		return object == null || object.length == 0;
	}

	public static boolean isNullOrEmpty(short[] object)  {
		return object == null || object.length == 0;
	}

	public static boolean isNullOrEmpty(byte[] object)  {
		return object == null || object.length == 0;
	}

}

