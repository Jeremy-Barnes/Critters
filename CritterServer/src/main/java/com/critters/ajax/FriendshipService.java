package com.critters.ajax;

import com.critters.bll.FriendshipBLL;
import com.critters.dal.dto.entity.Friendship;
import com.critters.dal.dto.entity.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Created by Jeremy on 8/28/2016.
 */
@Path("/friends")
public class FriendshipService extends AjaxService {

	@POST
	@Path("/createFriendship")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFriendship(JAXBElement<Friendship> jsonRequest) throws GeneralSecurityException, UnsupportedEncodingException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		Friendship request = jsonRequest.getValue();
		FriendshipBLL.createFriendship(request.getRequester(), request.getRequested(), loggedInUser);

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/respondToFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFriendship(JAXBElement<Friendship> request) throws GeneralSecurityException, UnsupportedEncodingException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		Friendship req = request.getValue();
		if(req.isAccepted()){
			FriendshipBLL.acceptFriendRequest(req, loggedInUser);
		} else {
			FriendshipBLL.deleteFriendRequest(req, loggedInUser);
		}

		return Response.status(Response.Status.OK).build();
	}
}