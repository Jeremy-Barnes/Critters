package com.critters.ajax;

import com.critters.bll.UserBLL;
import com.critters.dal.dto.AuthToken;
import com.critters.dal.dto.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jeremy on 8/7/2016.
 */
@Path("/users")
public class UserService extends AjaxService{
	private static Map<Integer,AsyncResponse> peers = Collections.synchronizedMap(new HashMap<Integer, AsyncResponse>());

	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(JAXBElement<User> jsonUser) throws Exception {
		User user = jsonUser.getValue();
		String validator = UserBLL.createUserReturnUnHashedValidator(user);

		httpRequest.getSession().setAttribute("user", user);
		User copiedUser = super.serializeDeepCopy(user, User.class);
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

		return Response.status(Response.Status.OK).cookie(createUserCookies(copiedUser))
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


		//throw new JAXBException("Not implemented yet"); //TODO this
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
		user = UserBLL.updateUser(user, loggedInUser);
		return Response.status(Response.Status.OK).entity(UserBLL.wipeSensitiveFields(user)).build();

		//throw new IOException("Not implemented yet"); //TODO this
	}

	@Path("/poll")
	@POST
	public void poll(@Suspended final AsyncResponse asyncResponse) throws InterruptedException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");

		if(loggedInUser != null) {
			asyncResponse.setTimeout(10, TimeUnit.SECONDS);
			peers.put(1, asyncResponse);
		}

	}
}
