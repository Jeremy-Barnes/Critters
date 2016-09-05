package com.critters.backgroundservices;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * Created by Jeremy on 8/24/2016.
 */
@Singleton
public class BackgroundJobManager {

	public static int jobs = 0;

	@Schedule(hour="*", minute="*", second="*/10", persistent=true)
	public void someQuarterlyJob() {
		jobs++;
		System.out.println("JOB JAERB JEOREARB");
	}

}
