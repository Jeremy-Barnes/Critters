package com.critters.backgroundservices;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Jeremy on 8/24/2016.
 */
@Singleton
public class BackgroundJobManager {

	public static int jobs = 0;
	public static String logs = "";

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

}
