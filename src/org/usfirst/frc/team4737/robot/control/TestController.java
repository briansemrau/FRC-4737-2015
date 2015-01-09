package org.usfirst.frc.team4737.robot.control;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.wrappers.PWM;

import edu.wpi.first.wpilibj.Joystick;

public class TestController extends AbstractController {

	private Joystick joy;
	private PWM talontest;

	public TestController(Robot robot) {
		super(robot);
		joy = new Joystick(1);
		talontest = new PWM(0);
	}

	@Override
	public void periodicUpdate(Robot robot) {
		super.periodicUpdate(robot);
		if (joy.getTrigger())
			talontest.set(0.5);
		else
			talontest.set(0);
	}

}
