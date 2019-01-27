package com.critters.Utilities;

import java.util.Collection;
import java.util.List;
import java.util.Random;

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

	public static boolean flipACoin(float percentLucky){
		if(percentLucky == 0) return false;
		if(percentLucky == 100) return true;
		Random rand = new Random();
		float rando = rand.nextFloat() * 100;
		return rando >= percentLucky;
	}

	public static <T> T pickRandomItem(List<T> list){
		if(isNullOrEmpty(list)) {
			return null;
		} else {
			Random rand = new Random();
			return list.get(rand.nextInt(list.size()));
		}
	}

}

