package org.usfirst.frc.team4737.robot.vision.components;

import java.awt.Rectangle;

public class Rect4i {

	public int x;
	public int y;
	public int w;
	public int h;

	public Rect4i(int x, int y, int width, int height) {
		if (width < 0) {
			x -= width; width = -width;
		}
		if (height < 0) {
			y -= height; height = -height;
		}
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
	}
	
	public boolean intersects(Rect4i b) {
		return new Rectangle(x, y, w, h).intersects(new Rectangle(b.x, b.y, b.w, b.h));
	}

}
