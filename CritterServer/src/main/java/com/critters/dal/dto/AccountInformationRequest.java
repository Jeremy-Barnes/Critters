package com.critters.dal.dto;

import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;
import com.critters.dal.dto.entity.UserImageOption;

/**
 * Created by Jeremy on 8/23/2016.
 */
public class AccountInformationRequest {
	public User user;
	public Pet pet;
	public UserImageOption imageChoice;
}
