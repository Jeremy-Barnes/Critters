package com.critters.bll;

import com.critters.Utilities.Enums.NPCActions;
import com.critters.Utilities.Extensions;
import com.critters.dal.accessors.DAL;
import com.critters.dto.NPCResponse;
import com.critters.dal.entity.NPC;
import com.critters.dal.entity.User;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by Jeremy on 1/7/2018.
 */
public class WorldBLL {
	static final Logger logger = LoggerFactory.getLogger("application");

	public static NPCResponse actionNPC(int npcID, NPCActions actionCode, int targetIdOrAmt, User loggedInUser) {
		NPCResponse response = new NPCResponse();
		ScriptEngineManager factory = new ScriptEngineManager();
		NPC npc = null;
		try(DAL dal = new DAL()) {
			npc = dal.npcs.getNPC(npcID);
		}
		if(npc == null || Extensions.isNullOrEmpty(npc.getActionHandlerScript())) {
			return null;
		}

		if(actionCode == NPCActions.SayHello) {
			checkForQuests();
		}

		try {
			ScriptEngine engine = factory.getEngineByName("JavaScript");
			engine.eval(npc.getActionHandlerScript());
			Invocable inv = (Invocable) engine;
			if((boolean)inv.invokeFunction("canAction", actionCode)){
				ScriptObjectMirror o =(ScriptObjectMirror) inv.invokeFunction("handleAction", loggedInUser, actionCode.ordinal(), targetIdOrAmt);
				response.responseMessage = (String)o.get("responseText");
				response.npc = npc;
				if(o.get("subItems") != null && !Extensions.isNullOrEmpty(((ScriptObjectMirror)o.get("subItems")).values())) {
					response.subItems = (NPCResponse.NPCMessageItem[]) ((ScriptObjectMirror) o.get("subItems")).values().toArray(new NPCResponse.NPCMessageItem[0]);
				}
			}
		} catch(Exception ex){
			logger.error("Couldn't execute the NPC js for npc " + npcID, ex);
		}
		return response;
	}

	public static NPCResponse getNPC(int npcID, User loggedInUser) {
		NPCResponse response = new NPCResponse();
		NPC npc = null;
		try(DAL dal = new DAL()) {
			npc = dal.npcs.getNPC(npcID);
		}
		if(npc == null || Extensions.isNullOrEmpty(npc.getActionHandlerScript())) {
			return null;
		}
		response.npc = npc;

		return response;
	}

	public static void checkForQuests(){

	}

}
