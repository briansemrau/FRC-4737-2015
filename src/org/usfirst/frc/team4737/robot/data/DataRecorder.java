package org.usfirst.frc.team4737.robot.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.usfirst.frc.team4737.robot.Log;

/**
 * 
 * This class is for recording, saving, and outputting data such as gyroscope readings, position, battery voltage, etc.
 * 
 * @author Brian
 *
 */
public class DataRecorder {

	private static class Dataset {
		public String varname;
		public ArrayList<Object> data;

		public Dataset(String varname) {
			this.varname = varname;
			data = new ArrayList<Object>();
		}
	}

	/**
	 * The current list of data sets.
	 */
	private static ArrayList<Dataset> datasets = new ArrayList<Dataset>();

	/**
	 * The time when the robot started
	 */
	private static String date;
	static {
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		date = format.format(new Date());
	}

	/**
	 * Records a piece of data.
	 * 
	 * @param varName
	 *            - The name of the data set to store the value in
	 * @param value
	 *            - The data to store
	 */
	public static void record(String varName, Object value) {
		Dataset set = findDataset(varName);
		set.data.add(value);
	}

	/**
	 * Outputs all the collected data to a log on the RoboRio's file system (
	 * <code>/home/lvuser/DATALOGS/datalog<#>.txt</code>).
	 */
	public static void saveDataLog() {
		Log.println("Saving session data...");

		String outputFile = "/home/lvuser/datalog" + date + ".txt";

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFile)));
			for (Dataset set : datasets) {
				String output = set.varname;
				for (Object o : set.data) {
					output += "," + o.toString();
				}
				writer.write(output + "\n");
			}
			writer.close();
			Log.println("Finished saving datalog.");
		} catch (IOException e) {
			e.printStackTrace();
			Log.println("Failed to write data log.");
		}

	}

	/**
	 * Searches for a data set with the given variable name and returns it. If the set is not found, a new data set is
	 * created.
	 * 
	 * @param varname
	 *            - The name of the variable recorded in the data set in search of.
	 * @return Returns the found data set, or a newly created data set.
	 */
	private static Dataset findDataset(String varname) {
		Dataset returnSet = null;
		boolean found = false;
		for (Dataset set : datasets) {
			if (set.varname.equals(varname)) {
				returnSet = set;
				found = true;
				break;
			}
		}
		if (found)
			return returnSet;
		else {
			returnSet = new Dataset(varname);
			datasets.add(returnSet);
			return returnSet;
		}
	}

	public static void clear() {
		datasets = new ArrayList<Dataset>();
	}
}
