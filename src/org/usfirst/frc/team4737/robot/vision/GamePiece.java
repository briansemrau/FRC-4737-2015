package org.usfirst.frc.team4737.robot.vision;

public class GamePiece {

	public enum Type {
		BIN(0.5, 0.5), // TODO measure dimensions of pieces
		GRAY(0.5, 0.25),
		YELLOW(0.5, 0.25);

		public double width;
		public double depth;

		private Type(double width, double depth) {
			this.width = width;
			this.depth = depth;
		}
	}

	public double posX;
	public double posY;
	public double zRot;
	public int stacksHigh;
	public Type type;

	public GamePiece(double x, double y, double rz, int stacksHigh, Type type) {
		this.posX = x;
		this.posY = y;
		this.zRot = rz;
		this.stacksHigh = stacksHigh;
		this.type = type;
	}

}
