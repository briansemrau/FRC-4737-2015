package org.usfirst.frc.team4737.robot.vision.components;

public class Bin extends GamePiece {

	public static Bin[] getBins(BinShape[] shapes) {
		return new Bin[0];
	}
	
	private Bin(double x, double y, double rz, int stacksHigh) {
		super(x, y, rz, stacksHigh);
	}

}
