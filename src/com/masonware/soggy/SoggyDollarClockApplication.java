package com.masonware.soggy;


import android.app.Application;

public class SoggyDollarClockApplication extends Application {
	private static SoggyDollarClockApplication instance = null;
	public static final String PREFS = "ClockPrefs";
	
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	public static SoggyDollarClockApplication getInstance() {
		return instance;
	}
}
