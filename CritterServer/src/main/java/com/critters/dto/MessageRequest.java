package com.critters.dto;

import com.critters.DTO;
import com.critters.dal.entity.Message;
import com.critters.dal.entity.User;

import java.util.List;

/**
 * Created by Jeremy on 6/12/2017.
 */
public class MessageRequest extends DTO {
	public User user;
	public List<Message> messages;
}
