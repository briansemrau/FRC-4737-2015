package org.usfirst.frc.team4737.robot.math;

/**
 * A 2D line segment consisting of two endpoints.
 * 
 * @author Brian
 *
 */
public class Line2D {

	public Vector2d a;
	public Vector2d b;

	public Line2D() {
		a = new Vector2d();
		b = new Vector2d();
	}

	public Line2D(Vector2d a, Vector2d b) {
		this.a = a;
		this.b = b;
	}
	
	public Line2D(double x1, double y1, double x2, double y2) {
		a = new Vector2d(x1, y1);
		b = new Vector2d(x2, y2);
	}

	/**
	 * @return Returns the X value of the lowest value endpoint.
	 */
	public double minX() {
		return Math.min(a.x, b.x);
	}

	/**
	 * @return Returns the X value of the highest value endpoint.
	 */
	public double maxX() {
		return Math.max(a.x, b.x);
	}

	/**
	 * @return Returns the Y value of the lowest value endpoint.
	 */
	public double minY() {
		return Math.min(a.y, b.y);
	}

	/**
	 * @return Returns the Y value of the highest value endpoint.
	 */
	public double maxY() {
		return Math.max(a.y, b.y);
	}
	
	/**
	 * @return Returns the length of the line.
	 */
	public double length() {
		return Math.hypot(b.x - a.x, b.y - a.y);
	}

	/**
	 * Determines if an intersection is occurring with another line segment.
	 * 
	 * @param b
	 *            - The line to test intersection with
	 * @return Returns whether or not the line intersects with this one.
	 */
	public boolean intersects(Line2D b) {
		Vector2d p = lineIntersection(b);
		if (p.x >= minX() && p.x <= maxX() && p.y >= minY() && p.y <= maxY())
			return true;
		return false;
	}

	/**
	 * Calculates an intersection point between another line assuming both lines
	 * have no end points.
	 * 
	 * @param l
	 *            - The line to find an intersection point with
	 * @return Returns the intersection point between this line and the line
	 *         given.
	 */
	public Vector2d lineIntersection(Line2D l) {
		double factor1 = (a.x * b.y - a.y * b.x);
		double factor2 = (l.a.x * l.b.y - l.a.y * l.b.x);
		double denominator = ((a.x - b.x) * (l.a.y - l.b.y) - (a.y - b.y) * (l.a.x - l.b.x));
		return new Vector2d((factor1 * (l.a.x - l.b.x) - (a.x - b.x) * factor2) / denominator, (factor1 * (l.a.y - l.b.y) - (a.y - b.y) * factor2) / denominator);
	}

	public double closestDistance(Vector2d point) {
		Line2D perpendicular = new Line2D(point, new Vector2d(b.x - a.x + point.x, b.y - a.y + point.y));
		
		Vector2d intersection = lineIntersection(perpendicular);
		
		if (intersection.x >= minX() && intersection.x <= maxX() && intersection.y >= minY() && intersection.y <= maxY()) {
			return intersection.distance(point);
		} else {
			return Math.min(a.distance(intersection), b.distance(intersection));
		}
	}

	public double closestDistanceSquared(Vector2d point) {
		Line2D perpendicular = new Line2D(point, new Vector2d(b.x - a.x + point.x, b.y - a.y + point.y));
		Vector2d intersection = lineIntersection(perpendicular);
		if (intersection.x >= minX() && intersection.x <= maxX() && intersection.y >= minY() && intersection.y <= maxY()) {
			return intersection.distanceSquared(point);
		} else {
			return Math.min(a.distanceSquared(intersection), b.distanceSquared(intersection));
		}
	}

}
