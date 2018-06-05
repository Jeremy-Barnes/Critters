package com.critters.dto;

import com.critters.dal.entity.Store;
import com.critters.dal.entity.StoreBackgroundImageOption;
import com.critters.dal.entity.StoreClerkImageOption;

/**
 * Created by Jeremy on 3/4/2017.
 */
public class StoreInformationRequest {
	public Store store;
	public StoreBackgroundImageOption backgroundImage;
	public StoreClerkImageOption clerkImage;
}
