package com.critters.dto;

import com.critters.DTO;
import com.critters.dal.entity.Item;
import com.critters.dal.entity.User;

/**
 * Created by Jeremy on 8/22/2016.
 */
public class SearchResponse extends DTO {
	public User[] users;
	public Item[] items;
}
