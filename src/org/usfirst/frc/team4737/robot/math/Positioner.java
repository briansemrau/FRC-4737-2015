package org.usfirst.frc.team4737.robot.math;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Log;
import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.wrappers.Dashboard;

/**
 * Handles all the measured and calculated positioning of the robot.
 * 
 * @author Brian
 *
 */
public class Positioner {
	
	/**
	 * The calculated local-axis acceleration of the robot.
	 */
	public Vector3d localAcceleration;
	/**
	 * The measured acceleration of the robot.
	 */
	public Vector3d globalAcceleration;
	private Vector3d prevGlobalAcceleration;
	/**
	 * The mean of the current and most previously recorded global acceleration.
	 */
	public Vector3d avgAcceleration;
	/**
	 * The measured value from the gyroscope(s).
	 */
	public Vector3d gyroAngle;
	private Vector3d prevGyroAngle;
	/**
	 * The mean of the current and most previously recorded gyroscope angles.
	 */
	public Vector3d avgGyroAngle;
	/**
	 * The calculated current angular speed of the robot.
	 */
	public Vector3d angularSpeed;
	private Vector3d prevAngularSpeed;
	/**
	 * The calculated current angular acceleration of the robot.
	 */
	public Vector3d angularAccel;
	/**
	 * The calculated velocity of the robot.
	 */
	public Vector3d velocity;
	private Vector3d prevVelocity;
	/**
	 * The mean of the current and most previously calculated velocity;
	 */
	public Vector3d avgVelocity;
	/**
	 * The calculated position of the robot.
	 */
	public Vector3d position;
	private Vector3d prevPosition;
	/**
	 * The mean of the current and most previously calculated position.
	 */
	public Vector3d avgPosition;

	private Vector3d localAccelCalibration;
	private Vector3d gyroCalibration;
	private int measurements;
	private double calibrationTimeLeft;
	private double calibrationTime = 6;

	public Positioner(Robot robot) {
		localAcceleration = new Vector3d();
		globalAcceleration = new Vector3d();
		prevGlobalAcceleration = new Vector3d();
		avgAcceleration = new Vector3d();

		gyroAngle = new Vector3d();
		prevGyroAngle = new Vector3d();
		avgGyroAngle = new Vector3d();

		angularSpeed = new Vector3d();
		prevAngularSpeed = new Vector3d();
		angularAccel = new Vector3d();

		velocity = new Vector3d();
		prevVelocity = new Vector3d();
		avgVelocity = new Vector3d();

		position = new Vector3d();
		prevPosition = new Vector3d();
		avgPosition = new Vector3d();

		localAccelCalibration = new Vector3d();
		gyroCalibration = new Vector3d();
	}

	/**
	 * The periodic update to be run every iteration of the robot periodic code.
	 * 
	 * @param robot
	 *            - The robot
	 * @param deltaTime
	 *            - The time since last update
	 */
	public void update(Robot robot, double deltaTime) {
		if (calibrationTimeLeft > 0) {
			calibrationTimeLeft -= deltaTime;
			localAccelCalibration.x += robot.accelerometer.getX() / Global.GRAVITY / deltaTime;
			localAccelCalibration.y += robot.accelerometer.getY() / Global.GRAVITY / deltaTime;
			localAccelCalibration.z += robot.accelerometer.getZ() / Global.GRAVITY / deltaTime; 
			gyroCalibration.z += robot.gyroscope.getAngle();
			measurements++;
			if (calibrationTimeLeft <= 0) {
				Log.println("Finished positioner calibration.");
				localAccelCalibration = localAccelCalibration.scaled(1.0 / measurements);
				gyroCalibration = gyroCalibration.scaled(1.0 / measurements);
				
			} else
				return;
		}

		// Re-record previous values
		prevGlobalAcceleration = globalAcceleration.clone();
		prevGyroAngle = gyroAngle.clone();
		prevVelocity = velocity.clone();
		prevPosition = position.clone();
		prevAngularSpeed = angularSpeed.clone();

		// Measure acceleration. The accelerometer returns values in Gs
		localAcceleration.x = robot.accelerometer.getX() / Global.GRAVITY;
		localAcceleration.y = robot.accelerometer.getY() / Global.GRAVITY;
		localAcceleration.z = robot.accelerometer.getZ() / Global.GRAVITY;
		localAcceleration.subtract(localAccelCalibration);
		localAcceleration.deadband(0.01);

		// Measure angle using gyroscope
		gyroAngle.z = robot.gyroscope.getAngle();
		gyroAngle.subtract(gyroCalibration);

		// Calculate angular values
		avgGyroAngle = gyroAngle.average(prevGyroAngle);

		angularSpeed = gyroAngle.minus(prevGyroAngle).scaled(1.0 / deltaTime);
		angularAccel = angularSpeed.minus(prevAngularSpeed).scaled(1.0 / deltaTime);

		// Calculate global acceleration
		globalAcceleration = localAcceleration.clone();
		globalAcceleration.rotate(avgGyroAngle.x, avgGyroAngle.y, avgGyroAngle.z);

		// Integrate velocity and position
		avgAcceleration = globalAcceleration.average(prevGlobalAcceleration);
		velocity.add(avgAcceleration.integral(deltaTime));
		avgVelocity = velocity.average(prevVelocity);
		position.add(avgVelocity.integral(deltaTime));
		avgPosition = position.average(prevPosition);
		
		double n = 100000.0;
		Dashboard.putString(Dashboard.DB_STRING_0, "PX" + (int) (n * position.x) / n);
		Dashboard.putString(Dashboard.DB_STRING_1, "PY" + (int) (n * position.y) / n);
		Dashboard.putString(Dashboard.DB_STRING_5, "VX" + (int) (n * velocity.x) / n);
		Dashboard.putString(Dashboard.DB_STRING_6, "VY" + (int) (n * velocity.y) / n);
		Dashboard.putString(Dashboard.DB_STRING_7, "AX" + (int) (n * localAcceleration.x) / n);
		Dashboard.putString(Dashboard.DB_STRING_8, "AY" + (int) (n * localAcceleration.y) / n);
	}

	public void startCalibration() {
		Log.println("Starting positioner calibration! Calibrating accelerometer and gyroscope.");
		localAccelCalibration = new Vector3d();
		gyroCalibration = new Vector3d();
		calibrationTimeLeft = calibrationTime;
	}

}
