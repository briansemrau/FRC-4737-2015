package org.usfirst.frc.team4737.robot.subAssemblies;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Log;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.BasicMotionController;
import org.usfirst.frc.team4737.robot.math.Vector2d;
import org.usfirst.frc.team4737.robot.wrappers.Dashboard;
import org.usfirst.frc.team4737.robot.wrappers.DoubleSolenoid;
import org.usfirst.frc.team4737.robot.wrappers.Motor;

import edu.wpi.first.wpilibj.DigitalInput;

public class Lift {

	private Motor leftMotor;
	private Motor rightMotor;
	// private Encoder leftEncoder;
	// private Encoder rightEncoder;

	private DigitalInput topBumper;
	private DigitalInput bottomBumper;

	private DoubleSolenoid solenoid;

	public BasicMotionController leftControl;
	public BasicMotionController rightControl;

	// private double prevVelL;
	// private double prevVelR;

	private int direction;

	public Lift(Robot robot) {
		Log.println("\tRunning left lift motor on CAN ID " + Global.LIFT_MOTOR_LEFT);
		leftMotor = new Motor(Global.LIFT_MOTOR_LEFT, true);
		Log.println("\tRunning right lift motor on CAN ID " + Global.LIFT_MOTOR_RIGHT);
		rightMotor = new Motor(Global.LIFT_MOTOR_RIGHT, false);

		// Log.println("\tRunning left lift encoder on digital " + Global.LIFT_ENCODER_LEFTA + " A, "
		// + Global.LIFT_ENCODER_LEFTB + " B");
		// leftEncoder = new Encoder(Global.LIFT_ENCODER_LEFTA, Global.LIFT_ENCODER_LEFTB);
		// leftEncoder.setDistancePerPulse(Global.LIFT_ENCODER_DPP);
		// Log.println("\tRunning right lift encoder on digital " + Global.LIFT_ENCODER_RIGHTA + " A, "
		// + Global.LIFT_ENCODER_RIGHTB + " B");
		// rightEncoder = new Encoder(Global.LIFT_ENCODER_RIGHTA, Global.LIFT_ENCODER_RIGHTB);
		// rightEncoder.setDistancePerPulse(Global.LIFT_ENCODER_DPP);

		Log.println("\tRunning top bumper switch on digital " + Global.LIFT_BUMPER_TOP);
		topBumper = new DigitalInput(Global.LIFT_BUMPER_TOP);
		Log.println("\tRunning bottom bumper switch on digital " + Global.LIFT_BUMPER_BOTTOM);
		bottomBumper = new DigitalInput(Global.LIFT_BUMPER_BOTTOM);

		solenoid = new DoubleSolenoid(Global.SOLENOID_FORWARD, Global.SOLENOID_BACKWARD);

		// leftControl = new MotionController("liftLcontrol", Global.LIFT_kP, Global.LIFT_kI, 0, Global.LIFT_MAX_ACCEL,
		// Global.LIFT_MAX_VEL, Global.LIFT_MAX_DECEL, Global.LIFT_IRANGE, null);
		// rightControl = new MotionController("liftRcontrol", Global.LIFT_kP, Global.LIFT_kI, 0, Global.LIFT_MAX_ACCEL,
		// Global.LIFT_MAX_VEL, Global.LIFT_MAX_DECEL, Global.LIFT_IRANGE, null);
		leftControl = new BasicMotionController(0.9, 1);
		rightControl = new BasicMotionController(0.9, 1);
	}

	public void periodicUpdate(Robot robot) {
		// Variables
		// double heightL = leftEncoder.getDistance();
		// double heightR = rightEncoder.getDistance();
		// double speedL = leftEncoder.getRate();
		// double speedR = rightEncoder.getRate();
		// double accelL = (speedL - prevVelL) / robot.deltaTime;
		// double accelR = (speedR - prevVelR) / robot.deltaTime;

		// double leftPower = leftControl.getPower(heightL, accelL, speedL, robot.deltaTime);
		// double rightPower = rightControl.getPower(heightR, accelR, speedR, robot.deltaTime);
		double leftPower = leftControl.getValue(direction, robot.deltaTime);
		double rightPower = rightControl.getValue(direction, robot.deltaTime);

		if (topBumper.get() || bottomBumper.get()) {
			// leftControl.value = rightControl.value = 0;
		}

		Vector2d power = new Vector2d(leftPower, rightPower);// .scaled(1);
		Dashboard.putString(Dashboard.DB_STRING_3, "L " + power.x);
		Dashboard.putString(Dashboard.DB_STRING_4, "R " + power.y);
		if (power.x < Global.LIFT_MAX_NEG_SPEED)
			power.x = power.y = Global.LIFT_MAX_NEG_SPEED;
		leftMotor.set(power.x);
		rightMotor.set(power.y);

		// Rewrite values
		// prevVelL = speedL;
		// prevVelR = speedR;
	}

	// public void setGoalHeight(double height) {
	// double val = Math.max(Math.min(height, Global.LIFT_MAX_HEIGHT), Global.LIFT_MIN_HEIGHT);
	// leftControl.setGoal(val);
	// rightControl.setGoal(val);
	// }

	public void startUpMotion() {
		if (topBumper.get() && false) {
			killMotion();
		} else {
			direction = 1;
		}
	}

	public void startDownMotion() {
		if (bottomBumper.get() && false) {
			killMotion();
		} else {
			direction = -1;
		}
	}

	public void stopMotion() {
		direction = 0;
	}

	public void killMotion() {
		leftControl.value = rightControl.value = 0;
		direction = 0;
	}

	public void setMotionSpeed(double speed) {
		leftControl.maxValue = rightControl.maxValue = speed;
	}

	public void openArms() {
		solenoid.set(1);
	}

	public void closeArms() {
		solenoid.set(-1);
	}

	public void releaseArms() {
		solenoid.set(0);
	}

}
