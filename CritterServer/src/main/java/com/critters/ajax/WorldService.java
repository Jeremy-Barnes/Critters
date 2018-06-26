package com.critters.ajax;

import com.critters.Utilities.Enums.NPCActions;
import com.critters.ajax.filters.UserSecure;
import com.critters.bll.WorldBLL;
import com.critters.dal.entity.User;
import com.critters.dto.NPCRequest;
import com.critters.dto.NPCResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.util.ArrayList;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Path("/world")
public class WorldService extends AjaxService {

	@Path("/actionNPC/{npcID}/{action}/{targetIdAmt}/")
	@UserSecure
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response actionToNPC(@PathParam("npcID") int npcID, @PathParam("action") int actionCode, @PathParam("targetIdAmt") int target, JAXBElement<NPCRequest> items) {
		User loggedInUser = getSessionUser();
		NPCActions code = NPCActions.values()[actionCode];
		int[] itemIDArr = items.getValue().itemIDs;
		NPCResponse response = WorldBLL.actionNPC(npcID, code, target, loggedInUser, new ArrayList<Integer>() {{ for (int i : itemIDArr) add(i); }});
		return Response.status(200).entity(response).build();
	}

	@Path("/NPC/{npcID}")
	@UserSecure
	@GET
	public Response getNPC(@PathParam("npcID") int npcID) {
		User loggedInUser = getSessionUser();
		NPCResponse response = WorldBLL.getNPC(npcID, loggedInUser);
		return Response.status(200).entity(response).build();
	}
}
