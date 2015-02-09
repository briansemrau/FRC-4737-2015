package org.usfirst.frc.team4737.robot.math;

/**
 * A class representing a 2D coordinate.
 * 
 * @author Brian
 *
 */
public class Vector2d {

	public double x;
	public double y;

	public Vector2d() {
	}
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d(double angleDeg, double magnitude, Vector2d point) {
		x = point.x + Math.cos(Math.toRadians(angleDeg)) * magnitude;
		y = point.y + Math.sin(Math.toRadians(angleDeg)) * magnitude;
	}

	public Vector2d plus(Vector2d b) {
		return new Vector2d(x + b.x, y + b.y);
	}

	public Vector2d minus(Vector2d b) {
		return new Vector2d(x - b.x, y - b.y);
	}

	/**
	 * @param b
	 *            - Another point
	 * @return Returns the distance between another point.
	 */
	public double distance(Vector2d b) {
		return Math.hypot(b.x - x, b.y - y);
	}

	/**
	 * @param b
	 *            - Another point
	 * @return Returns the distance squared between another point.
	 */
	public double distanceSquared(Vector2d b) {
		return (b.x - x) * (b.x - x) + (b.y - y) * (b.y - y);
	}
	
	public Vector2d scaled(double max) {
		double x = this.x;
		double y = this.y;
		if (x > max || y > max) {
			double higher = x > y ? x : y;
			x *= max / higher;
			y *= max / higher;
		}
		return new Vector2d(x, y);
	}

	public Vector2d clone() {
		return new Vector2d(x, y);
	}

}
