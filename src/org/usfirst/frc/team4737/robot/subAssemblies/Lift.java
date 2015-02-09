package org.usfirst.frc.team4737.robot.subAssemblies;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Log;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.MotionController;
import org.usfirst.frc.team4737.robot.math.Vector2d;
import org.usfirst.frc.team4737.robot.wrappers.Motor;

import edu.wpi.first.wpilibj.Encoder;

public class Lift {

	private Motor leftMotor;
	private Motor rightMotor;
	private Encoder leftEncoder;
	private Encoder rightEncoder;

	private MotionController leftControl;
	private MotionController rightControl;

	private double prevVelL;
	private double prevVelR;

	public Lift(Robot robot) {
		Log.println("\tRunning left lift motor on CAN ID " + Global.LIFT_MOTOR_LEFT);
		leftMotor = new Motor(Global.LIFT_MOTOR_LEFT, false);
		Log.println("\tRunning right lift motor on CAN ID " + Global.LIFT_MOTOR_RIGHT);
		rightMotor = new Motor(Global.LIFT_MOTOR_RIGHT, true);

		Log.println("\tRunning left lift encoder on digital " + Global.LIFT_ENCODER_LEFTA + " A, "
				+ Global.LIFT_ENCODER_LEFTB + " B");
		leftEncoder = new Encoder(Global.LIFT_ENCODER_LEFTA, Global.LIFT_ENCODER_LEFTB);
		leftEncoder.setDistancePerPulse(Global.LIFT_ENCODER_DPP);
		Log.println("\tRunning right lift encoder on digital " + Global.LIFT_ENCODER_RIGHTA + " A, "
				+ Global.LIFT_ENCODER_RIGHTB + " B");
		rightEncoder = new Encoder(Global.LIFT_ENCODER_RIGHTA, Global.LIFT_ENCODER_RIGHTB);
		rightEncoder.setDistancePerPulse(Global.LIFT_ENCODER_DPP);

		leftControl = new MotionController("liftLcontrol", Global.LIFT_kP, Global.LIFT_kI, 0, Global.LIFT_MAX_ACCEL,
				Global.LIFT_MAX_VEL, Global.LIFT_MAX_DECEL, Global.LIFT_IRANGE, null);
		rightControl = new MotionController("liftRcontrol", Global.LIFT_kP, Global.LIFT_kI, 0, Global.LIFT_MAX_ACCEL,
				Global.LIFT_MAX_VEL, Global.LIFT_MAX_DECEL, Global.LIFT_IRANGE, null);
	}

	public void periodicUpdate(Robot robot) {
		// Variables
		double heightL = leftEncoder.getDistance();
		double heightR = rightEncoder.getDistance();
		double speedL = leftEncoder.getRate();
		double speedR = rightEncoder.getRate();
		double accelL = (speedL - prevVelL) / robot.deltaTime;
		double accelR = (speedR - prevVelR) / robot.deltaTime;

		double leftPower = leftControl.getPower(heightL, accelL, speedL, robot.deltaTime);
		double rightPower = rightControl.getPower(heightR, accelR, speedR, robot.deltaTime);

		Vector2d power = new Vector2d(leftPower, rightPower).scaled(1);
		leftMotor.set(power.x);
		rightMotor.set(power.y);

		// Rewrite values
		prevVelL = speedL;
		prevVelR = speedR;
	}

	public void setGoalHeight(double height) {
		double val = Math.max(Math.min(height, Global.LIFT_MAX_HEIGHT), Global.LIFT_MIN_HEIGHT);
		leftControl.setGoal(val);
		rightControl.setGoal(val);
	}

}
