package com.jgw.printerposdemo.printer;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ExitApplication extends Application {
	private List<Activity> activityList = new LinkedList();
	private static ExitApplication instance;

	private ExitApplication() {
	}

	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
//	public void remveActivity(Activity activity) {
//		activityList.remove(activity);
//	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
