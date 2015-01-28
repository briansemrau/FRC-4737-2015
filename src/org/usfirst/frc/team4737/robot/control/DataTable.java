package org.usfirst.frc.team4737.robot.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.usfirst.frc.team4737.robot.Log;
import org.usfirst.frc.team4737.robot.math.Vector2d;

public class DataTable {

	private double interval;
	private double minVal, maxVal;
	private double defaultVal;

	private double[] table;

	private ArrayList<Vector2d> prevSearched;

	public DataTable(double xInterval, double minVal, double maxVal, double defaultVal) {
		init(xInterval, minVal, maxVal, defaultVal);
	}

	private void init(double xInterval, double minVal, double maxVal, double defaultVal) {
		this.interval = xInterval;
		this.minVal = minVal;
		this.maxVal = maxVal;
		
		table = new double[(int) ((maxVal - minVal) / interval)];
		for (int i = 0; i < table.length; i++) {
			table[i] = Double.NaN;
		}
		
		prevSearched = new ArrayList<Vector2d>();
	}

	public DataTable(String filename) {
		double xInterval = 1;
		double minVal = 0;
		double maxVal = 0;
		double defaultVal = 0;
		try {
			Scanner scan = new Scanner(new File(filename));

			xInterval = scan.nextDouble();
			minVal = scan.nextDouble();
			maxVal = scan.nextDouble();
			defaultVal = scan.nextDouble();

			init(xInterval, minVal, maxVal, defaultVal);
			for (int i = 0; i < table.length; i++) {
				table[i] = scan.nextDouble();
			}

			scan.close();
		} catch (NoSuchElementException e) {
			init(xInterval, minVal, maxVal, defaultVal);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.println("Failed to load a data table from file \"" + filename + "\"");
			init(xInterval, minVal, maxVal, defaultVal);
		}
	}

	public void save(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));
			writer.write("" + interval + "\n");
			writer.write("" + minVal + "\n");
			writer.write("" + maxVal + "\n");
			writer.write("" + defaultVal + "\n");
			for (int i = 0; i < table.length; i++) {
				writer.write("" + table[i] + (i == table.length - 1 ? "" : " "));
			}
			writer.close();
			Log.println("Saved data table \"" + filename + "\"");
		} catch (IOException e) {
			e.printStackTrace();
			Log.println("Failed to save data table \"" + filename + "\"");
		}
	}

	public boolean mapValue(double x, double y, boolean overwrite) {
		int xtable = (int) ((x - minVal) / interval);
		if (xtable >= 0 && xtable < table.length) {
			if (overwrite || table[xtable] == Double.NaN) {
				table[xtable] = y;
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public double getY(double x) {
		int xtable = (int) ((x - minVal) / interval);
		if (xtable >= 0 && xtable < table.length) {
			return table[xtable];
		}
		return defaultVal;
	}

	public double findClosestX(double y) {
		for (int i = 0; i < prevSearched.size(); i++) {
			Vector2d point = prevSearched.get(i);
			if (y == point.y)
				return point.x;
		}

		double val = defaultVal;
		double x = 0;
		for (int i = 0; i < table.length; i++) {
			if (Math.abs(y - table[i]) < Math.abs(y - val)) {
				val = table[i];
				x = i * interval + minVal;
			}
		}

		prevSearched.add(new Vector2d(x, y));

		return val;
	}

}
