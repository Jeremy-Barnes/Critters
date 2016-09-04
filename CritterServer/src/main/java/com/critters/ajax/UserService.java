package com.critters.ajax;

import com.critters.bll.PetBLL;
import com.critters.bll.UserBLL;
import com.critters.dal.dto.AuthToken;
import com.critters.dal.dto.CreateAccountRequest;
import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;

import javax.resource.spi.InvalidPropertyException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * Created by Jeremy on 8/7/2016.
 */
@Path("/users")
public class UserService extends AjaxService{

	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(JAXBElement<CreateAccountRequest> jsonRequest) throws Exception {
		CreateAccountRequest request = jsonRequest.getValue();
		//todo checkName availability
		//todo checkPetName availability
		String validator = UserBLL.createUserReturnUnHashedValidator(request.user);
		Pet pet = PetBLL.createPet(request.pet, request.user);
		ArrayList<Pet> pets = new ArrayList<Pet>();
		pets.add(pet);
		request.user.setPets(pets);
		httpRequest.getSession().setAttribute("user", request.user);
		User copiedUser = super.serializeDeepCopy(request.user, User.class);
		return Response.status(Response.Status.OK).cookie(createUserCookies(copiedUser))
					   .entity(UserBLL.wipeSensitiveFields(copiedUser)).build();
	}

	@POST
	@Path("/getUserFromLogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserFromLogin(JAXBElement<User> jsonUser) throws Exception {
		User user = jsonUser.getValue();
		user = UserBLL.getUser(user.getEmailAddress(), user.getPassword(), true);

		httpRequest.getSession().setAttribute("user", user);
		User copiedUser = super.serializeDeepCopy(user, User.class);

		return Response.status(Response.Status.OK)
					   .cookie(createUserCookies(copiedUser))
					   .entity(UserBLL.wipeSensitiveFields(copiedUser)).build();
	}

	@POST
	@Path("/getUserFromToken")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserFromToken(JAXBElement<AuthToken> jsonToken) throws GeneralSecurityException, UnsupportedEncodingException, JAXBException {
		AuthToken token = jsonToken.getValue();

		User user = UserBLL.getUser(token.selector, token.validator);
		httpRequest.getSession().setAttribute("user", user);
		User copiedUser = super.serializeDeepCopy(user, User.class);

		return Response.status(Response.Status.OK)
					   .entity(UserBLL.wipeSensitiveFields(copiedUser)).build();
	}

	@POST
	@Path("/changeUserInformation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeUserInformation(JAXBElement<User> jsonUser) throws JAXBException, GeneralSecurityException, IOException {
		User user = jsonUser.getValue();
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");

		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		try {
			user = UserBLL.updateUser(user, loggedInUser);
		} catch(InvalidPropertyException e){
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		return Response.status(Response.Status.OK).entity(UserBLL.wipeSensitiveFields(user)).build();
	}
}
