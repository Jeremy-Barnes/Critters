package com.critters.ajax;

import com.critters.dal.dto.AuthToken;
import com.critters.dal.dto.entity.User;
import com.critters.games.sockets.SocketManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;

/**
 * Created by Jeremy on 7/16/2017.
 */
@Path("/games")
public class GameService extends AjaxService {

	@GET
	@Path("/openGameServer/{gameType}/{clientID}/{gameName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response openGameServer(@PathParam("gameType") int gameType, @PathParam("clientID") String clientID, @PathParam("gameName") String gameName) {
		String gameID = SocketManager.createNewGame(gameType, clientID, gameName);
		return Response.status(200).entity(gameID).build();
	}

	@GET
	@Path("/connectToGameServer/{gameID}/{clientID}")
	@Produces(MediaType.APPLICATION_JSON)
	public void connectToGameServer(@PathParam("gameID") String gameID, @PathParam("clientID") String clientID, @Suspended final AsyncResponse asyncResponse) {
		SocketManager.tryToConnect(gameID, clientID, asyncResponse);
	}

	@GET
	@Path("/getAllActiveGames/{gameType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllActiveGames(@PathParam("gameType") int gameType) {
		return Response.status(200).entity(SocketManager.findAllGames(gameType)).build();
	}

	@GET
	@Path("/findUserNameGames/{gameType}/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserNameGames(@PathParam("gameType") int gameType, @PathParam("userName") String userName) {
		return Response.status(200).entity(SocketManager.findGames(gameType, userName)).build();
	}

	@GET
	@Path("/getUsersGames/{gameType}/{userID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersGames(@PathParam("gameType") int gameType, @PathParam("userID") int userID) {
		return Response.status(200).entity(SocketManager.findGames(gameType, userID)).build();
	}

	@GET
	@Path("/getSecureID")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSecureID(){
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		Random rand = new Random();
		String secureID = Math.random()* rand.nextInt() + " " + System.currentTimeMillis();
		SocketManager.setUserID(secureID, loggedInUser);
		AuthToken auth = new AuthToken();
		auth.selector = secureID;
		return Response.status(Response.Status.OK).entity(auth).build();
	}
}
