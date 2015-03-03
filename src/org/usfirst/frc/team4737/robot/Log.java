package org.usfirst.frc.team4737.robot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.wpi.first.wpilibj.DriverStation;

public class Log {

//	static {
//		final Thread mainThread = Thread.currentThread();
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			public void run() {
//				println("Caught shutdown hook!");
//				saveLog();
//				try {
//					mainThread.join();
//					println("Finally shutting down.");
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	private static double startTime;
	private static String date;
	
	private static ArrayList<String> log;
	
	public static void init() {
		startTime = System.nanoTime() / 1000000000.0;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		date = format.format(new Date());
		
		log = new ArrayList<String>();
	}

	public static void print(Object o) {
//		System.out.print("Log: " + o);
		String text = "[" + (int) ((System.nanoTime() / 1000000000.0 - startTime) * 1000) / 1000.0 + "s]\t" + o;
		log.add(text);
//		DriverStation.reportError(text, false);
	}

	public static void println(Object o) {
		String text = "[" + (int) ((System.nanoTime() / 1000000000.0 - startTime) * 1000) / 1000.0 + "s]\t" + o;
//		System.out.println(text);
		log.add(text);
//		DriverStation.reportError(text + "\n", false);
	}

	public static void saveLog() {

		String outputFile = "/home/lvuser/LOGS/log" + date + ".txt";
//		Log.println("Saving session log (file: " + outputFile + ")...");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFile)));
			for (String line : log) {
				writer.write(line + "\n");
			}
			writer.close();
//			Log.println("Finished saving log.");
		} catch (IOException e) {
			e.printStackTrace();
			Log.println("Failed to write log.");
		}

	}

}
