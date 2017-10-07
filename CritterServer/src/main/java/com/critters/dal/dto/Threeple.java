package com.critters.dal.dto;

/**
 * Created by Jeremy on 7/17/2017.
 */
public class Threeple<X, Y, Z> {
	public X x;
	public Y y;
	public Z z;

	public Threeple(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
