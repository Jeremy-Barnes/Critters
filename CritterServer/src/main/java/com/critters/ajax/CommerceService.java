package com.critters.ajax;

import com.critters.dal.dto.entity.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

}
