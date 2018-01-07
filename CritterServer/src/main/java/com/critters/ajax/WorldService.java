package com.critters.ajax;

import com.critters.ajax.filters.UserSecure;
import com.critters.bll.WorldBLL;
import com.critters.dal.dto.NPCResponse;
import com.critters.dal.dto.entity.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Path("/world")
public class WorldService extends AjaxService {

	@Path("/NPC/{npcID}/{action}/{targetIdAmt}")
	@UserSecure
	@GET
	public Response giveToWishingWell(@PathParam("npcID") int npcID, @PathParam("action") int actionCode, @PathParam("targetIdAmt") int target) throws InterruptedException {
		User loggedInUser = getSessionUser();
		NPCResponse response = WorldBLL.actionNPC(npcID, actionCode, target, loggedInUser);
		return Response.status(200).entity(response).build();
	}
}
