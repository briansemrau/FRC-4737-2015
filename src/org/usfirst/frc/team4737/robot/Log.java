package org.usfirst.frc.team4737.robot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Log {

	static {
		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				println("Caught shutdown hook!");
				saveLog();
				try {
					mainThread.join();
					println("Finally shutting down.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static String date;
	static {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		date = format.format(new Date());
	}
	private static ArrayList<String> log;

	public static void print(Object o) {
		System.out.print("Log: " + o);
		log.add(o.toString());
	}

	public static void println(Object o) {
		System.out.println("Log: " + o);
		log.add(o.toString());
	}

	public static void saveLog() {
		System.out.println("Saving session log...");

		String outputFile = new String();
		File f = new File("/Volumes/NO NAME");
		if (f.exists() && f.isDirectory()) {
			outputFile = "/Volumes/NO NAME/log" + date + ".txt";
		} else {
			outputFile = "log" + date + ".txt";
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					outputFile)));
			for (String line : log) {
				writer.write(line + "\n");
			}
			writer.close();
			System.out.println("Finished!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to write log.");
		}

	}

}
