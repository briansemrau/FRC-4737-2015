package org.usfirst.frc.team4737.robot.vision.components;

public class BinShape {

	public static BinShape[] findShapes(Rect4i[] rects) {
		return new BinShape[0];
	}

	private double midX, topY;
	private double width;

	private BinShape(Rect4i rect) {
		midX = rect.x + rect.w / 2;
		topY = rect.y;
		width = rect.w;
	}

	public double getDistance(double fov) {
		// Distance will be calculated from the width of the rectangle and the fov of the camera
		return 0;
	}

	public double getAngle(int imgW, double fov) {
		return ((imgW / 2) - midX) / (double) imgW * fov;
	}

}
