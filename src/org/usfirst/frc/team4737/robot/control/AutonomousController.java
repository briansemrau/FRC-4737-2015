package org.usfirst.frc.team4737.robot.control;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.wrappers.Dashboard;

public class AutonomousController extends AbstractController {

	private int state;

	private double time;

	BasicMotionController forward;

	public AutonomousController(Robot robot) {
		super(robot);
		state = 0;
		forward = new BasicMotionController(.75, .25);
	}

	@Override
	public void periodicUpdate(Robot robot) {
		super.periodicUpdate(robot);

		time += robot.deltaTime;

		// if (Dashboard.getButton(Dashboard.DB_BUTTON_2, false))
		if (time < 3.5) {
			double power = forward.getValue(1, robot.deltaTime);
			robot.leftDriveMotors.set(power);
			robot.rightDriveMotors.set(power);
		} else {
			double power = forward.getValue(0, robot.deltaTime);
			robot.leftDriveMotors.set(power);
			robot.rightDriveMotors.set(power);
		}

		// Time to score all the points

		// 1. Pick up bin
		// 2. Lift bin
		// 3. Move forward
		// 4. Drop bin
		// 5. Pick up tote
		// 6. Turn left
		// 7. Move forward
		// 8. Drop stack

		// switch (state) {
		// case 0:
		// state = 1;
		// break;
		// case 1:
		// robot.lift.closeArms();
		// break;
		// case 2:
		// if (time > 0.5)
		// break;
		// case 3:
		// break;
		// case 4:
		// break;
		// case 5:
		// break;
		// case 6:
		// break;
		// case 7:
		// break;
		// case 8:
		// break;
		// }

	}

	@Override
	public void reset() {
		state = 0;
		forward = new BasicMotionController(1, 1);
	}

}
