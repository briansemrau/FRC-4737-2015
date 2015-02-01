package org.usfirst.frc.team4737.robot.subAssemblies;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.wrappers.Motor;

import edu.wpi.first.wpilibj.Encoder;

public class Lift {

	private Motor leftMotor;
	private Motor rightMotor;
	private Encoder leftEncoder;
	private Encoder rightEncoder;

	private double goalHeight;

	private double prevHeightL;
	private double prevHeightR;

	private double integralLeft;
	private double integralRight;

	public Lift(Robot robot) {
		leftMotor = new Motor(Global.LIFT_MOTOR_LEFT, false);
		rightMotor = new Motor(Global.LIFT_MOTOR_RIGHT, true);

		leftEncoder = new Encoder(Global.LIFT_ENCODER_LEFTA,
				Global.LIFT_ENCODER_LEFTB);
		leftEncoder.setDistancePerPulse(Global.LIFT_ENCODER_DPP);
		rightEncoder = new Encoder(Global.LIFT_ENCODER_RIGHTA,
				Global.LIFT_ENCODER_RIGHTB);
		rightEncoder.setDistancePerPulse(Global.LIFT_ENCODER_DPP);

		goalHeight = Global.LIFT_MIN_HEIGHT;
		prevHeightL = goalHeight;
		prevHeightR = goalHeight;
	}

	public void periodicUpdate(Robot robot) {
		// Variables
		double heightL = leftEncoder.getDistance();
		double heightR = rightEncoder.getDistance();
		double speedL = (heightL - prevHeightL) / robot.deltaTime;
		double speedR = (heightR - prevHeightR) / robot.deltaTime;

		// Control handling
		double errorLeft = goalHeight - heightL;
		double errorRight = goalHeight - heightR;
		integralLeft += errorLeft * robot.deltaTime;
		integralRight += errorRight * robot.deltaTime;

		double projectedDiff = (heightL + speedL * robot.deltaTime)
				- (heightR + speedR * robot.deltaTime);
		if (Math.abs(projectedDiff) >= Global.LIFT_MAX_DIFFERENCE) {

		}

//		double leftPower = errorLeft * Global.LIFT_kP + integralLeft
//				* Global.LIFT_kI;
//		double rightPower = errorRight * Global.LIFT_kP + integralRight
//				* Global.LIFT_kI;
//
//		leftMotor.set(leftPower);
//		rightMotor.set(rightPower);

		// Rewrite values
		prevHeightL = heightL;
		prevHeightR = heightR;
	}

	public void setGoalHeight(double height) {
		goalHeight = Math.max(Math.min(height, Global.LIFT_MAX_HEIGHT),
				Global.LIFT_MIN_HEIGHT);
	}

}
