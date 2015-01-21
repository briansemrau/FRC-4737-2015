package org.usfirst.frc.team4737.robot.vision.components;

public abstract class GamePiece {

	public double posX;
	public double posY;
	public double zRot;
	public int stacksHigh;

	public GamePiece(double x, double y, double rz, int stacksHigh) {
		this.posX = x;
		this.posY = y;
		this.zRot = rz;
		this.stacksHigh = stacksHigh;
	}

}
