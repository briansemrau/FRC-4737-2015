package org.usfirst.frc.team4737.robot.test;

import org.usfirst.frc.team4737.robot.Global;
import org.usfirst.frc.team4737.robot.vision.Vision;
import org.usfirst.frc.team4737.test.control.AbstractController;
import org.usfirst.frc.team4737.test.control.TestController;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// Controllers

	public AbstractController test;
	
	public AbstractController current;

	// Controller Assistants
	
	public Vision vision;

	// Sensors and Actuators

	public AxisCamera camera;

//	public BuiltInAccelerometer accelerometer;

	// Values

	/**
	 * The time (in seconds) of the last periodic update.
	 */
	private double timeLast;
	/**
	 * The current time (in seconds) measured most recently.
	 */
	private double timeCurrent;
	/**
	 * The time passed since the last periodic update.
	 */
	public double deltaTime;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		System.out.println("Initializing Robot...");
		
		System.out.println("Initializing controllers...");
		test = new TestController(this);
		
		camera = new AxisCamera(Global.CAMERA_IP);
		vision = new Vision();

//		accelerometer = new BuiltInAccelerometer();

		timeLast = System.nanoTime() / 1000000000.0;
		timeCurrent = System.nanoTime() / 1000000000.0;

		System.out.println("Initialized robot.");
	}

	public void autonomousInit() {
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
	}

	public void teleopInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
	}

	public void testInit() {
		current = test;
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		commonPeriodic();
		test.periodicUpdate(this);
	}
	
	public void disabledInit() {
	}

	/**
	 * This function is called periodically while disabled
	 */
	public void disabledPeriodic() {
	}

	public void commonPeriodic() {
		vision.update(camera);
		
		// Handle time
		timeLast = timeCurrent;
		timeCurrent = System.nanoTime() / 1000000000.0;
		deltaTime = timeCurrent - timeLast;
	}

}
