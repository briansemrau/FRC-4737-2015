package org.usfirst.frc.team4737.robot.vision;

public class ColorThreshold {
	
	public static final ColorThreshold WHITE = new ColorThreshold(250, 255, 250, 255, 250, 255);
//	public static final ColorThreshold RED = new ColorThreshold(25, 255, 0, 45, 0, 47); // This isn't needed
	public static final ColorThreshold GREEN = new ColorThreshold(0, 45, 25, 255, 0, 45);
	public static final ColorThreshold YELLOW = new ColorThreshold(25, 255, 25, 255, 0, 45);

	public int redLow;
	public int redHigh;
	public int greenLow;
	public int greenHigh;
	public int blueLow;
	public int blueHigh;
	
	public ColorThreshold(int redLow, int redHigh, int greenLow, int greenHigh, int blueLow, int blueHigh) {
		this.redLow = redLow;
		this.redHigh = redHigh;
		this.greenLow = greenLow;
		this.greenHigh = greenHigh;
		this.blueLow = blueLow;
		this.blueHigh = blueHigh;
	}
	
}
