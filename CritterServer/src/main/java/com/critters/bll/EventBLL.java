package com.critters.bll;

import com.critters.dal.dto.entity.User;

import javax.ws.rs.container.AsyncResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeremy on 12/30/2017.
 */
public class EventBLL {

	private static Map<Integer, User> registrantsForLottery = Collections.synchronizedMap(new HashMap<Integer, User>());
	private static Map<Integer, LotteryEvent> outstandingLotteryWinners = Collections.synchronizedMap(new HashMap<Integer, LotteryEvent>());


	public static void registerForRandomEventLottery(User user) {
		registrantsForLottery.put(user.getUserID(), user);
	}

	public static int numberOfRegistrants(){
		return registrantsForLottery.size();
	}

	public static void clearRegistrations(){
		registrantsForLottery.clear();
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
		int cashPrize = (int)Math.random()*100  + 1;

		LotteryEvent event = new LotteryEvent() {
			@Override
			public void giveaway(User winner) {
				UserBLL.giveUserCash(winner, cashPrize);
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
		if(event == null) return;
		event.giveaway(loggedInUser);
		asyncResponse.resume(event.message);
	}


	public static class LotteryEvent {
		String message;
		String innerHTML;
		public void giveaway(User winner){}
	}
}

