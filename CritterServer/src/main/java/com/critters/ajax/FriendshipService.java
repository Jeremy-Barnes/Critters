package com.critters.ajax;

import com.critters.ajax.filters.UserSecure;
import com.critters.bll.FriendshipBLL;
import com.critters.dal.dto.entity.Friendship;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

/**
 * Created by Jeremy on 8/28/2016.
 */
@Path("/friends")
public class FriendshipService extends AjaxService {

	@POST
	@Path("/createFriendship")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createFriendship(JAXBElement<Friendship> jsonRequest) {
		Friendship request = jsonRequest.getValue();
	//	try {
			request = FriendshipBLL.createFriendship(request.getRequester(), request.getRequested(), getSessionUser());
			return Response.status(Response.Status.OK).entity(request).build();
//		} catch (GeneralSecurityException ex) {
//			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//		} catch (Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		}

	}

	@POST
	@Path("/respondToFriendRequest")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFriendship(JAXBElement<Friendship> request) {
		Friendship req = request.getValue();
		//try {
			if (req.isAccepted()) {
				req = FriendshipBLL.acceptFriendRequest(req, getSessionUser());
				return Response.status(Response.Status.OK).entity(req).build();
			} else {
				FriendshipBLL.deleteFriendRequest(req, getSessionUser());
				return Response.status(Response.Status.OK).build();
			}
		//} catch (GeneralSecurityException ex) {
		//	return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		//} catch (Exception e) {
	//		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
	//	}
	}

	@POST
	@Path("/cancelFriendRequest")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelFriendRequest(JAXBElement<Friendship> request) {
		Friendship req = request.getValue();
		//try {
			FriendshipBLL.cancelFriendRequest(req, getSessionUser());
			return Response.status(Response.Status.OK).build();
		//} catch (GeneralSecurityException ex) {
	//		return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
	//	}  catch (Exception e) {
	//		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
	//	}
	}

	@POST
	@Path("/deleteFriendship")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response endFriendship(JAXBElement<Friendship> request) {
		Friendship req = request.getValue();
		//try {
			FriendshipBLL.endFriendship(req, getSessionUser());
			return Response.status(Response.Status.OK).build();
		//} catch (GeneralSecurityException ex) {
		//	return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		//}  catch (Exception e) {
		////	return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		//}
	}
}