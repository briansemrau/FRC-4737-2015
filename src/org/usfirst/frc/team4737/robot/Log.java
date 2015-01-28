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

	private static double startTime = System.nanoTime() / 1000000000.0;
	private static String date;
	static {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		date = format.format(new Date());
	}
	private static ArrayList<String> log = new ArrayList<String>();

	public static void print(Object o) {
		System.out.print("Log: " + o);
		log.add(o.toString());
	}

	public static void println(Object o) {
		System.out.println("[" + (System.nanoTime() / 1000000000.0 - startTime) + "s]" + o);
		log.add("[" + (System.nanoTime() / 1000000000.0 - startTime) + "s]" + o.toString());
	}

	public static void saveLog() {
		System.out.println("Saving session log...");

		String outputFile = "/home/lvuser/LOGS/log" + date + ".txt";

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					outputFile)));
			for (String line : log) {
				writer.write(line + "\n");
			}
			writer.close();
			System.out.println("Finished saving log.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to write log.");
		}

	}

}
