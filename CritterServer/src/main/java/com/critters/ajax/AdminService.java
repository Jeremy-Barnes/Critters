package com.critters.ajax;

import com.critters.ajax.filters.UserSecure;
import com.critters.bll.WorldBLL;
import com.critters.dal.entity.QuestInstance;
import com.critters.dal.entity.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Jeremy on 1/26/2019.
 */
@Path("admin")
public class AdminService extends AjaxService {
	@Path("/testAPI")
	@GET
	@UserSecure
	@Produces(MediaType.APPLICATION_JSON)
	public Response testAPI(){
		User loggedInUser = getSessionUser();
		QuestInstance o = WorldBLL.test(loggedInUser);

		return Response.status(200).entity(o).build();
	}

	@Path("/healthcheck")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response healthCheck(){
		return Response.status(200).entity(WorldBLL.getNPC(1, null)).build();
	}




}
