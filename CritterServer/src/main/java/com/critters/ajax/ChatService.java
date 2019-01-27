package com.critters.ajax;

import com.critters.ajax.filters.UserSecure;
import com.critters.bll.ChatBLL;
import com.critters.dto.Conversation;
import com.critters.dto.MessageRequest;
import com.critters.dal.entity.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import java.util.stream.Collectors;


/**
 * Created by Jeremy on 11/29/2016.
 */
@Path("/chat")
public class ChatService extends AjaxService {

	@POST
	@Path("/sendMessage")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessage(JAXBElement<Message> message) {
		Message msg = message.getValue();
		//try {
			msg = ChatBLL.sendMessage(msg, getSessionUser());
			return Response.status(Response.Status.OK).entity(msg).build();
//		} catch (GeneralSecurityException ex) {
//			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//		} catch (Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		}
	}


	@GET
	@Path("/getMessage/{id}")
	@UserSecure
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessage(@PathParam("id") int id) {

		//try {
			return Response.status(Response.Status.OK).entity(ChatBLL.getMessage(id, getSessionUser())).build();
	//	} catch (GeneralSecurityException ex) {
	//		return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
	//	} catch (Exception e) {
	//		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
	//	}
	}

	@GET
	@Path("/getMailbox")
	@UserSecure
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailbox() {
		try {
			return Response.status(Response.Status.OK).entity(ChatBLL.getConversations(getSessionUser().getUserID()).toArray(new Conversation[0])).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}


	@GET
	@Path("/getUnreadMail")
	@UserSecure
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnreadMail() {
		try {
			return Response.status(Response.Status.OK).entity(ChatBLL.getMail(getSessionUser().getUserID(), true).toArray(new Message[0])).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/deleteMessage/{id}")
	@UserSecure
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMail(@PathParam("id") int messageId) {
	//	try {
			ChatBLL.deleteMessage(messageId, getSessionUser());
			return Response.status(Response.Status.OK).build();
	//	} catch (GeneralSecurityException ex) {
	//		return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
	//	} catch (Exception e) {
	//		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
	//	}
	}

	@POST
	@Path("/markMessagesDelivered")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response markMessagesDelivered(JAXBElement<MessageRequest> message) {
		MessageRequest msg = message.getValue();
		//try {
			msg.messages = ChatBLL.markMessagesDelivered(msg.messages, getSessionUser());
			return Response.status(Response.Status.OK).entity(msg).build();
	//	} catch (GeneralSecurityException ex) {
	//		return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
	//	} catch (Exception e) {
	//		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
	//	}
	}

	@POST
	@Path("/markMessagesRead")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response markMessagesRead(JAXBElement<MessageRequest> message) {
		MessageRequest msg = message.getValue();
	//	try {
			msg.messages = ChatBLL.markMessagesRead(msg.messages, getSessionUser());
			return Response.status(Response.Status.OK).entity(msg).build();
	//	} catch (GeneralSecurityException ex) {
	//		return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
	//	} catch (Exception e) {
	//		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
	//	}
	}

	@POST
	@Path("/deleteMail")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMessages(JAXBElement<MessageRequest> message) {
		MessageRequest msg = message.getValue();
		//try {
			ChatBLL.deleteMessages(msg.messages.stream().map(Message::getMessageID).collect(Collectors.toList()), getSessionUser());
			return Response.status(Response.Status.OK).build();
//		} catch (GeneralSecurityException ex) {
//			return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//		} catch (Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		}
	}

	@POST
	@Path("/markMessagesUnread")
	@UserSecure
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response markMessagesUnread(JAXBElement<MessageRequest> message) {
		MessageRequest msg = message.getValue();
	//	try {
			msg.messages = ChatBLL.markMessagesUnread(msg.messages, getSessionUser());
			return Response.status(Response.Status.OK).entity(msg).build();
	//	} catch (GeneralSecurityException ex) {
	//		return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//		} catch (Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		///}
	}
}
