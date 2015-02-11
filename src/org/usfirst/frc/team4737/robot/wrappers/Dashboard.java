package org.usfirst.frc.team4737.robot.wrappers;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {

	public static final String DB_STRING_0 = "DB/String 0";
	public static final String DB_STRING_1 = "DB/String 1";
	public static final String DB_STRING_2 = "DB/String 2";
	public static final String DB_STRING_3 = "DB/String 3";
	public static final String DB_STRING_4 = "DB/String 4";
	public static final String DB_STRING_5 = "DB/String 5";
	public static final String DB_STRING_6 = "DB/String 6";
	public static final String DB_STRING_7 = "DB/String 7";
	public static final String DB_STRING_8 = "DB/String 8";
	public static final String DB_STRING_9 = "DB/String 9";
	
	public static final String DB_BUTTON_0 = "DB/Button 0";
	public static final String DB_BUTTON_1 = "DB/Button 1";
	public static final String DB_BUTTON_2 = "DB/Button 2";
	public static final String DB_BUTTON_3 = "DB/Button 3";
	
	public static final String DB_LED_0 = "DB/LED 0";
	public static final String DB_LED_1 = "DB/LED 1";
	public static final String DB_LED_2 = "DB/LED 2";
	public static final String DB_LED_3 = "DB/LED 3";
	
	public static final String DB_SLIDER_0 = "DB/Slider 0";
	public static final String DB_SLIDER_1 = "DB/Slider 1";
	public static final String DB_SLIDER_2 = "DB/Slider 2";
	public static final String DB_SLIDER_3 = "DB/Slider 3";
	
	public static void putString(String dbVal, String output) {
		SmartDashboard.putString(dbVal, output);
	}
	
	public static String getString(String dbVal) {
		return getString(dbVal, "");
	}
	
	public static String getString(String dbVal, String defaultValue) {
		return SmartDashboard.getString(dbVal, "");
	}
	
	public static void setButton(String dbVal, boolean output) {
		SmartDashboard.putBoolean(dbVal, output);
	}
	
	public static boolean getButton(String dbVal) {
		return getButton(dbVal, false);
	}
	
	public static boolean getButton(String dbVal, boolean defaultValue) {
		return SmartDashboard.getBoolean(dbVal, defaultValue);
	}
	
	public static void putSlider(String dbVal, double output) {
		SmartDashboard.putNumber(dbVal, output);
	}
	
	public static double getSlider(String dbVal) {
		return getSlider(dbVal, 0);
	}
	
	public static double getSlider(String dbVal, double defaultValue) {
		return SmartDashboard.getNumber(dbVal, defaultValue);
	}
	
}
