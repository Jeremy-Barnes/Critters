package com.critters.backgroundservices;

import com.critters.bll.CommerceBLL;
import com.critters.dal.dto.entity.NPCStoreRestockConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Random;

/**
 * Created by Jeremy on 8/24/2016.
 */
@Singleton
public class BackgroundJobManager {

	public static int jobs = 0;
	public static String logs = "";
	static final Logger logger = LoggerFactory.getLogger(BackgroundJobManager.class);

	public static void printLine(String string){
		logs = logs + " \n" + string + "\n\n\n next log \n\n\n";
		System.out.println(string);

	}

	public static void printLine(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		printLine(sw.toString());
	}

	@Schedule(hour="*", minute="*", second="*/10", persistent=true)
	public void someQuarterlyJob() {
		jobs++;
		System.out.println("JOB JAERB JEOREARB");

	}

	@Schedule(hour="*", minute="*/15", second="*", persistent=true)
	public void restockAllShopsMaybe() {
		Random rand = new Random();
		List<NPCStoreRestockConfig> rcfgs = CommerceBLL.getAllRestockConfigs();
		for(int i = 0; i < rcfgs.size(); i++){
			if(rand.nextInt(97) == 7){ //why 7? why not? its a 1 in 96 chance and thats all I care about.
			// 1 in 96 because this job happens every quarter hour and  I really only want it to happen once per day per rule.
				if(rand.nextDouble() <= rcfgs.get(i).getPercentOdds()){
					CommerceBLL.restock(rcfgs.get(i));
				}
			}
		}
	}

}
