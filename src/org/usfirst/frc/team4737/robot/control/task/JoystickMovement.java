package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.BasicMotionController;
import org.usfirst.frc.team4737.robot.math.Vector2d;
import org.usfirst.frc.team4737.robot.wrappers.Dashboard;
import org.usfirst.frc.team4737.robot.wrappers.Joystick;

/**
 * The task for joystick control of the robot.<br>
 * <br>
 * 
 * @author Brian
 *
 */
public class JoystickMovement {

	public enum Drive {
		TANK, ARCADE, MECANUM
	}

	private Drive mode;
	private Joystick joystick1;
	private Joystick joystick2;

	// private MotionController forward;
	// private MotionController angular;
	private BasicMotionController forward;
	private BasicMotionController angular;

	/**
	 * 
	 * @param primary
	 *            - The primary joystick. Used in Arcade mode. Controls left drive and speed scale (Z axis) in Tank
	 *            mode.<br>
	 * <br>
	 * @param secondary
	 *            - The secondary joystick. Not used in Arcade mode. Controls right drive in Tank mode.<br>
	 * <br>
	 * @param mode
	 *            - The control mode: Tank or Arcade.
	 */
	public JoystickMovement(Joystick primary, Joystick secondary, Drive mode) {
		joystick1 = primary;
		joystick2 = secondary;
		// forward = new MotionController("joysticklinear", 0, 0, 0, 0, 0, 0, 0, null);
		// angular = new MotionController("joystickangular", 0, 0, 0, 0, 0, 0, 0, null);
		forward = new BasicMotionController(1, 1);
		angular = new BasicMotionController(1, 1);
		this.mode = mode;
	}

	public void periodicExecution(Robot robot, double delta) {

		// forward.kP = Dashboard.getSlider(Dashboard.DB_SLIDER_0);
		// forward.accelLimit = Dashboard.getSlider(Dashboard.DB_SLIDER_1);
		// forward.decelLimit = Dashboard.getSlider(Dashboard.DB_SLIDER_2);
		// forward.velLimit = Dashboard.getSlider(Dashboard.DB_SLIDER_3);
//		forward.maxDelta = Dashboard.getSlider(Dashboard.DB_SLIDER_0);
//		forward.maxValue = Dashboard.getSlider(Dashboard.DB_SLIDER_1);
//		angular.maxDelta = Dashboard.getSlider(Dashboard.DB_SLIDER_0);
//		angular.maxValue = Dashboard.getSlider(Dashboard.DB_SLIDER_1);

		if (mode == Drive.ARCADE) {
			double xAxis = joystick1.getX(); // Rotational axis
			double yAxis = joystick1.getY(); // Magnitude axis
			double zAxis = (joystick1.getZ() + 1) / 2.0; // Scale axis

			if (!Global.USE_RAW_DRIVING) {
				// forward.setGoal(0);
				// angular.setGoal(0);
				
				forward.maxValue = Math.max(Math.abs(yAxis) * 2, Math.abs(xAxis));
				angular.maxValue = forward.maxValue * 0.5;
				
				int direction = yAxis == 0 ? 0 : yAxis < 0 ? -1 : 1;
				double linearPower = forward.getValue(direction, delta);
				// forward.getPower(yAxis, robot.positioner.localAcceleration.x, robot.positioner.velocity.magnitude(),
				// delta);
				int direction2 = xAxis == 0 ? 0 : xAxis < 0 ? -1 : 1;
				double angularPower = angular.getValue(direction2, delta);
				// angular.getPower(xAxis * Global.ARCADE_YAW_SENSITIVITY, robot.positioner.angularAccel.z,
				// robot.positioner.angularSpeed.z, delta);

				double leftSpeed = angularPower * zAxis + linearPower * -zAxis;
				double rightSpeed = -angularPower * zAxis + linearPower * -zAxis;

				Vector2d power = new Vector2d(leftSpeed, rightSpeed).scaled(1);
//				Dashboard.putString(Dashboard.DB_STRING_3, "" + power.x);
//				Dashboard.putString(Dashboard.DB_STRING_4, "" + power.y);
				robot.leftDriveMotors.set(power.x);// Math.max(Math.min(power.x, 0.1), -0.1));
				robot.rightDriveMotors.set(power.y);// Math.max(Math.min(power.y, 0.1), -0.1));

			} else {
				double leftSpeed = Math.min(Math.abs(-1 / Global.ARCADE_YAW_SENSITIVITY - xAxis)
						/ (1 / Global.ARCADE_YAW_SENSITIVITY), 2) - 1;
				double rightSpeed = Math.min(Math.abs(1 / Global.ARCADE_YAW_SENSITIVITY - xAxis)
						/ (1 / Global.ARCADE_YAW_SENSITIVITY), 2) - 1;

				Vector2d power = new Vector2d(leftSpeed * zAxis + yAxis * -zAxis, rightSpeed * zAxis + yAxis * -zAxis)
						.scaled(1);
				robot.leftDriveMotors.set(power.x);
				robot.rightDriveMotors.set(power.y);

			}

		} else if (mode == Drive.TANK) {
			double yAxis1 = joystick1.getY();
			double yAxis2 = joystick2.getY();
			double zAxis = joystick1.getZ();

			robot.leftDriveMotors.set(yAxis1 * zAxis);
			robot.rightDriveMotors.set(yAxis2 * zAxis);
		} else if (mode == Drive.MECANUM) {
			// TODO
		}
	}

	public void reset() {
		forward.value = 0;
		angular.value = 0;
	}

}
