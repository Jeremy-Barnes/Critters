package com.critters.backgroundservices;

import com.critters.bll.CommerceBLL;
import com.critters.bll.EventBLL;
import com.critters.dal.entity.NPCStoreRestockConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jeremy on 8/24/2016.
 */
@Singleton
public class BackgroundJobManager {

	static final Logger logger = LoggerFactory.getLogger("application");


	@Schedule(hour="*", minute="*/15", second="*", persistent=true)
	public void restockAllShopsMaybe() {
		Random rand = new Random();
		List<NPCStoreRestockConfig> rcfgs = CommerceBLL.getAllRestockConfigs();
		for(int i = 0; i < rcfgs.size(); i++){
			if(rand.nextInt(97) == 7){ //why 7? why not? its a 1 in 96 chance and thats all I care about.
			// 1 in 96 because this job happens every quarter hour and  I really only want it to happen once per day per rule.
				if(rand.nextDouble() <= rcfgs.get(i).getPercentOdds()){
					logger.info("Restocking" + rcfgs.get(i).toString());
					CommerceBLL.restock(rcfgs.get(i));
				}
			}
		}
	}

	@Schedule(hour="*", minute="*/5", second="1", persistent=true)
	public void generateRandomEvents() {
		int players = EventBLL.numberOfRegistrants();
		int numberOfEventsToGenerate = 0;

		if(players > 1 && players < 10) {
			numberOfEventsToGenerate = (int) Math.round(Math.random()) + 1;
		} else if(players < 500) {
			numberOfEventsToGenerate = (int)(players * .1);
		} else if(players > 500) {
			numberOfEventsToGenerate = (int)(players * .25);
			logger.warn("WOW TOO MANY PLAYERS");
		}
		ArrayList<EventBLL.LotteryEvent> events = new ArrayList();
		for(int i = 0; i < numberOfEventsToGenerate; i++) {
			EventBLL.LotteryEvent event = EventBLL.generateRandomEvent();
			if(event != null) events.add(event);
			else i--;
		}
		EventBLL.linkRandomEventsToUsers(events);
		EventBLL.clearRegistrations();
	}

}
