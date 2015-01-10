package org.usfirst.frc.team4737.robot.math;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.Robot;

/**
 * Handles all the measured and calculated positioning of the robot
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

	public Positioner(Robot robot) {
		localAcceleration = new Vector3d();
		globalAcceleration = new Vector3d();
		prevGlobalAcceleration = new Vector3d();
		avgAcceleration = new Vector3d();
		
		gyroAngle = new Vector3d();
		prevGyroAngle = new Vector3d();
		avgGyroAngle = new Vector3d();
		
		velocity = new Vector3d();
		prevVelocity = new Vector3d();
		avgVelocity = new Vector3d();
		
		position = new Vector3d();
		prevPosition = new Vector3d();
		avgPosition = new Vector3d();
	}

	public void update(Robot robot, double deltaTime) {
		// Re-record previous values
		prevGlobalAcceleration = globalAcceleration.clone();
		prevGyroAngle = gyroAngle.clone();
		prevVelocity = velocity.clone();
		prevPosition = position.clone();

		// Measure acceleration. The accelerometer returns values in Gs
		localAcceleration.x = robot.accelerometer.getX() / Global.GRAVITY;
		localAcceleration.y = robot.accelerometer.getY() / Global.GRAVITY;
		localAcceleration.z = robot.accelerometer.getZ() / Global.GRAVITY;

		// Measure angle using gyroscope
		gyroAngle.z = robot.gyroscope.getAngle();
		
		// Calculate average gyroscope value
		avgGyroAngle = gyroAngle.average(prevGyroAngle);

		// Calculate global acceleration
		globalAcceleration = localAcceleration.clone();
		globalAcceleration.rotate(avgGyroAngle.x, avgGyroAngle.y, avgGyroAngle.z);

		// Integrate velocity and position
		velocity.add(globalAcceleration.integral(deltaTime));
		position.add(velocity.integral(deltaTime));

		// Calculate average positioning
		avgAcceleration = globalAcceleration.average(prevGlobalAcceleration);
		avgVelocity = velocity.average(prevVelocity);
		avgPosition = position.average(prevPosition);
	}

}
