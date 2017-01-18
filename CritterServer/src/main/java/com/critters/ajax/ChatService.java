package com.critters.ajax;

import com.critters.bll.ChatBLL;
import com.critters.dal.dto.Conversation;
import com.critters.dal.dto.entity.Message;
import com.critters.dal.dto.entity.User;
import com.critters.backgroundservices.BackgroundJobManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Calendar;


/**
 * Created by Jeremy on 11/29/2016.
 */
@Path("/chat")
public class ChatService extends AjaxService {

	@POST
	@Path("/sendMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessage(JAXBElement<Message> message) throws GeneralSecurityException, UnsupportedEncodingException {
		BackgroundJobManager.printLine("Enter sendmessage in chatservice " + Calendar.getInstance().getTime());
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		Message msg = message.getValue();
		msg = ChatBLL.sendMessage(msg, loggedInUser);
		return Response.status(Response.Status.OK).entity(msg).build();
	}


	@GET
	@Path("/getMessage/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessage(@PathParam("id") int id) throws GeneralSecurityException, UnsupportedEncodingException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		return Response.status(Response.Status.OK).entity(ChatBLL.getMessage(id, loggedInUser)).build();
	}

	@GET
	@Path("/getMailbox")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailbox() throws GeneralSecurityException, UnsupportedEncodingException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		return Response.status(Response.Status.OK).entity(ChatBLL.getConversations(loggedInUser.getUserID()).toArray(new Conversation[0])).build();
	}


	@GET
	@Path("/getUnreadMail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnreadMail() throws GeneralSecurityException, UnsupportedEncodingException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		return Response.status(Response.Status.OK).entity(ChatBLL.getMail(loggedInUser.getUserID(), true).toArray(new Message[0])).build();
	}
}
