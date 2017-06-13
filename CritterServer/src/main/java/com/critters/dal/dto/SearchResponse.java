package com.critters.dal.dto;

import com.critters.dal.dto.entity.Item;
import com.critters.dal.dto.entity.User;

/**
 * Created by Jeremy on 8/22/2016.
 */
public class SearchResponse extends DTO {
	public User[] users;
	public Item[] items;
}
