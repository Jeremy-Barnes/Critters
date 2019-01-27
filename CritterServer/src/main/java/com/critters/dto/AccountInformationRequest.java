package com.critters.dto;

import com.critters.DTO;
import com.critters.dal.entity.Pet;
import com.critters.dal.entity.User;
import com.critters.dal.entity.UserImageOption;

/**
 * Created by Jeremy on 8/23/2016.
 */
public class AccountInformationRequest extends DTO {
	public User user;
	public Pet pet;
	public UserImageOption imageChoice;
}
