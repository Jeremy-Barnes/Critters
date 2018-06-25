package com.critters.bll;

import com.critters.Utilities.Enums.NPCActions;
import com.critters.Utilities.Extensions;
import com.critters.Utilities.Serializer;
import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.NPC;
import com.critters.dal.entity.QuestInstance;
import com.critters.dal.entity.User;
import com.critters.dto.NPCResponse;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy on 1/7/2018.
 */
public class WorldBLL {
	static final Logger logger = LoggerFactory.getLogger("application");


	public static NPCResponse actionNPC(int npcID, NPCActions actionCode, int targetIdOrAmt, User loggedInUser, int[] itemIDs) {
		NPCResponse response = new NPCResponse();
		NPC npc = null;
		List<QuestInstance> usersQuests = null;
		QuestInstance usersQuest = null;

		try(DAL dal = new DAL()) {
			npc = dal.npcs.getNPC(npcID);

			if (npc == null || Extensions.isNullOrEmpty(npc.getActionHandlerScript())) {
				return null;
			}

			try {
				ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
				Invocable inv = (Invocable) engine; //TODO newing this up N times is stupid
				//but making this thread static causes function name collisions. Need to centralize and cache scripted values.
				engine.eval(npc.getActionHandlerScript());
				if ((boolean) inv.invokeFunction("canAction", actionCode.ordinal())) {

					ScriptObjectMirror responseObject = (ScriptObjectMirror) inv.invokeFunction("handleAction", loggedInUser, actionCode.ordinal(), targetIdOrAmt);
					response.responseMessage = (String) responseObject.get("responseText");
					response.npc = npc;
					if (responseObject.get("subItems") != null && !Extensions.isNullOrEmpty(((ScriptObjectMirror) responseObject.get("subItems")).values())) {
						response.subItems = (NPCResponse.NPCMessageItem[]) ((ScriptObjectMirror) responseObject.get("subItems")).values().toArray(new NPCResponse.NPCMessageItem[0]);
					}


					if (actionCode == NPCActions.SubmitQuest) {
						usersQuest = dal.quests.getQuestInstance(targetIdOrAmt);
						if(usersQuest != null && usersQuest.getCompletedDate() == null && (usersQuest.getRandomQuestGiverRedeemer() == npcID ||
						(usersQuest.getCurrentStep() != null && usersQuest.getCurrentStep().getRedeemerNPC() != null &&
						usersQuest.getCurrentStep().getRedeemerNPC().getNpcID() == npcID))) {

							if(usersQuest.getRandomQuestObjectivesJSONObject() != null) {
								JSONObject o = Serializer.dictionaryFromJSON(usersQuest.getRandomQuestObjectivesJSONObject());
								JSONObject retrieveItems = (JSONObject) o.get("giveItems");
								Map<String, Object> ItemsAndQtys = retrieveItems.toMap();
								for(Map.Entry<String, Object> itemQty : ItemsAndQtys.entrySet()) {
									int itemID = Integer.parseInt(itemQty.getKey());
									int qty = Integer.parseInt(itemQty.getValue().toString());
									loggedInUser.setInventory(UserBLL.getUserInventory(loggedInUser.getUserID()));
								//	loggedInUser.getInventory().stream().filter(i -> i.getDescription().getItemConfigID())
								}
							}

						}
					}

					//append eligible new quests to the NPC response
					List<QuestInstance.StoryQuestStep> eligibleQuests = null;
					if (actionCode == NPCActions.SayHello) {
						eligibleQuests = checkForNewQuests(npc.getNpcID(), loggedInUser);
					}
					if (!Extensions.isNullOrEmpty(eligibleQuests)) {
						response.questItems = new NPCResponse.NPCQuestMessage[eligibleQuests.size()];
						for (int i = 0; i < eligibleQuests.size(); i++) {
							Object questScript = engine.eval(eligibleQuests.get(i).getActionHandlerScript());
							String tagLine = (String) inv.invokeFunction("getTagline", loggedInUser);
							String imagePath = (String) inv.invokeFunction("getImage", loggedInUser);
							int questStepID = eligibleQuests.get(i).getStoryQuestStepID();
							String questText = (String) inv.invokeFunction("getQuestDescription", loggedInUser);
							String questTitle = eligibleQuests.get(i).getQuestTitle();
							response.questItems[i] = new NPCResponse.NPCQuestMessage(tagLine, imagePath, questStepID, null);
						}
					}


				}
			} catch (Exception ex) {
				logger.error("Couldn't execute the NPC js for npc " + npcID, ex);
			}
			return response;
		}
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

	private static List<QuestInstance.StoryQuestStep> checkForNewQuests(int npcID, User loggedInUser){
		ArrayList<QuestInstance.StoryQuestStep> userQuests = new ArrayList<QuestInstance.StoryQuestStep>();
		try(DAL dal = new DAL()) {
			List<QuestInstance.StoryQuestStep> rootQuests = dal.quests.getStoryQuestStepConfigByGiverID(npcID, true);
			for(QuestInstance.StoryQuestStep quest : rootQuests){
				try {
					ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript"); //TODO newing this up N times is stupid
					//but making this thread static causes function name collisions. Need to centralize and cache scripted values.
					Invocable inv = (Invocable) engine;
					engine.eval(quest.getActionHandlerScript());
					if((boolean)inv.invokeFunction("isEligible", loggedInUser)){
						userQuests.add(quest);
					}
				} catch(Exception ex){
					logger.error("Couldn't execute the quest js for npc " + npcID + " and quest " + quest.getStoryQuestStepID(), ex);
				}
			}
		}
		return userQuests;
	}

//	public static NPCResponse getQuest(int npcID, User loggedInUser) {
//		QuestInstance.StoryQuestStep quest = null;
//		try(DAL dal = new DAL()) {
//			quest = dal.quests.getStoryQuestStepConfig(1);
//			if(quest == null) { return null; }
//			QuestInstance q = new QuestInstance(loggedInUser.getUserID(), quest, new Date(), null, null, null);
//
//			dal.beginTransaction();
//			dal.quests.save(q);
//			dal.commitTransaction();
//
//			NPCResponse ret = new NPCResponse();
//			ret.npc = quest.getGiverNPC();
//			ret.responseMessage = "U got a quest";
//			return ret;
//		}
//	}
//
//	public static NPCResponse advQuest(int npcID, User loggedInUser) {
//		List<QuestInstance.StoryQuestStep> quest = null;
//		try(DAL dal = new DAL()) {
//			QuestInstance curQ = dal.quests.getStoryQuestInstances(loggedInUser.getUserID(), npcID).get(0);
//			quest = dal.quests.getStoryQuestNextSteps(npcID, curQ.getCurrentStep().getStoryQuestStepID());
//			if(quest == null) { return null; }
//			QuestInstance q = new QuestInstance(loggedInUser.getUserID(), quest.get(0), new Date(), null, null, null);
//			dal.beginTransaction();
//			dal.quests.save(q);
//			dal.commitTransaction();
//			NPCResponse ret = new NPCResponse();
//			ret.npc = quest.get(0).getGiverNPC();
//			ret.responseMessage = "U got a quest advanced";
//			return ret;
//		}
//	}
//
//	public static NPCResponse retQuest(int npcID, User loggedInUser) {
//		List<QuestInstance> quest = null;
//		try(DAL dal = new DAL()) {
//			quest = dal.quests.getStoryQuestInstances(loggedInUser.getUserID(), npcID);
//			if(quest == null) { return null; }
//			NPCResponse ret = new NPCResponse();
//			ret.npc = quest.get(0).getCurrentStep().getGiverNPC();
//			ret.responseMessage = "U got a quest or 2";
//			return ret;
//		}
//	}

}
