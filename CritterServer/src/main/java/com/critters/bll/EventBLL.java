package com.critters.bll;

import com.critters.Utilities.Extensions;
import com.critters.dal.dto.Notification;
import com.critters.dal.dto.UINotification;
import com.critters.dal.dto.entity.User;

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

	public static void distributePrizes(List<LotteryEvent> events) {
		if(Extensions.isNullOrEmpty(registrantsForLottery.keySet()) || Extensions.isNullOrEmpty(events))
			return;

		for(LotteryEvent event : events) {
			User winner = null;

			double odds = 1/registrantsForLottery.size();

			while(winner == null && !Extensions.isNullOrEmpty(registrantsForLottery.keySet())) {
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
			outstandingLotteryWinners.put(winner.getUserID(), event);
			purchasedLuck.remove(winner.getUserID());
			registrantsForLottery.remove(winner.getUserID());
		}
	}

	public static void purchaseSomeLuck(int userID, int luck){
		Integer newLuck = purchasedLuck.get(userID);
		newLuck = newLuck == null ? luck : newLuck + luck;
		purchasedLuck.put(userID, newLuck);
	}

	public static LotteryEvent generateRandomEvent(){
		double threshold = Math.random();//used for determining giveaway types
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
		int cashPrize = (int)((Math.random()*100) + 1);

		LotteryEvent event = new LotteryEvent() {
			@Override
			public void giveaway(User winner) {
				winner.setCritterbuxx(UserBLL.giveUserCash(winner.getUserID(), cashPrize));
			}
		};
		event.message = "You found " + cashPrize + " critterbucks, just laying around!";
		return event;
	}

	private static LotteryEvent generateFlavorTextGiveaway(){
		LotteryEvent event = new LotteryEvent() {
			@Override
			public void giveaway(User winner) {
			}
		};
		event.message = "The Spoon Angel says: FART";
		return event;
	}

	private static LotteryEvent generateFloorItemGiveaway(){
		LotteryEvent event = new LotteryEvent() {
			@Override
			public void giveaway(User winner) {
			}
		};
		event.message = "The Spoon Angel says: FART";
		return event;
	}

	private static LotteryEvent generateQuestGiveaway(){
		LotteryEvent event = new LotteryEvent() {
			@Override
			public void giveaway(User winner) {
			}
		};
		event.message = "The Spoon Angel says: FART";
		return event;
	}

	public static void tellMeWhatIWon(User loggedInUser, AsyncResponse asyncResponse) {
		LotteryEvent event = outstandingLotteryWinners.get(loggedInUser.getUserID());
		outstandingLotteryWinners.remove(loggedInUser.getUserID());
		if(event == null) return;
		event.giveaway(loggedInUser);


		//temp, put in generate method
		UINotification notice = new UINotification();
		notice.body = event.message;
		notice.title = "LOL SO RANDOM";
		notice.customBodyHTML = "<img src=\"https://40.media.tumblr.com/35934de8201e95eafc07fc77282a8359/tumblr_inline_nvepzwfkaA1qmorkz_540.jpg\" />";
		Notification not = new Notification();
		not.serverMessages = new ArrayList<UINotification>();
		not.serverMessages.add(notice);


		asyncResponse.resume(not);
	}

	public static class LotteryEvent {
		String message;
		String innerHTML;
		public void giveaway(User winner){}
	}
}

