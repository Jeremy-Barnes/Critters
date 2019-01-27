package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.critters.dal.accessors.DAL;
import com.critters.dal.entity.Item;
import com.critters.dal.entity.NPC;
import com.critters.dal.entity.QuestInstance;
import com.critters.dal.entity.User;
import com.critters.dto.Notification;
import com.critters.dto.UINotification;

import javax.ws.rs.container.AsyncResponse;
import java.util.*;

/**
 * Created by Jeremy on 12/30/2017.
 */
public class EventBLL {

	private static Map<Integer, User> registrantsForLottery = Collections.synchronizedMap(new HashMap<Integer, User>());
	private static Map<Integer, LotteryEvent> outstandingLotteryWinners = Collections.synchronizedMap(new HashMap<Integer, LotteryEvent>());
	private static Map<Integer, Integer> purchasedLuck = Collections.synchronizedMap(new HashMap<Integer, Integer>());

	public static void registerForRandomEventLottery(User user) {
		registrantsForLottery.put(user.getUserID(), user);
	}

	public static int numberOfRegistrants(){
		return registrantsForLottery.size();
	}

	public static void clearRegistrations(){
		registrantsForLottery.clear();
	}

	public static void linkRandomEventsToUsers(List<LotteryEvent> events) {
		if(Extensions.isNullOrEmpty(registrantsForLottery.keySet()) || Extensions.isNullOrEmpty(events))
			return;

		for(LotteryEvent event : events) {
			User winner = null;

			while(winner == null && !Extensions.isNullOrEmpty(registrantsForLottery.keySet())) {
				double odds = 1/registrantsForLottery.size();
				for(User registrant : registrantsForLottery.values()) {
					int tries = 1;
					Integer luck = purchasedLuck.get(registrant);
					if(luck != null && luck > 0) {
						tries = (int) Math.ceil(5.0 * Math.log10((double)luck)) + 1;
					}
					for(int tryNumber = tries; tryNumber >= 0; tryNumber--) {
						double random = Math.random();
						if (random <= odds) {
							winner = registrant;
						}
					}
				}
			}
			if(winner != null) {
				outstandingLotteryWinners.put(winner.getUserID(), event);
				purchasedLuck.remove(winner.getUserID());
				registrantsForLottery.remove(winner.getUserID());
			}
		}
	}

	//used in JS, do not delete
	public static void purchaseSomeLuck(int userID, int luck){
		Integer newLuck = purchasedLuck.get(userID);
		newLuck = newLuck == null ? luck : newLuck + luck;
		purchasedLuck.put(userID, newLuck);
	}

	public static LotteryEvent generateRandomEvent(){
		double threshold = Math.random();//used for determining random event giveaway types
		if(threshold < .5) {
			return generateCashGiveaway();
		} else if(threshold < .75) {
			return generateFlavorTextGiveaway();
		} else if(threshold < 1) {
			return generateFloorItemGiveaway();
		} else {
			return generateQuestGiveaway();
		}
	}

	private static LotteryEvent generateCashGiveaway(){
		int cashPrize = (int)((Math.random()*100) + 1); //todo economics

		LotteryEvent event = new LotteryEvent() {
			@Override
			public UINotification actionToUser(User winner) {
				winner.setCritterbuxx(UserBLL.alterUserCash(winner.getUserID(), cashPrize));
				UINotification notification = new UINotification();
				notification.body = "You found something! " + cashPrize + "cB!";
				notification.title = "Random Event";
				return notification;
			}
		};
		event.message = "You found " + cashPrize + " critterbucks, just laying around! You shouldn't be seeing this message";
		return event;
	}

	private static LotteryEvent generateFlavorTextGiveaway(){
		LotteryEvent event = new LotteryEvent() {
			@Override
			public UINotification actionToUser(User winner) {
				return null;
			}
		};
		event.message = "Test message: you won some meaningless fluff"; //todo cfg tables
		return event;
	}

	private static LotteryEvent generateFloorItemGiveaway(){

		List<Item> items = ItemsBLL.getAbandonedItems(true);
		List<Item> foundItems = new ArrayList<Item>();
		if(Extensions.isNullOrEmpty(items)) return null;

		int totalRarity = 0;
		for(Item i : items) {
			if(i.getDescription().getRarity().getItemRarityTypeID() >= 4) {
				foundItems.clear();
			}
			foundItems.add(i);
			if(totalRarity >= 4) {
				break;
			}
		}

		LotteryEvent event = new LotteryEvent() {
			@Override
			public UINotification actionToUser(User winner) {
				if(ItemsBLL.giveDiscardedInventoryItems(items, winner)) {
					UINotification notification = new UINotification();
					notification.body = "You found something!";
					notification.title = "Random Event";
					notification.customBodyHTML = items.stream().map(i -> i.getDescription().getItemName()).reduce("",(s, s2) -> s + ", " + s2);//todo build a real UI template here
					return notification;
				}
				return null;
			}
		};
		event.message = "Test message: you won an item that someone else threw on the ground. You probably shouldn't see this message??? ";
		return event;
	}

	private static LotteryEvent generateQuestGiveaway(){
		List<NPC> questNPCs;
		try(DAL dal = new DAL()){
			questNPCs = dal.npcs.getNPCWithRandomQuests(); //could use this opportunity to filter based on alliance, friendliness, future features maybe
		}
		NPC questIssuer = Extensions.pickRandomItem(questNPCs);

		LotteryEvent event = new LotteryEvent() {
			@Override
			public UINotification actionToUser(User winner) {
				QuestInstance randomQuest = WorldBLL.generateARandomQuest(questIssuer, winner.getUserID(), null, null);
				return null;
			}
		};
		event.message = "Test message- " + questIssuer.getName() + " says: Hey, I have a job for you!"; //todo unique NPC message text? needs a new table or field in the NPC action script
		return event;
	}

	public static void redeemRandomEventForUser(User loggedInUser, AsyncResponse asyncResponse) {
		LotteryEvent event = outstandingLotteryWinners.get(loggedInUser.getUserID());
		outstandingLotteryWinners.remove(loggedInUser.getUserID());
		if(event == null) return;
		UINotification notification = event.actionToUser(loggedInUser);
		if(notification == null) return;
		asyncResponse.resume(new Notification(null, null, notification));
	}

	public abstract static class LotteryEvent {
		String message;
		String innerHTML;
		public abstract UINotification actionToUser(User winner);
	}
}

