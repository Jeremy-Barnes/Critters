package com.critters.ajax;

import com.critters.bll.CommerceBLL;
import com.critters.dal.dto.ItemRequest;
import com.critters.dal.dto.StoreInformationRequest;
import com.critters.dal.dto.entity.Store;
import com.critters.dal.dto.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

/**
 * Created by Jeremy on 8/28/2016.
 */
@Path("/commerce")
public class CommerceService extends AjaxService {

	@POST
	@Path("/createTradeListing")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTradeListing() {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}

	@POST
	@Path("/moveInventoryItemToStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response moveInventoryItemToStore(JAXBElement<ItemRequest> jsonRequest) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();

		try {
			CommerceBLL.changeItemsStore(request.items, loggedInUser);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path("/setInventoryItemStorePrice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setInventoryItemStorePrice(JAXBElement<ItemRequest> jsonRequest) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();

		try {
			CommerceBLL.changeItemsPrice(request.items, loggedInUser.getUserID());
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path("/removeInventoryItemFromStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeInventoryItemFromStore(JAXBElement<ItemRequest> jsonRequest) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();

		try {
			for (int i = 0; i < request.items.length; i++) request.items[i].setPrice(null);
			CommerceBLL.changeItemsPrice(request.items, loggedInUser.getUserID());

			for (int i = 0; i < request.items.length; i++) request.items[i].setContainingStoreId(null);
			CommerceBLL.changeItemsStore(request.items, loggedInUser);

			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/purchaseInventoryItemFromStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response purchaseInventoryItemFromStore(JAXBElement<ItemRequest> jsonRequest) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();

		//try {
			CommerceBLL.changeItemsOwnerViaPurchase(request.items, loggedInUser);
			return Response.status(Response.Status.OK).build();
	//	} // catch (InvalidPropertyException ex) {
//			return Response.status(Response.Status.PRECONDITION_FAILED).entity(ex.getMessage()).build();
//		} catch (Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		}
	}

	@POST
	@Path("/createStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createStore(JAXBElement<StoreInformationRequest> jsonRequest) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		StoreInformationRequest request = jsonRequest.getValue();
		if(loggedInUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();

		try {
			return Response.status(Response.Status.OK).entity(CommerceBLL.createStore(request.store, request.backgroundImage, request.clerkImage, loggedInUser)).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/editStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editStore(JAXBElement<StoreInformationRequest> jsonRequest) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		StoreInformationRequest request = jsonRequest.getValue();
		if(loggedInUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();

		try {
			return Response.status(Response.Status.OK).entity(CommerceBLL.editStore(request.store, request.backgroundImage, request.clerkImage, loggedInUser)).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/getStorefront/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewStore(@PathParam("id") int id) {
		try {
			Store retrievedStore = CommerceBLL.getStore(id);
			if(retrievedStore != null) {
				Store copiedStore = super.serializeDeepCopy(retrievedStore, Store.class);
				return Response.status(Response.Status.OK).entity(copiedStore).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}


}
