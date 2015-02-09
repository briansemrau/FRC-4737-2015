package org.usfirst.frc.team4737.robot.control;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.task.JoystickMovement;

import edu.wpi.first.wpilibj.Joystick;

public class TeleopController extends AbstractController {

	private Joystick primaryJoystick;
	private Joystick secondaryJoystick;

	private JoystickMovement joymovement;

	public TeleopController(Robot robot) {
		super(robot);
		primaryJoystick = new Joystick(Global.JOYSTICK_1_USB);
		secondaryJoystick = new Joystick(Global.JOYSTICK_2_USB);

		joymovement = new JoystickMovement(primaryJoystick, secondaryJoystick, JoystickMovement.Drive.ARCADE);
	}

	@Override
	public void periodicUpdate(Robot robot) {
		super.periodicUpdate(robot);

		joymovement.periodicExecution(robot, robot.deltaTime);

		if (secondaryJoystick.getTrigger()) {
			// TODO implement high-powered laser code
		}

	}

}
