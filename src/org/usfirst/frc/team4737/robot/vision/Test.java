package org.usfirst.frc.team4737.robot.vision;

import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.RGBImage;

public class Test {

	public static void main(String[] args) {
		Vision vision = new Vision();
		try {
			ColorImage image = new RGBImage("/ir.png");
			vision.testRects(image);
			image.write("/OUTPUT.png");
			image.free();
		} catch (NIVisionException e) {
			e.printStackTrace();
		}
	}

}
