package org.usfirst.frc.team4737.robot.vision;

import java.util.ArrayList;

import org.usfirst.frc.team4737.robot.math.Vector2d;

public class L {

	public static L[] findLs(Rect4i[] rects) {
		ArrayList<L> ls = new ArrayList<L>();
		for (int i = 0; i < rects.length; i++) {
			for (int n = 0; n < rects.length; n++) {
				if (i != n) {
					Rect4i a = rects[i];
					Rect4i b = rects[n];
					if (a.intersects(b)) {
						int y1 = a.y + a.h;
						int y2 = b.y + b.h;
						boolean change = y1 < y2;
						ls.add(new L(change ? b : a, change ? a : b, change ? (b.x < a.x) : (a.x < b.x)));
					}
				}
			}
		}

		return ls.toArray(new L[ls.size()]);
	}

	private Rect4i horiz;
	private Rect4i vert;
	private boolean side;

	/**
	 * 
	 * @param horiz
	 *            - The horizontally oriented rectangle
	 * @param vert
	 *            - The vertically oriented rectangle
	 * @param side
	 *            - <b>true</b> if left L, <b>false</b> if right L
	 */
	public L(Rect4i horiz, Rect4i vert, boolean side) {
		this.horiz = horiz;
		this.vert = vert;
		this.side = side;
	}

	/**
	 * In image coordinates
	 */
	public Vector2d getInsideCorner() {
		return new Vector2d(side ? (vert.x) : (vert.x + vert.w), horiz.y + horiz.h);
	}

	/**
	 * In image coordinates
	 */
	public Vector2d getObtuseCorner() {
		return new Vector2d(side ? (vert.x + vert.w) : (vert.x), horiz.y);
	}

	public boolean leftSide() {
		return side;
	}

	public boolean rightSide() {
		return !side;
	}

	public Rect4i getBounds() {
		return new Rect4i(Math.min(horiz.x, vert.x), Math.min(horiz.y, vert.y), Math.max(horiz.w, vert.w), Math.max(
				horiz.h, vert.h));
	}

}
