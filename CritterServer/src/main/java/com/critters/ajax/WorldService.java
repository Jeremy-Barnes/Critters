package com.critters.ajax;

import com.critters.Utilities.Enums.NPCActions;
import com.critters.ajax.filters.UserSecure;
import com.critters.bll.WorldBLL;
import com.critters.dto.NPCResponse;
import com.critters.dal.entity.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Path("/world")
public class WorldService extends AjaxService {

	@Path("/actionNPC/{npcID}/{action}/{targetIdAmt}")
	@UserSecure
	@GET
	public Response actionToNPC(@PathParam("npcID") int npcID, @PathParam("action") NPCActions actionCode, @PathParam("targetIdAmt") int target) {
		User loggedInUser = getSessionUser();
		NPCResponse response = WorldBLL.actionNPC(npcID, actionCode, target, loggedInUser);
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



	@Path("/Quest/{npcID}")
	@UserSecure
	@GET
	public Response getQuestNPC(@PathParam("npcID") int npcID) {
		User loggedInUser = getSessionUser();
		NPCResponse response = WorldBLL.getQuest(npcID, loggedInUser);
		return Response.status(200).entity(response).build();
	}

	@Path("/QuestA/{npcID}")
	@UserSecure
	@GET
	public Response getAQuestNPC(@PathParam("npcID") int npcID) {
		User loggedInUser = getSessionUser();
		NPCResponse response = WorldBLL.advQuest(npcID, loggedInUser);
		return Response.status(200).entity(response).build();
	}

	@Path("/QuestR/{npcID}")
	@UserSecure
	@GET
	public Response getRQuestNPC(@PathParam("npcID") int npcID) {
		User loggedInUser = getSessionUser();
		NPCResponse response = WorldBLL.retQuest(npcID, loggedInUser);
		return Response.status(200).entity(response).build();
	}


}
