package org.usfirst.frc.team4737.robot.vision.components;

public class LGroup {

	public static LGroup[] findGroups(L[] ls) {
		// TODO;
		return new LGroup[0];
	}

	private L left;
	private L right;

	private LGroup(L left, L right) {
		this.left = left;
		this.right = right;
	}

	public Rect4i getBounds() {
		int x = left.getBounds().x;
		int y = Math.min(left.getBounds().y, right.getBounds().y); // TODO deal with image vs. physical coordinates
		int width = right.getBounds().x + right.getBounds().w - left.getBounds().x;
		int height = Math.max(left.getBounds().y + left.getBounds().h - (right.getBounds().y), right.getBounds().y + right.getBounds().h - (left.getBounds().y)); // TODO deal with image vs. physical coordinates
		return new Rect4i(x, y, width, height);
	}
	
	public double getFarHeight() {
		return Math.min(left.vertical().h, right.vertical().h);
	}
	
	public double getCloseHeight() {
		return Math.min(left.vertical().h, right.vertical().h);
	}

}
