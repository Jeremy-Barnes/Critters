package com.critters.ajax;

import com.critters.backgroundservices.BackgroundJobManager;
import com.critters.bll.PetBLL;
import com.critters.bll.UserBLL;
import com.critters.dal.dto.SearchResponse;
import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;

import javax.resource.spi.InvalidPropertyException;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Path("/meta")
public class MetaService extends AjaxService {
	private static Map<Integer, AsyncResponse> peers = Collections.synchronizedMap(new HashMap<Integer, AsyncResponse>());

	@Path("/jobs")
	@GET
	@Produces("text/plain")
	public Response checkJobs() {
		return Response.status(Response.Status.OK).entity(BackgroundJobManager.jobs).build();
	}

	@Path("/poll")
	@POST
	public void poll(@Suspended final AsyncResponse asyncResponse) throws InterruptedException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");

		if (loggedInUser != null) {
			asyncResponse.setTimeout(10, TimeUnit.SECONDS);
			peers.put(1, asyncResponse);
		}
	}

	@GET
	@Path("/search/{searchStr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchData(@PathParam("searchStr") String searchStr) throws JAXBException, GeneralSecurityException, IOException {
		SearchResponse results = new SearchResponse();
		results.users = UserBLL.searchUsers(searchStr).toArray(new User[0]);
		//TODO results.items = ItemsBLL.searchItems(searchStr).toArray(new Item[0]);
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
		return UserBLL.isUserNameValid(userName);
	}

	@GET
	@Path("/checkEmail/{email}")
	public boolean checkEmailAvailability(@PathParam("email") String email) {
		return UserBLL.isEmailAddressValid(email);
	}

	@GET
	@Path("/getPetSpecies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPetSpecies() throws JAXBException, IOException, InvalidPropertyException {
		return Response.status(Response.Status.OK).entity( //do this because MOXy is a garbage serializer and I don't want to mess with the POM tonight
			   	new GenericEntity<Pet.PetSpecies[]>(PetBLL.getPetSpecies().toArray(new Pet.PetSpecies[0]), Pet.PetSpecies[].class)).build();

	}

	@GET
	@Path("/getPetColors")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPetColors() throws JAXBException, IOException, InvalidPropertyException {
		return Response.status(Response.Status.OK).entity( //do this because MOXy is a garbage serializer and I don't want to mess with the POM tonight
				new GenericEntity<Pet.PetColor[]>(PetBLL.getPetColors().toArray(new Pet.PetColor[0]), Pet.PetColor[].class)).build();
	}


}
