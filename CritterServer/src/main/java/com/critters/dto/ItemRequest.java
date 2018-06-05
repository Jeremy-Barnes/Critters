package com.critters.dto;

import com.critters.dal.entity.Item;
import com.critters.dal.entity.User;

/**
 * Created by Jeremy on 8/9/2016.
 */
public class ItemRequest {
	public User user;
	public Item[] items;
}