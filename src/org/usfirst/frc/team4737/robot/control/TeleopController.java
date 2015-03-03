package org.usfirst.frc.team4737.robot.control;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.task.JoystickMovement;
import org.usfirst.frc.team4737.robot.wrappers.Dashboard;

public class TeleopController extends AbstractController {

	private JoystickMovement joymovement;

	public TeleopController(Robot robot) {
		super(robot);

		joymovement = new JoystickMovement(robot.primaryJoystick, robot.secondaryJoystick,
				JoystickMovement.Drive.ARCADE);
	}

	@Override
	public void periodicUpdate(Robot robot) {
		super.periodicUpdate(robot);

		joymovement.periodicExecution(robot, robot.deltaTime);

		if (robot.secondaryJoystick.getButton(1)) {
			robot.lift.openArms();
		} else if (robot.secondaryJoystick.getButton(3)) {
			robot.lift.closeArms();
		}

		double yAxis = robot.secondaryJoystick.getY();
		if (robot.secondaryJoystick.getButton(2)) {
			robot.lift.killMotion();
		} else if (yAxis != 0) {
			robot.lift.setMotionSpeed(Math.abs(yAxis));
			if (yAxis > 0)
				robot.lift.startDownMotion();
			if (yAxis < 0)
				robot.lift.startUpMotion();
		} else {
			robot.lift.stopMotion();
		}
		
		robot.lift.periodicUpdate(robot);

	}

	public void reset() {
		joymovement.reset();
	}

}
