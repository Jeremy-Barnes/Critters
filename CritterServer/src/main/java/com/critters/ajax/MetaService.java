package com.critters.ajax;

import com.critters.bll.UserBLL;
import com.critters.dal.dto.SearchResponse;
import com.critters.dal.dto.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jeremy on 8/22/2016.
 */
@Path("/meta")
public class MetaService extends AjaxService {
	private static Map<Integer,AsyncResponse> peers = Collections.synchronizedMap(new HashMap<Integer, AsyncResponse>());

	@Path("/poll")
	@POST
	public void poll(@Suspended final AsyncResponse asyncResponse) throws InterruptedException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");

		if(loggedInUser != null) {
			asyncResponse.setTimeout(10, TimeUnit.SECONDS);
			peers.put(1, asyncResponse);
		}
	}

	@GET
	@Path("/search/{searchStr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchData(@PathParam("searchStr") String searchStr) throws JAXBException, GeneralSecurityException, IOException {
		SearchResponse results = new SearchResponse();
		results.users = UserBLL.searchUsers(searchStr).toArray(new User[0]);
		//TODO results.items = ItemsBLL.searchItems(searchStr).toArray(new Item[0]);
		return Response.status(200).entity(results).build();
	}
}
