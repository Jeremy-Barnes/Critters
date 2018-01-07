package com.critters.ajax.filters;

import com.critters.dal.dto.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by Jeremy on 8/7/2016.
 */
@Provider
@UserSecure
public class UserSecurityFilter implements ContainerRequestFilter {

	@Context
	HttpServletRequest httpRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		User loggedInUser = (User) httpRequest.getSession().getAttribute("user");
		if(loggedInUser == null) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You need to log in first!").build());
		}
	}
}
