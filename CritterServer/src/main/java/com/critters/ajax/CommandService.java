package com.critters.ajax;

import com.critters.ajax.filters.UserSecure;
import com.critters.bll.ChatBLL;
import com.critters.bll.EventBLL;
import com.critters.dal.entity.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Path("/command")
public class CommandService extends AjaxService {

	@Path("/pollForNotifications")
	@UserSecure
	@GET
	public void pollForNotification(@Suspended final AsyncResponse asyncResponse) throws InterruptedException {
		User loggedInUser = getSessionUser();
		EventBLL.registerForRandomEventLottery(loggedInUser);
		EventBLL.tellMeWhatIWon(loggedInUser, asyncResponse);
		ChatBLL.createPoll(loggedInUser.getUserID(), asyncResponse);
	}

	@GET
	@Path("/respondToServerQuery/{queryID}/{response}}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchData(@PathParam("queryID") String queryID, @PathParam("response") String response) {
		//TODO results.items = ItemsBLL.searchItems(searchStr).toArray(new Item[0]);
		return Response.status(200).build();
	}
}
