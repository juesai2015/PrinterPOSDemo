package com.jgw.printerposdemo.printer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

/**
 * SharePreference封装
 * 
 * @author Kevin
 * 
 */
public class PrefUtils {

	public static final String PREF_NAME = "config";

	public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	public static String getString(Context ctx, String key, String defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	public static int getInt(Context ctx, String key, int defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	public static void setInt(Context ctx, String key, int value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}

	public static String getSystemTime() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int seconds = t.second;
		String tag = "AM";
		if (hour >= 12) {
			tag = "PM";
		}
		String time = date + "/" + month + "/" + year + " " + hour + ":" + minute + " " + tag;
		return time;

	}

	public static String getSystemTime2() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int seconds = t.second;
		String tag = "AM";
		if (hour >= 12) {
			tag = "PM";
		}
		String time;
		if (month < 10) {
			if (seconds >= 10) {
				time = year + "-0" + month + "-" + date + "     " + hour + ":" + minute + ":" + seconds;
			} else {
				time = year + "-0" + month + "-" + date + "     " + hour + ":" + minute + ":0" + seconds;
			}

		} else {
			if (seconds >= 10) {
				time = year + "-" + month + "-" + date + "     " + hour + ":" + minute + ":" + seconds;
			} else {
				time = year + "-" + month + "-" + date + "     " + hour + ":" + minute + ":0" + seconds;
			}

		}
		return time;

	}

	public static String getSystemTime3() {
		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int seconds = t.second;
		String tag = "AM";
		if (hour >= 12) {
			tag = "PM";
		}
		String time;
		if (month < 10) {
			if (seconds >= 10) {
				time = year + "-0" + month + "-" + date;
			} else {
				time = year + "-0" + month + "-" + date;
			}

		} else {
			if (seconds >= 10) {
				time = year + "-" + month + "-" + date;
			} else {
				time = year + "-" + month + "-" + date;
			}

		}
		return time;

	}
}
