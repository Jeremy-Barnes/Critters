package com.critters.ajax;

import com.critters.bll.PetBLL;
import com.critters.bll.UserBLL;
import com.critters.dal.dto.*;
import com.critters.dal.dto.entity.Pet;
import com.critters.dal.dto.entity.User;

import javax.resource.spi.InvalidPropertyException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
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
	public Response createUser(JAXBElement<AccountInformationRequest> jsonRequest){
		AccountInformationRequest request = jsonRequest.getValue();

		boolean emailAvailable;
		boolean userNameAvailable = true;
		boolean petAvailable = true;

		emailAvailable = UserBLL.isEmailAddressValid(request.user.getEmailAddress());
		if (emailAvailable) {
			userNameAvailable = UserBLL.isUserNameValid(request.user.getUserName());
			if (userNameAvailable) {
				petAvailable = PetBLL.isPetNameValid(request.pet.getPetName());
			}
		}

		if(!emailAvailable || !userNameAvailable || !petAvailable){
			String propertyMessage = !emailAvailable  ? "email address is already tied to an account!" :
					!userNameAvailable ? "user name is not available! Try a different one." :
							"pet name is not available! Try a different one.";
			return Response.status(Response.Status.CONFLICT).entity("Sorry! This " + propertyMessage).build();
		}

		try {
			String validator = UserBLL.createUserReturnUnHashedValidator(request.user);
			if (validator != null) {
				Pet pet = PetBLL.createPet(request.pet, request.user);
				ArrayList<Pet> pets = new ArrayList<Pet>();
				pets.add(pet);
				request.user.setPets(pets);
				httpRequest.getSession().setAttribute("user", request.user);
				User copiedUser = super.serializeDeepCopy(request.user, User.class);
				return Response.status(Response.Status.OK).cookie(createUserCookies(copiedUser))
							   .entity(UserBLL.wipeSensitiveFields(copiedUser)).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User creation failed for unknown reason.").build();
			}
		} catch(Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/getUserFromLogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserFromLogin(JAXBElement<User> jsonUser) {
		User user = jsonUser.getValue();
		user = UserBLL.getUser(user.getEmailAddress(), user.getPassword(), true);

		httpRequest.getSession().setAttribute("user", user);
		if(user == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email/password. Try again!").build();
		}
		User copiedUser = super.serializeDeepCopy(user, User.class);

		return Response.status(Response.Status.OK)
					   .cookie(createUserCookies(copiedUser))
					   .header("JSESSIONID", httpRequest.getSession().getId())
					   .entity(UserBLL.wipeSensitiveFields(copiedUser, true)).build();
	}

	@POST
	@Path("/getUserFromToken")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserFromToken(JAXBElement<AuthToken> jsonToken) {
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
	public Response changeUserInformation(JAXBElement<AccountInformationRequest> jsonRequest) {
		AccountInformationRequest request = jsonRequest.getValue();
		User user = request.user;
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		user.setIsActive(true);
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		try {
			user = UserBLL.updateUser(user, loggedInUser, request.imageChoice);
		} catch(InvalidPropertyException e){
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		return Response.status(Response.Status.OK).entity(UserBLL.wipeSensitiveFields(user)).build();
	}

	@DELETE
	@Path("/deleteUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUserAccount() {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");

		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			return UserBLL.deleteUser(loggedInUser) ? Response.status(Response.Status.OK).build() :
					Response.status(Response.Status.BAD_REQUEST).entity("Couldn't delete your account. You're stuck with us! Contact an admin for help.").build();
		}
	}

	@POST
	@Path("/addPet")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPetToAccount(JAXBElement<Pet> jsonPet) {
		Pet pet = jsonPet.getValue();
		boolean petAvailable = PetBLL.isPetNameValid(pet.getPetName());
		if(!petAvailable){
			return Response.status(Response.Status.CONFLICT).entity("Sorry! This pet name is not available! Try a different one.").build();
		}

		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		pet.setIsAbandoned(false);
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		pet = PetBLL.createPet(pet, loggedInUser);
		return Response.status(Response.Status.OK).entity(pet).build();
	}

	@GET
	@Path("/getUserFromID/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserFromID(@PathParam("id") int id) {
		User user = UserBLL.getUser(id);
		if(user == null){
			return Response.status(Response.Status.NOT_FOUND).entity("No such user exists").build();
		}
		return Response.status(Response.Status.OK).entity(user).build();
	}

	@POST
	@Path("/getInventory")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInventory(JAXBElement<User> jsonUser) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");

		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			return Response.status(Response.Status.OK).entity(UserBLL.getInventory(loggedInUser).toArray(new InventoryGrouping[0])).build();
		}
	}

	@POST
	@Path("/discardInventoryItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response discardInventoryItem(JAXBElement<ItemRequest> jsonRequest) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		ItemRequest request = jsonRequest.getValue();
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		} else {
			UserBLL.discardInventoryItems(request.items, loggedInUser);
			return Response.status(Response.Status.OK).build();
		}
	}
}
