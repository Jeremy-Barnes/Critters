package com.critters.ajax;

import com.critters.ajax.filters.UserSecure;
import com.critters.bll.UserBLL;
import com.critters.dto.ItemRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;


/**
 * Created by Jeremy on 8/7/2016.
 */
@Path("/items")
public class ItemService extends AjaxService{
	@POST
	@Path("/discardInventoryItem")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response discardInventoryItem(JAXBElement<ItemRequest> jsonRequest) {
		ItemRequest request = jsonRequest.getValue();
		UserBLL.discardInventoryItems(request.items, getSessionUser());
		return Response.status(Response.Status.OK).build();

	}

	@GET
	@Path("/use/{itemID}/{actionID}/{targetID}")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response inventoryItemAction(@PathParam("itemID") int item, @PathParam("actionID") int action, @PathParam("targetID") int target) {
		throw new NotImplementedException();
		//if item = sword && action == equip then target = petID
	}
}
