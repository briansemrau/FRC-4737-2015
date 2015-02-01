package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.MotionController;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;

/**
 * The task for joystick control of the robot.<br>
 * <br>
 * This class is an example for how tasks should be implemented.
 * 
 * @author Brian
 *
 */
public class JoystickMovementTask extends AbstractRobotTask {

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
	public JoystickMovementTask(Joystick primary, Joystick secondary, Drive mode) {
		super("JoystickMovement", false);
		joystick1 = primary;
		joystick2 = secondary;
		forward = new MotionController(0.1, 0, 2, 10, 2, 0.05, 0, null);
		angular = new MotionController(0.1, 0, 2, 10, 2, 0.05, 0, null);
	}

	public void periodicExecution(Robot robot) {
		super.periodicExecution(robot);

		if (mode == Drive.ARCADE) {
			double xAxis = joystick1.getAxis(AxisType.kX); // Rotational axis
			double yAxis = joystick1.getAxis(AxisType.kY); // Magnitude axis
			double zAxis = joystick1.getAxis(AxisType.kZ); // Scale axis

//			double leftSpeedMod = Math.min(Math.abs(-1 / Global.ARCADE_YAW_SENSITIVITY - xAxis)
//					/ (1 / Global.ARCADE_YAW_SENSITIVITY), 1);
//			double rightSpeedMod = Math.min(Math.abs(1 / Global.ARCADE_YAW_SENSITIVITY - xAxis)
//					/ (1 / Global.ARCADE_YAW_SENSITIVITY), 1);

			forward.setGoal(0);
			angular.setGoal(0);
			double linearPower = forward.getPower(yAxis, robot.positioner.localAcceleration.y, robot.positioner.velocity.magnitude(), deltaTime());
			double angularPower = angular.getPower(xAxis * Global.ARCADE_YAW_SENSITIVITY, robot.positioner.angularAccel.z, robot.positioner.angularSpeed.z, deltaTime());
			
//			robot.leftDriveMotors.set(leftSpeedMod * yAxis * zAxis);
//			robot.rightDriveMotors.set(rightSpeedMod * yAxis * zAxis);

		} else if (mode == Drive.TANK) {
			double yAxis1 = joystick1.getAxis(AxisType.kY);
			double yAxis2 = joystick2.getAxis(AxisType.kY);
			double zAxis = joystick1.getAxis(AxisType.kZ);

			robot.leftDriveMotors.set(yAxis1 * zAxis);
			robot.rightDriveMotors.set(yAxis2 * zAxis);
		}
	}
	
	private void scale(double a, double b) {
		
	}

}
