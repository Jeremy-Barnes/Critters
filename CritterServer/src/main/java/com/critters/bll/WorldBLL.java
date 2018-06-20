package com.critters.bll;

import com.critters.Utilities.Enums.NPCActions;
import com.critters.Utilities.Extensions;
import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.NPC;
import com.critters.dal.entity.QuestInstance;
import com.critters.dal.entity.User;
import com.critters.dto.NPCResponse;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Date;
import java.util.List;

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

	public static NPCResponse getQuest(int npcID, User loggedInUser) {
		QuestInstance.StoryQuestStep quest = null;
		try(DAL dal = new DAL()) {
			quest = dal.quests.getStoryQuestStepConfig(1);
			if(quest == null) { return null; }
			QuestInstance q = new QuestInstance(loggedInUser.getUserID(), quest, new Date(), null, null, null);

			dal.beginTransaction();
			dal.quests.save(q);
			dal.commitTransaction();

			NPCResponse ret = new NPCResponse();
			ret.npc = quest.getGiverNPC();
			ret.responseMessage = "U got a quest";
			return ret;
		}
	}

	public static NPCResponse advQuest(int npcID, User loggedInUser) {
		List<QuestInstance.StoryQuestStep> quest = null;
		try(DAL dal = new DAL()) {
			QuestInstance curQ = dal.quests.getStoryQuestInstances(loggedInUser.getUserID(), npcID).get(0);
			quest = dal.quests.getStoryQuestNextSteps(npcID, curQ.getCurrentStep().getStoryQuestStepID());
			if(quest == null) { return null; }
			QuestInstance q = new QuestInstance(loggedInUser.getUserID(), quest.get(0), new Date(), null, null, null);
			dal.beginTransaction();
			dal.quests.save(q);
			dal.commitTransaction();
			NPCResponse ret = new NPCResponse();
			ret.npc = quest.get(0).getGiverNPC();
			ret.responseMessage = "U got a quest advanced";
			return ret;
		}
	}

	public static NPCResponse retQuest(int npcID, User loggedInUser) {
		List<QuestInstance> quest = null;
		try(DAL dal = new DAL()) {
			quest = dal.quests.getStoryQuestInstances(loggedInUser.getUserID(), npcID);
			if(quest == null) { return null; }
			NPCResponse ret = new NPCResponse();
			ret.npc = quest.get(0).getCurrentStep().getGiverNPC();
			ret.responseMessage = "U got a quest or 2";
			return ret;
		}
	}

}
