package com.critters.ajax;

import com.critters.Utilities.Serializer;
import com.critters.dal.dto.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;

/**
 * Created by Jeremy on 8/9/2016.
 */
public class AjaxService {

	@Context
	HttpServletRequest httpRequest;

	protected String getHeaderSelectorValidatorString(HttpHeaders header){
		return header.getCookies().get("nicelist") != null ? header.getCookies().get("nicelist").getValue() : header.getHeaderString("SelectorValidator");
	}

	protected String[] getHeaderSelectorValidatorArray(HttpHeaders header){
		String cookie = getHeaderSelectorValidatorString(header);
		return cookie.split(":");
	}

	protected <T> T serializeDeepCopy(T object, Class objType) {
		return Serializer.fromJSON(Serializer.toJSON(false, object, objType), objType);
	}

	protected NewCookie[] createUserCookies(User user){
		NewCookie longTermCookie = new NewCookie("critters", user.getTokenSelector() + ":" + user.getTokenValidator(), "/", null, null, 60*60*24*30, false ); //sec*min*hours*days
		NewCookie sessionID = new NewCookie("JSESSIONID", httpRequest.getSession().getId(), "/api/", null, null, 60*60*3, false ); //used because setting other cookie seems to overwrite Tomcat generated cookie.
		return new NewCookie[]{longTermCookie, sessionID};
	}
}

