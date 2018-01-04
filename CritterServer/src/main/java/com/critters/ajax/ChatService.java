package com.critters.ajax;

import com.critters.bll.ChatBLL;
import com.critters.dal.dto.Conversation;
import com.critters.dal.dto.MessageRequest;
import com.critters.dal.dto.entity.Message;
import com.critters.dal.dto.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.security.GeneralSecurityException;
import java.util.stream.Collectors;


/**
 * Created by Jeremy on 11/29/2016.
 */
@Path("/chat")
public class ChatService extends AjaxService {

	@POST
	@Path("/sendMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessage(JAXBElement<Message> message) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		Message msg = message.getValue();
		//try {
			msg = ChatBLL.sendMessage(msg, loggedInUser);
			return Response.status(Response.Status.OK).entity(msg).build();
//		} catch (GeneralSecurityException ex) {
//			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//		} catch (Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		}
	}


	@GET
	@Path("/getMessage/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessage(@PathParam("id") int id) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}
		try {
			return Response.status(Response.Status.OK).entity(ChatBLL.getMessage(id, loggedInUser)).build();
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/getMailbox")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailbox() {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		try {
			return Response.status(Response.Status.OK).entity(ChatBLL.getConversations(loggedInUser.getUserID()).toArray(new Conversation[0])).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}


	@GET
	@Path("/getUnreadMail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnreadMail() {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		try {
			return Response.status(Response.Status.OK).entity(ChatBLL.getMail(loggedInUser.getUserID(), true).toArray(new Message[0])).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/deleteMessage/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMail(@PathParam("id") int messageId) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		try {
			ChatBLL.deleteMessage(messageId, loggedInUser);
			return Response.status(Response.Status.OK).build();
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/markMessagesDelivered")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response markMessagesDelivered(JAXBElement<MessageRequest> message) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		MessageRequest msg = message.getValue();
		try {
			msg.messages = ChatBLL.markMessagesDelivered(msg.messages, loggedInUser);
			return Response.status(Response.Status.OK).entity(msg).build();
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/markMessagesRead")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response markMessagesRead(JAXBElement<MessageRequest> message) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		MessageRequest msg = message.getValue();
		try {
			msg.messages = ChatBLL.markMessagesRead(msg.messages, loggedInUser);
			return Response.status(Response.Status.OK).entity(msg).build();
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/deleteMail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMessages(JAXBElement<MessageRequest> message) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		MessageRequest msg = message.getValue();
		//try {
			ChatBLL.deleteMessages(msg.messages.stream().map(Message::getMessageID).collect(Collectors.toList()), loggedInUser);
			return Response.status(Response.Status.OK).build();
//		} catch (GeneralSecurityException ex) {
//			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//		} catch (Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		}
	}

	@POST
	@Path("/markMessagesUnread")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response markMessagesUnread(JAXBElement<MessageRequest> message) {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build();
		}

		MessageRequest msg = message.getValue();
		try {
			msg.messages = ChatBLL.markMessagesUnread(msg.messages, loggedInUser);
			return Response.status(Response.Status.OK).entity(msg).build();
		} catch (GeneralSecurityException ex) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
