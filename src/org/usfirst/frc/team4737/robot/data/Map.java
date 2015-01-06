package org.usfirst.frc.team4737.robot.data;

import java.util.ArrayList;

import org.usfirst.frc.team4737.robot.math.Line2D;
import org.usfirst.frc.team4737.robot.math.Vector2d;

/**
 * A map drawn out by points.
 * 
 * @author Brian
 *
 */
public class Map {

	public class Point {
		public Vector2d p;
		public boolean solid;

		public Point(Vector2d p, boolean solid) {
			this.p = p;
			this.solid = solid;
		}
	}

	private ArrayList<Point> points;
	private double closestDist;
	private double closestDistSquare;

	public Map(double pointRadius) {
		points = new ArrayList<Point>();
		closestDist = (pointRadius * 2.0) * 0.8;
		closestDistSquare = closestDist * closestDist;
	}

	/**
	 * Maps a line on the map.
	 * 
	 * @param line
	 *            - The line to map, with vertex 'a' being the start of the line
	 *            and the vertex 'b' being the end point.
	 */
	public void mapLine(Line2D line) {
		double length = line.length();
		double sin = Math.sin(Math.atan2(line.b.y - line.a.y, line.b.x - line.a.x)) * closestDist;
		double cos = Math.cos(Math.atan2(line.b.y - line.a.y, line.b.x - line.a.x)) * closestDist;
		int n = 0;
		for (double d = 0; d < ((int) (length / closestDist) - 1) * closestDist; d += closestDist) {
			mapPoint(new Point(new Vector2d(cos * n, sin * n), false), false);
			n++;
		}
		mapPoint(new Point(line.b.clone(), true), true);
		mapPoint(new Point(line.b.clone().minus(new Vector2d(cos, sin)), false), true);
	}

	public void mapPoint(Point point, boolean override) {
		if (override) {
			points.add(point);
			return;
		}
		for (Point p : points) {
			if (p.p.distanceSquared(point.p) < closestDistSquare) {
				points.add(point);
				return;
			}
		}
	}

	public boolean intersectingWall(Line2D bound, Vector2d bodyCenter, double checkRange) {
		double rangeSquare = checkRange * checkRange;
		for (Point p : points) {
			if (bodyCenter.distanceSquared(p.p) < rangeSquare)
				if (bound.closestDistanceSquared(p.p) < closestDistSquare) {
					return true;
				}
		}
		return false;
	}

}
