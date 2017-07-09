package com.critters.ajax;

import com.critters.bll.CommerceBLL;
import com.critters.dal.dto.ItemRequest;
import com.critters.dal.dto.StoreInformationRequest;
import com.critters.dal.dto.entity.Store;

import com.critters.dal.dto.entity.User;

import javax.resource.spi.InvalidPropertyException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Created by Jeremy on 8/28/2016.
 */
@Path("/commerce")
public class CommerceService extends AjaxService {

	@POST
	@Path("/createTradeListing")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTradeListing() throws GeneralSecurityException, UnsupportedEncodingException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/moveInventoryItemToStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response moveInventoryItemToStore(JAXBElement<ItemRequest> jsonRequest) throws JAXBException, IOException, InvalidPropertyException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			CommerceBLL.changeItemsStore(request.items, loggedInUser);
			return Response.status(Response.Status.OK).build();
		}
	}

	@POST
	@Path("/setInventoryItemStorePrice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setInventoryItemStorePrice(JAXBElement<ItemRequest> jsonRequest) throws JAXBException, IOException, InvalidPropertyException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			CommerceBLL.changeItemsPrice(request.items, loggedInUser);
			return Response.status(Response.Status.OK).build();
		}
	}

	@POST
	@Path("/removeInventoryItemFromStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeInventoryItemFromStore(JAXBElement<ItemRequest> jsonRequest) throws JAXBException, IOException, InvalidPropertyException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			for(int i = 0; i< request.items.length; i++) request.items[i].setPrice(null);

			CommerceBLL.changeItemsPrice(request.items, loggedInUser);

			for(int i = 0; i< request.items.length; i++) request.items[i].setContainingStoreId(null);
			CommerceBLL.changeItemsStore(request.items, loggedInUser);

			return Response.status(Response.Status.OK).build();
		}
	}

	@POST
	@Path("/purchaseInventoryItemFromStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response purchaseInventoryItemFromStore(JAXBElement<ItemRequest> jsonRequest) throws JAXBException, IOException, InvalidPropertyException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			CommerceBLL.changeItemsOwnerViaPurchase(request.items, loggedInUser);
			return Response.status(Response.Status.OK).build();
		}
	}

	@POST
	@Path("/createStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createStore(JAXBElement<StoreInformationRequest> jsonRequest) throws JAXBException, IOException, InvalidPropertyException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		StoreInformationRequest request = jsonRequest.getValue();
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			return Response.status(Response.Status.OK).entity(CommerceBLL.createStore(request.store, request.backgroundImage, request.clerkImage, loggedInUser)).build();
		}
	}

	@POST
	@Path("/editStore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editStore(JAXBElement<StoreInformationRequest> jsonRequest) throws JAXBException, IOException, InvalidPropertyException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		StoreInformationRequest request = jsonRequest.getValue();
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			return Response.status(Response.Status.OK).entity(CommerceBLL.editStore(request.store, request.backgroundImage, request.clerkImage,loggedInUser)).build();
		}
	}

	@GET
	@Path("/getStorefront/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewStore(@PathParam("id") int id) throws JAXBException {
		Store copiedStore = super.serializeDeepCopy(CommerceBLL.getStore(id), Store.class);
		return Response.status(Response.Status.OK).entity(copiedStore).build();

	}


}
