package com.critters.bll;

import com.critters.Utilities.Enums.NPCActions;
import com.critters.Utilities.Extensions;
import com.critters.Utilities.Serializer;
import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.*;
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
		eligibleQuests = checkForNewStoryQuests(dal, npc.getNpcID(), loggedInUser);
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

	private static NPCResponse handleSubmitQuest(DAL dal, ScriptEngine engine, Invocable inv, int activeNPCID, int targetIdOrAmt, User loggedInUser,
												 List<Integer> itemIDs, NPCResponse npcResponse) throws ScriptException, NoSuchMethodException {
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
				if(UserBLL.giveOrDiscardInventoryItems(outParamFilteredItems.toArray(new Item[0]), loggedInUser, null)) {
					JSONObject rewards =  (JSONObject) jsonObj.get("successRewards");
					Map<String, Object> rewardsDictionary = rewards.toMap();
					Map<Integer, Integer> itemCreateDictionary = new HashMap<>();
					for (Map.Entry<String, Object> itemQty : rewardsDictionary.entrySet()) {
						int qty = Integer.parseInt(itemQty.getValue().toString());
						if(itemQty.getKey().equalsIgnoreCase("cash")) {
							UserBLL.alterUserCash(loggedInUser.getUserID(), qty);
						} else {
							int itemConfigID = Integer.parseInt(itemQty.getKey());
							itemCreateDictionary.put(itemConfigID, qty);
						}
					}

					List<Item> createdItems = ItemsBLL.createNewItems(itemCreateDictionary, loggedInUser.getUserID());
					dal.beginTransaction();
					usersQuest.setCompletedDate(new Date());
					dal.quests.save(usersQuest);
					dal.commitTransaction();

					JSONObject successResponse = jsonObj.getJSONObject("successResponse");
					response.messageText = successResponse.get("messageText").toString();
					//todo response.image
					response.subItems = new NPCResponse.NPCQuestMessage[itemCreateDictionary.size()];
					int n = 0;
					for(Map.Entry<Integer, Integer> itemCfgAndQty : itemCreateDictionary.entrySet()) {
						int cfgId = itemCfgAndQty.getKey();
						int qty = itemCfgAndQty.getValue();

						Item item = createdItems.stream().filter(i -> i.getDescription().getItemConfigID() == cfgId).collect(Collectors.toList()).get(0);
						String message = "You received " + qty + " " + item.getDescription().getItemName() + (qty > 1 ? "s!" : "!");
						response.subItems[n] = new NPCResponse.NPCQuestMessage(message, item.getDescription().getImagePath(), null, null, null);
						n++;
					}
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

	private static List<QuestInstance.StoryQuestStep> checkForNewStoryQuests(DAL dal, int npcID, User loggedInUser){
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

	public static QuestInstance test(User user){
		NPC npc;
		try(DAL dal = new DAL()){
			npc = dal.npcs.getNPC(1);
		}
		return generateARandomQuest(npc, user.getUserID(), null, 400);
	}

	private static QuestInstance generateARandomQuest(NPC npc, int userID, Integer maxItemRarityType,  Integer maxCashReward) {
/*
 {
      "giveItems" : {
          "1" : 1,
          "3" : 3
      },
      "successRewards" : {
           "cash" : "534",
           "47" : "2"
      }
      "successResponse" : {
           "messageText" : "Hey thanks!"
      },
      "incompleteFailureResponses" : [
           "Not enough stuff!",
           "Thanks for nothing, jerk!"
      ],
 }
 */
		QuestInstance quest = null;
		try(DAL dal = new DAL()) {
			JSONObject questObject = new JSONObject();
			List<NPCItemQuestPreferenceConfig> itemWantCfgs = dal.quests.getNPCItemQuestPreferenceConfigs();
			List<NPCQuestResponseConfig> responsecfgs = dal.quests.getNPCQuestResponseConfigs();

			itemWantCfgs = itemWantCfgs.stream().filter(c -> c.getWanterNPC() == npc.getNpcID()).collect(Collectors.toList());
			responsecfgs = responsecfgs.stream().filter(c -> c.getRespondingNPCID() == npc.getNpcID()).collect(Collectors.toList());
			Collections.shuffle(itemWantCfgs);
			Collections.shuffle(responsecfgs);


			int cashReward = 0;
			int iterations = 0;
			List<Item.ItemDescription> wantItems = new ArrayList<Item.ItemDescription>();
			while(maxCashReward != null && cashReward < maxCashReward && iterations < 10) {
				for (NPCItemQuestPreferenceConfig cfg : itemWantCfgs) {
					int reward = cfg.getCritterBuxxValuePerItem();
					Item.ItemDescription itemtype = cfg.getItemConfig();
					Item.ItemRarityType rarity = itemtype.getRarity();
					if ( (maxCashReward < (cashReward + reward) || ((1.0 * (cashReward + reward)) / maxCashReward < 1.25)) &&
							(maxItemRarityType == null || rarity.getItemRarityTypeID() <= maxItemRarityType)) {

						if (Extensions.flipACoin(50)) {
							cashReward += reward;
							wantItems.add(cfg.getItemConfig());
						}
						if (maxCashReward <= cashReward) break;
					}
				}
				iterations++;
			}
			Map<Item.ItemDescription, Long> wantCfgs = wantItems.stream().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
			//s
			// wantItems.stream().distinct().collect(Collectors.toList());
			Map<Integer, Long> groupByCfgIDAndCounted = wantItems.stream().collect(Collectors.groupingBy(i -> i.getItemConfigID(), Collectors.counting()));

			if(groupByCfgIDAndCounted.size() > 0) {
				JSONObject giveItems = new JSONObject(groupByCfgIDAndCounted);
				questObject.accumulate("giveItems", giveItems);
			}
			//todo create reward objects, not just cash
			JSONObject successRewards = new JSONObject();
			successRewards.accumulate("cash", cashReward);

			JSONObject successResponse = new JSONObject();
			boolean successDone = false;
			int failureCollected = 0;
			String[] failureResponses = new String[4];
			//this logic left overly expanded to add other properties (image?) later
			for(NPCQuestResponseConfig cfg : responsecfgs) {
				if(groupByCfgIDAndCounted.size() > 0 && !cfg.worksForFetchQuests()) {
					continue;
				}
				if(!successDone && cfg.isSuccessResponse()) {
					successResponse.accumulate("messageText", cfg.getResponse());
					questObject.accumulate("successResponse", successResponse);
					successDone = true;
				} else if(failureCollected < failureResponses.length && !cfg.isSuccessResponse()) {
					failureResponses[failureCollected] = cfg.getResponse();
					failureCollected++;
				}
				if(successDone && failureCollected == failureResponses.length) break;
			}
			questObject.accumulate("incompleteFailureResponses", failureResponses);


			String questText =  generateQuestText(dal, npc,groupByCfgIDAndCounted.size() > 0, groupByCfgIDAndCounted, new ArrayList<Item.ItemDescription>(wantCfgs.keySet()));
			quest = new QuestInstance(userID, null, new Date(), null, questText, "Random Quest: " + npc.getName(), npc.getImagePath(), npc.getNpcID(), questObject.toString());
			dal.beginTransaction();
			quest = dal.configuration.save(quest);
			dal.commitTransaction();
		}
		return quest;
	}

	private static void generateRewardItems(DAL dal, NPC npc, Map<Item.ItemDescription, Long> demandedItemsAndQty){
		List<NPCQuestRewardConfig> itemsNPCCanCreate = dal.quests.getNPCQuestRewardConfigs();
		Map<Integer, NPCQuestRewardConfig> itemConfigsToInstantiate = new HashMap<>();
		Collections.shuffle(itemsNPCCanCreate);
		for(Map.Entry<Item.ItemDescription, Long> itemAndQty : demandedItemsAndQty.entrySet()) {
			Item.ItemDescription itemDescription = itemAndQty.getKey();
			long qty = itemAndQty.getValue();


			for(NPCQuestRewardConfig rewardItem : itemsNPCCanCreate){
				if(rewardItem.getPercentOdds() != null && !Extensions.flipACoin(rewardItem.getPercentOdds())) {
					continue;
				}
				if(rewardItem.isRarityMatch() &&
						!(itemDescription.getRarity().getItemRarityTypeID() * qty > rewardItem.getRarityMatchFactor() * rewardItem.getRewardItemConfig().getRarity().getItemRarityTypeID())) {
					continue;
				}
				itemConfigsToInstantiate.put(rewardItem.getRewardItemConfig().getItemConfigID(), rewardItem);
			}
		}

		//ItemsBLL.createNewItems()

	}

	private static String generateQuestText(DAL dal, NPC npc, boolean isFetchQuest, Map<Integer, Long> itemCfgIdAndCount, List<Item.ItemDescription> itemDescriptions) {
		String message = "";
		if (isFetchQuest) {
			if (message.length() > 0) {
				message += " and also, bring me"; //todo write some kind of sentence builder, this sucks
			} else {
				message += "Bring me";
			}
			for (int i = 0; i < itemDescriptions.size(); i++) {
				if (itemDescriptions.size() != 1) {
					if (i > 0 && itemDescriptions.size() > 2) {
						message += ",";
					}
					if (i == itemDescriptions.size() - 1) {
						message += " and";
					}
				}
				long ct = itemCfgIdAndCount.get(itemDescriptions.get(i).getItemConfigID());
				message += " " + ct + " " + itemDescriptions.get(i).getItemName() + (ct > 1 ? "es" : ""); //todo write real pluralizer, this will be wrong OFTEN
			}
		}
		return message;
	}

}