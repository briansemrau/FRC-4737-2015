package org.usfirst.frc.team4737.robot.control;

import org.usfirst.frc.team4737.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.RGBImage;

public class TestController extends AbstractController {

	private Joystick joy;
//	private PWM talontest;

	public TestController(Robot robot) {
		super(robot);
		joy = new Joystick(1);
//		talontest = new PWM(0);
	}

	@SuppressWarnings("null")
	@Override
	public void periodicUpdate(Robot robot) {
		super.periodicUpdate(robot);
//		if (joy.getTrigger())
//			talontest.set(0.5);
//		else
//			talontest.set(0);
		if (joy.getTrigger()) {
			RGBImage image = null;
			robot.camera.getImage(image);
			try {
				image.write("IMAGE.png");
				image.free();
			} catch (NIVisionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
