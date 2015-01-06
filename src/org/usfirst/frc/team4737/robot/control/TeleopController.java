package org.usfirst.frc.team4737.robot.control;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.task.JoystickMovementTask;

import edu.wpi.first.wpilibj.Joystick;

public class TeleopController extends AbstractController {

	private Joystick primaryJoystick;
	private Joystick secondaryJoystick;
	
	public TeleopController(Robot robot) {
		primaryJoystick = new Joystick(Global.JOYSTICK_1_USB);
		secondaryJoystick = new Joystick(Global.JOYSTICK_2_USB);
		
		// Start up Arcade control
		super.startTask(new JoystickMovementTask(primaryJoystick, secondaryJoystick, JoystickMovementTask.Drive.ARCADE));
	}
	
	@Override
	public void periodicUpdate(Robot robot) {
		super.periodicUpdate(robot);
		
		if (secondaryJoystick.getTrigger()) {
			// TODO implement high-powered laser code
		}
		
	}

}
