package com.critters.ajax;

import com.critters.dal.dto.AuthToken;
import com.critters.dal.dto.User;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Created by Jeremy on 8/7/2016.
 */
@Path("/users")
public class UserService extends AjaxService{
	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(JAXBElement<User> jsonUser) throws Exception {
		User user = jsonUser.getValue();
		throw new Exception("Not implemented yet"); //TODO this
	}

	@POST
	@Path("/getUserFromLogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserFromLogin(JAXBElement<User> jsonUser) throws Exception {
		User user = jsonUser.getValue();
		throw new Exception("Not implemented yet"); //TODO this
	}

	@POST
	@Path("/getUserFromToken")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserFromToken(JAXBElement<AuthToken> jsonToken) throws GeneralSecurityException, UnsupportedEncodingException, JAXBException {
		AuthToken token = jsonToken.getValue();
		throw new JAXBException("Not implemented yet"); //TODO this
	}

	@POST
	@Path("/changeUserInformation")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeUserInformation(FormDataMultiPart form, @Context HttpHeaders header) throws JAXBException, GeneralSecurityException, IOException {
		User user = (User) httpRequest.getSession().getAttribute("user");
		throw new IOException("Not implemented yet"); //TODO this
	}
}
