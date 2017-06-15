package com.critters.dal.dto;

import com.critters.dal.dto.entity.Message;
import com.critters.dal.dto.entity.User;

import java.util.List;

/**
 * Created by Jeremy on 6/12/2017.
 */
public class MessageRequest extends DTO {
	public User user;
	public List<Message> messages;
}
