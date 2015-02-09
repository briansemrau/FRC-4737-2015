package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.Dependencies;
import org.usfirst.frc.team4737.robot.control.MotionController;
import org.usfirst.frc.team4737.robot.math.Vector2d;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

/**
 * The task for joystick control of the robot.<br>
 * <br>
 * 
 * @author Brian
 *
 */
public class JoystickMovement {

	public enum Drive {
		TANK, ARCADE
	}

	private Drive mode;
	private Joystick joystick1;
	private Joystick joystick2;

	private MotionController forward;
	private MotionController angular;

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
		forward = new MotionController("joysticklinear", 0.1, 0, 2, 10, 2, 0.05, 0, null);
		angular = new MotionController("joystickangular", 0.1, 0, 2, 10, 2, 0.05, 0, null);
	}

	public void periodicExecution(Robot robot, double delta) {
		if (mode == Drive.ARCADE) {
			double xAxis = joystick1.getAxis(AxisType.kX); // Rotational axis
			double yAxis = joystick1.getAxis(AxisType.kY); // Magnitude axis
			double zAxis = joystick1.getAxis(AxisType.kZ); // Scale axis

			if (Dependencies.GYROSCOPE.enabled()) {

				forward.setGoal(0);
				angular.setGoal(0);
				
				double linearPower = forward.getPower(yAxis, robot.positioner.localAcceleration.y,
						robot.positioner.velocity.magnitude(), delta);
				double angularPower = angular.getPower(xAxis * Global.ARCADE_YAW_SENSITIVITY,
						robot.positioner.angularAccel.z, robot.positioner.angularSpeed.z, delta);
				
				double leftSpeedMod = Math.min(Math.abs(-1 - angularPower), 1) * linearPower * zAxis;
				double rightSpeedMod = Math.min(Math.abs(1 - angularPower), 1) * linearPower * zAxis;
				
				Vector2d power = new Vector2d(leftSpeedMod, rightSpeedMod).scaled(1);
				robot.leftDriveMotors.set(power.x);
				robot.rightDriveMotors.set(power.y);

			} else {

				double leftSpeedMod = Math.min(Math.abs(-1 / Global.ARCADE_YAW_SENSITIVITY - xAxis)
						/ (1 / Global.ARCADE_YAW_SENSITIVITY), 1);
				double rightSpeedMod = Math.min(Math.abs(1 / Global.ARCADE_YAW_SENSITIVITY - xAxis)
						/ (1 / Global.ARCADE_YAW_SENSITIVITY), 1);

				robot.leftDriveMotors.set(leftSpeedMod * yAxis * zAxis);
				robot.rightDriveMotors.set(rightSpeedMod * yAxis * zAxis);

			}

		} else if (mode == Drive.TANK) {
			double yAxis1 = joystick1.getAxis(AxisType.kY);
			double yAxis2 = joystick2.getAxis(AxisType.kY);
			double zAxis = joystick1.getAxis(AxisType.kZ);

			robot.leftDriveMotors.set(yAxis1 * zAxis);
			robot.rightDriveMotors.set(yAxis2 * zAxis);
		}
	}

}
