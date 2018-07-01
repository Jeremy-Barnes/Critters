package com.critters.bll;

import com.critters.Utilities.Enums.NPCActions;
import com.critters.Utilities.Extensions;
import com.critters.Utilities.Serializer;
import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.Item;
import com.critters.dal.entity.NPC;
import com.critters.dal.entity.QuestInstance;
import com.critters.dal.entity.User;
import com.critters.dto.NPCResponse;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Jeremy on 1/7/2018.
 */
public class WorldBLL {
	static final Logger logger = LoggerFactory.getLogger("application");


	public static NPCResponse actionNPC(int npcID, NPCActions actionCode, int targetIdOrAmt, User loggedInUser, List<Integer> itemIDs) {
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
						response = handleSubmitQuest(dal, engine, inv, npcID, targetIdOrAmt, loggedInUser, itemIDs, response);
					} else if(actionCode == NPCActions.SayHello) {
						response = handleHello(dal, engine, inv, npc, loggedInUser, response);
					}
				}//can do action
			} catch (Exception ex) {
				logger.error("Couldn't execute the NPC js for npc " + npcID, ex);
			}
			return response;
		}
	}

	private static NPCResponse handleHello(DAL dal, ScriptEngine engine, Invocable inv, NPC npc, User loggedInUser,NPCResponse response) throws ScriptException, NoSuchMethodException {
		List<QuestInstance.StoryQuestStep> eligibleQuests = null;
		eligibleQuests = checkForNewQuests(dal, npc.getNpcID(), loggedInUser);
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
		return response;
	}


	private static NPCResponse handleSubmitQuest(DAL dal, ScriptEngine engine, Invocable inv,
												 int activeNPCID, int targetIdOrAmt, User loggedInUser, List<Integer> itemIDs, NPCResponse npcResponse)
			throws ScriptException, NoSuchMethodException {
		QuestInstance usersQuest = dal.quests.getQuestInstance(targetIdOrAmt);
		if(usersQuest != null && usersQuest.getCompletedDate() == null) {
			if(usersQuest.getCurrentStep() != null) {
				handleSubmitStoryQuest(usersQuest, dal, engine, inv, activeNPCID, targetIdOrAmt, loggedInUser, itemIDs);
			} else if(usersQuest.getRandomQuestObjectivesJSONObject() != null) {
				npcResponse.questItems = new NPCResponse.NPCQuestMessage[1];
				npcResponse.questItems[0] = handleSubmitRandomQuest(usersQuest, dal, activeNPCID, targetIdOrAmt, loggedInUser, itemIDs);
			}
		} else {

		}
		return npcResponse;
	}

	private static NPCResponse.NPCQuestMessage handleSubmitRandomQuest(QuestInstance usersQuest, DAL dal, int activeNPCID, int targetIdOrAmt, User loggedInUser, List<Integer> itemIDs){
		NPCResponse.NPCQuestMessage response = new NPCResponse.NPCQuestMessage();

		JSONObject jsonObj = Serializer.dictionaryFromJSON(usersQuest.getRandomQuestObjectivesJSONObject());
		JSONObject retrieveItems = (JSONObject) jsonObj.get("giveItems");//TODO formalize all this as a class

		if(retrieveItems != null) {
			List<Item> outParamFilteredItems = new ArrayList<Item>();
			Random rand = new Random();

			if(sufficientGifts(loggedInUser, itemIDs, retrieveItems, outParamFilteredItems)) {
				if(UserBLL.discardInventoryItems(outParamFilteredItems.toArray(new Item[0]), loggedInUser, null)) {
					dal.beginTransaction();
					usersQuest.setCompletedDate(new Date());
					dal.quests.save(usersQuest);
					dal.commitTransaction();
					JSONArray successResponses = jsonObj.getJSONArray("successResponses");
					response.messageText = successResponses.get(rand.nextInt(successResponses.length())).toString();
				}
			} else {
				JSONArray failureResponses = jsonObj.getJSONArray("incompleteFailureResponses");
				response.messageText = failureResponses.get(rand.nextInt(failureResponses.length())).toString();

			}
		}
		return response;
	}

	private static void handleSubmitStoryQuest(QuestInstance usersQuest, DAL dal, ScriptEngine engine, Invocable inv, int activeNPCID,
											   int targetIdOrAmt, User loggedInUser, List<Integer> itemIDs) throws ScriptException, NoSuchMethodException {

		if(usersQuest.getCurrentStep().getRedeemerNPC() != null && usersQuest.getCurrentStep().getRedeemerNPC().getNpcID() == activeNPCID){
			Object questScript = engine.eval(usersQuest.getCurrentStep().getActionHandlerScript());
			String tagLine = (String) inv.invokeFunction("getTagline", loggedInUser);

		} else {

		}
	}


	private static boolean sufficientGifts(User loggedInUser, List<Integer> itemIDs, JSONObject npcWantItems, List<Item> outParamFilteredItems) {
		boolean insufficientGifts = true;
		if(!Extensions.isNullOrEmpty(itemIDs)) {
			loggedInUser.setInventory(UserBLL.getUserInventory(loggedInUser.getUserID()));
			outParamFilteredItems.addAll(loggedInUser.getInventory().stream().filter(i -> itemIDs.contains(i.getInventoryItemId())).collect(Collectors.toList()));
			Map<String, Object> ItemsAndQtys = npcWantItems.toMap();
			for (Map.Entry<String, Object> itemQty : ItemsAndQtys.entrySet()) {
				int itemConfigID = Integer.parseInt(itemQty.getKey());
				int qty = Integer.parseInt(itemQty.getValue().toString());
				if (outParamFilteredItems.stream().filter(i -> i.getDescription().getItemConfigID() == itemConfigID).count() >= qty) {
					insufficientGifts = false;
				} else {
					insufficientGifts = true;
					break;
				}
			}
		}

		return !insufficientGifts;
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

	private static List<QuestInstance.StoryQuestStep> checkForNewQuests(DAL dal, int npcID, User loggedInUser){
		ArrayList<QuestInstance.StoryQuestStep> userQuests = new ArrayList<QuestInstance.StoryQuestStep>();
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
