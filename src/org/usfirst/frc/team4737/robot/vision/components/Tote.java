package org.usfirst.frc.team4737.robot.vision.components;

public class Tote extends GamePiece {

	public static Tote[] createTotes(LGroup[] groups) {
		for (LGroup group : groups) {
			
			// TODO
			
		}
		return null;
	}

	public boolean yellow;

	private Tote(double x, double y, double rz, int stacksHigh, boolean yellow) {
		super(x, y, rz, stacksHigh);
		this.yellow = yellow;
	}

}
