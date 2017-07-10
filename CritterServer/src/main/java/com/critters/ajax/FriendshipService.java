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
	public Response createFriendship(JAXBElement<Friendship> jsonRequest) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		Friendship request = jsonRequest.getValue();
		try {
			request = FriendshipBLL.createFriendship(request.getRequester(), request.getRequested(), loggedInUser);
			return Response.status(Response.Status.OK).entity(request).build();
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path("/respondToFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFriendship(JAXBElement<Friendship> request) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		Friendship req = request.getValue();
		try {
			if (req.isAccepted()) {
				req = FriendshipBLL.acceptFriendRequest(req, loggedInUser);
				return Response.status(Response.Status.OK).entity(req).build();
			} else {
				FriendshipBLL.deleteFriendRequest(req, loggedInUser);
				return Response.status(Response.Status.OK).build();
			}
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/cancelFriendRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelFriendRequest(JAXBElement<Friendship> request) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		Friendship req = request.getValue();
		try {
			FriendshipBLL.cancelFriendRequest(req, loggedInUser);
			return Response.status(Response.Status.OK).build();
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}  catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/deleteFriendship")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response endFriendship(JAXBElement<Friendship> request) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		Friendship req = request.getValue();
		try {
			FriendshipBLL.endFriendship(req, loggedInUser);
			return Response.status(Response.Status.OK).build();
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		}  catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}