package com.critters.ajax;

import com.critters.ajax.filters.UserSecure;
import com.critters.bll.*;
import com.critters.dal.entity.*;
import com.critters.dto.SearchResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Path("/meta")
public class MetaService extends AjaxService {

	@Path("/pollForNotifications")
	@UserSecure
	@GET
	public void pollForNotification(@Suspended final AsyncResponse asyncResponse) throws InterruptedException {
		User loggedInUser = getSessionUser();
		EventBLL.registerForRandomEventLottery(loggedInUser);
		EventBLL.redeemRandomEventForUser(loggedInUser, asyncResponse);
		ChatBLL.createPoll(loggedInUser.getUserID(), asyncResponse);
	}


	@GET
	@Path("/respondToServerQuery/{queryID}/{response}}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchData(@PathParam("queryID") String queryID, @PathParam("response") String response) {
		//TODO results.items = ItemsBLL.searchItems(searchStr).toArray(new Item[0]);
		return Response.status(200).build();
	}

	@GET
	@Path("/searchUsers/{searchStr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUsers(@PathParam("searchStr") String searchStr) {
		SearchResponse results = new SearchResponse();
		results.users = UserBLL.searchForUser(searchStr).toArray(new User[0]);
		return Response.status(200).entity(results).build();
	}

	@GET
	@Path("/checkPetName/{petName}")
	public boolean checkPetNameAvailability(@PathParam("petName") String petName) {
		return PetBLL.isPetNameValid(petName);
	}

	@GET
	@Path("/checkUserName/{userName}")
	public boolean checkUserNameAvailability(@PathParam("userName") String userName) {
		return UserBLL.isUserNameTaken(userName);
	}

	@GET
	@Path("/checkEmail/{email}")
	public boolean checkEmailAvailability(@PathParam("email") String email) {
		return UserBLL.isEmailAddressTaken(email);
	}

	@GET
	@Path("/getPetSpecies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPetSpecies() {
		return Response.status(Response.Status.OK).entity( //do this because MOXy is a garbage serializer and I don't want to mess with the POM tonight
			   	new GenericEntity<Pet.PetSpecies[]>(PetBLL.getPetSpecies().toArray(new Pet.PetSpecies[0]), Pet.PetSpecies[].class)).build();
	}

	@GET
	@Path("/getPetColors")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPetColors() {
		return Response.status(Response.Status.OK).entity( //do this because MOXy is a garbage serializer and I don't want to mess with the POM tonight
				new GenericEntity<Pet.PetColor[]>(PetBLL.getPetColors().toArray(new Pet.PetColor[0]), Pet.PetColor[].class)).build();
	}

	@GET
	@Path("/getGames")
	public Response getGames() {
		return Response.status(Response.Status.OK).entity(GameBLL.getGames()).build();
	}

	@GET
	@Path("/getUserImageOptions")
	public Response getUserImageOptions() {
		try {
			return Response.status(Response.Status.OK).entity(new GenericEntity<UserImageOption[]>(UserBLL.getUserImageOptions(), UserImageOption[].class)).build(); //todo this will break
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/getShopkeeperImageOptions")
	public Response getShopKeeperImageOptions() {
		try {
			return Response.status(Response.Status.OK).entity(
					new GenericEntity<StoreClerkImageOption[]>(CommerceBLL.getStoreClerkImageOptionsForPresentation(), StoreClerkImageOption[].class)).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/getStoreBackgroundOptions")
	public Response getStoreBackgroundOptions() {
		try {
			return Response.status(Response.Status.OK).entity(
					new GenericEntity<StoreBackgroundImageOption[]>(CommerceBLL.getStoreBackgroundImageOptionsForPresentation(), StoreBackgroundImageOption[].class)).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}


}
