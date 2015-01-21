package org.usfirst.frc.team4737.robot.data;

import java.util.ArrayList;

import org.usfirst.frc.team4737.robot.math.Line2D;
import org.usfirst.frc.team4737.robot.math.Vector2d;

public class Map2 {

	public static final byte UNMEASURED = 0;
	public static final byte SPACE = 1;
	public static final byte WALL = 2;

	private byte[] map;
	private int size;
	public int minX;
	public int maxX;
	public int minY;
	public int maxY;

	public Map2(int maxMapSize) {
		map = new byte[maxMapSize * maxMapSize];
		this.size = maxMapSize;
		minX = this.size / 2;
		maxX = this.size / 2;
		minY = this.size / 2;
		maxY = this.size / 2;
	}

	/**
	 * Maps a square millimeter of space with a measured value.
	 * 
	 * @param x
	 * @param y
	 * @param type
	 */
	public void mapPoint(int x, int y, byte type) {
		int mx = x + size / 2;
		int my = y + size / 2;
		if (mx >= 0 && mx < size && my >= 0 && my < size) {
			map[mx + my * size] = type;
			if (mx < minX)
				minX = mx;
			if (mx > maxX)
				maxX = mx;
			if (my < minY)
				minY = my;
			if (my > maxY)
				maxY = my;
		}
	}

	/**
	 * Maps a thick line of space with solid points at the end and empty points
	 * everywhere else in the line.
	 * 
	 * @param width
	 *            - The width of the line to draw
	 * @param sx
	 *            - The starting X coordinate in millimeters.
	 * @param sy
	 *            - The starting Y coordinate in millimeters.
	 * @param ex
	 *            - The ending X coordinate in millimeters.
	 * @param ey
	 *            - The ending Y coordinate in millimeters.
	 */
	public void mapLine(int width, int sx, int sy, int ex, int ey) {
		double angle = Math.atan2(ey - sy, ex - sx);
		double length = Math.hypot(ex - sx, ey - sy);
		fillPolygon(
				new double[] {
						sx + Math.cos(angle - Math.PI / 2) * width / 2.0,
						sy + Math.sin(angle - Math.PI / 2) * width / 2.0,
						sx + Math.cos(angle + Math.PI / 2) * width / 2.0,
						sy + Math.sin(angle + Math.PI / 2) * width / 2.0,
						ex + Math.cos(angle + Math.PI / 2) * width / 2.0,
						ey + Math.sin(angle + Math.PI / 2) * width / 2.0,
						ex + Math.cos(angle - Math.PI / 2) * width / 2.0,
						ey + Math.sin(angle - Math.PI / 2) * width / 2.0, },
				SPACE);
		fillPolygon(
				new double[] {
						Math.cos(angle) * (length - 1)
								+ Math.cos(angle - Math.PI / 2) * width / 2.0,
						Math.sin(angle) * (length - 1)
								+ Math.sin(angle - Math.PI / 2) * width / 2.0,
						Math.cos(angle) * (length - 1)
								+ Math.cos(angle + Math.PI / 2) * width / 2.0,
						Math.sin(angle) * (length - 1)
								+ Math.sin(angle + Math.PI / 2) * width / 2.0,

						ex + Math.cos(angle - Math.PI / 2) * width / 2.0,
						ey + Math.sin(angle - Math.PI / 2) * width / 2.0,
						ex + Math.cos(angle + Math.PI / 2) * width / 2.0,
						ey + Math.sin(angle + Math.PI / 2) * width / 2.0,

				}, WALL);
	}

	private void fillPolygon(double[] points, byte type) {
		ArrayList<Line2D> lines = new ArrayList<Line2D>();
		double xMin = points[0];
		double xMax = points[0];
		double yMin = points[1];
		double yMax = points[1];

		for (int i = 0; i < points.length; i += 2) {
			boolean lastSet = i == points.length - 2;
			double x1 = points[i];
			double y1 = points[i + 1];
			double x2 = lastSet ? points[0] : points[i + 2];
			double y2 = lastSet ? points[1] : points[i + 3];

			// Checks if any of the boundaries need to be expanded
			if (x1 < xMin)
				xMin = x1;
			if (x2 < xMin)
				xMin = x2;
			if (x1 > xMax)
				xMax = x1;
			if (x2 > xMax)
				xMax = x2;
			if (y1 < yMin)
				yMin = y1;
			if (y2 < yMin)
				yMin = y2;
			if (y1 > yMax)
				yMax = y1;
			if (y2 > yMax)
				yMax = y2;
			// Adds the next line of the shape, if necessary for the algorithm
			if (y2 - y1 != 0)
				lines.add(new Line2D(x1, y1, x2, y2));
		}

		for (double y = yMin; y <= yMax; y++) {
			ArrayList<Vector2d> intersections = new ArrayList<Vector2d>();

			// The length being scanned
			Line2D scan = new Line2D(xMin, y, xMax, y);

			// Goes through the list of lines to check if the scan intersects
			// any of them
			for (int i = 0; i < lines.size(); i++) {
				if (scan.intersects(lines.get(i))) {
					Vector2d intersectPoint = scan.lineIntersection(lines
							.get(i));
					intersections.add(intersectPoint);
				}
			}

			for (int i = 0; i < intersections.size() - 1; i += 2) {
				double x1 = intersections.get(i).x;
				double x2 = intersections.get(i + 1).x;
				for (int x = (int) x1; x <= (int) x2; x++) {
					mapPoint(x, (int) y, type);
				}
			}

		}
	}

}
