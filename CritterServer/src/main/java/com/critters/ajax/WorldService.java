package com.critters.ajax;

import com.critters.Utilities.Enums.NPCActions;
import com.critters.Utilities.Extensions;
import com.critters.ajax.filters.UserSecure;
import com.critters.bll.WorldBLL;
import com.critters.dal.entity.User;
import com.critters.dto.NPCResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Path("/world")
public class WorldService extends AjaxService {

	@Path("/actionNPC/{npcID}/{action}/{targetIdAmt}/{itemIDs}")
	@UserSecure
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response actionToNPC(@PathParam("npcID") int npcID, @PathParam("action") int actionCode, @PathParam("targetIdAmt") int target, @PathParam("itemIDs") String items) {
		User loggedInUser = getSessionUser();
		NPCActions code = NPCActions.values()[actionCode];
		int[] itemIDArr = null;
		if(!Extensions.isNullOrEmpty(items)) {
			String[] itemsStrArr = items.split(",");
			itemIDArr = new int[itemsStrArr.length];
			for(int i = 0; i < itemsStrArr.length; i++) {
				itemIDArr[i] = Integer.parseInt(itemsStrArr[i]);//may 500, but thats a bad user so meh.
			}
		}
		NPCResponse response = WorldBLL.actionNPC(npcID, code, target, loggedInUser, itemIDArr);
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

//	@Path("/Quest/{npcID}")
//	@UserSecure
//	@GET
//	public Response getQuestNPC(@PathParam("npcID") int npcID) {
//		User loggedInUser = getSessionUser();
//		NPCResponse response = WorldBLL.getQuest(npcID, loggedInUser);
//		return Response.status(200).entity(response).build();
//	}
//
//	@Path("/QuestA/{npcID}")
//	@UserSecure
//	@GET
//	public Response getAQuestNPC(@PathParam("npcID") int npcID) {
//		User loggedInUser = getSessionUser();
//		NPCResponse response = WorldBLL.advQuest(npcID, loggedInUser);
//		return Response.status(200).entity(response).build();
//	}
//
//	@Path("/QuestR/{npcID}")
//	@UserSecure
//	@GET
//	public Response getRQuestNPC(@PathParam("npcID") int npcID) {
//		User loggedInUser = getSessionUser();
//		NPCResponse response = WorldBLL.retQuest(npcID, loggedInUser);
//		return Response.status(200).entity(response).build();
//	}


}
