package org.usfirst.frc.team4737.robot;

import org.usfirst.frc.team4737.robot.control.AbstractController;
import org.usfirst.frc.team4737.robot.control.AutonomousController;
import org.usfirst.frc.team4737.robot.control.Dependencies;
import org.usfirst.frc.team4737.robot.control.TeleopController;
import org.usfirst.frc.team4737.robot.control.TestController;
import org.usfirst.frc.team4737.robot.data.DataRecorder;
import org.usfirst.frc.team4737.robot.math.Positioner;
import org.usfirst.frc.team4737.robot.subAssemblies.Lift;
import org.usfirst.frc.team4737.robot.subAssemblies.MotorGroup;
import org.usfirst.frc.team4737.robot.vision.Vision;
import org.usfirst.frc.team4737.robot.wrappers.Motor;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as
 * described in the IterativeRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot {

	// Controllers

	public AbstractController teleop;
	public AbstractController autonomous;
	public AbstractController test;

	// Controller Assistants

	public Vision vision;

	// Sensors and Actuators

	public AxisCamera camera;

	public Motor driveMotorFrontLeft;
	public Motor driveMotorRearLeft;
	public Motor driveMotorFrontRight;
	public Motor driveMotorRearRight;
	
	public Gyro gyroscope;
	public BuiltInAccelerometer accelerometer;

	public MotorGroup leftDriveMotors;
	public MotorGroup rightDriveMotors;

	public Lift lift;

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

	private double sinceLastLogSave;

	public Positioner positioner;

	/**
	 * This function is run when the robot is first started up and should be used for any initialization code.
	 */
	public void robotInit() {
		Log.println("Robot Code Version " + Global.VERSION);
		Log.println("");
		Log.println("Initializing Robot...");

		Log.println("Initializing controllers...");
		teleop = new TeleopController(this);
		autonomous = new AutonomousController(this);
		test = new TestController(this);

		Log.println("Initializing sensors and actuators...");
		Log.println("\tUsing camera on port " + Global.CAMERA_IP);
		camera = new AxisCamera(Global.CAMERA_IP);
		vision = new Vision();

		Log.println("\tRunning front left drive motor on CAN ID " + Global.DRIVEMOTOR_FL);
		driveMotorFrontLeft = new Motor(Global.DRIVEMOTOR_FL, false);
		Log.println("\tRunning rear left drive motor on CAN ID " + Global.DRIVEMOTOR_RL);
		driveMotorRearLeft = new Motor(Global.DRIVEMOTOR_RL, false);
		Log.println("\tRunning front right drive motor on CAN ID " + Global.DRIVEMOTOR_FR);
		driveMotorFrontRight = new Motor(Global.DRIVEMOTOR_FR, false);
		Log.println("\tRunning rear right drive motor on CAN ID " + Global.DRIVEMOTOR_RR);
		driveMotorRearRight = new Motor(Global.DRIVEMOTOR_RR, false);

		leftDriveMotors = new MotorGroup(driveMotorFrontLeft, driveMotorRearLeft);
		rightDriveMotors = new MotorGroup(driveMotorFrontRight, driveMotorRearRight);

		try {
			gyroscope = new Gyro(Global.GYROSCOPE);
			gyroscope.initGyro(); // TODO: add some button to do this maybe
			Log.println("\tUsing gyro on analog " + Global.GYROSCOPE);
		} catch (AllocationException e) {
			Dependencies.GYROSCOPE.disable();
			Log.println("\tGyroscope not found, should be in analog " + Global.GYROSCOPE);
		}

		accelerometer = new BuiltInAccelerometer();

		timeLast = System.nanoTime() / 1000000000.0;
		timeCurrent = System.nanoTime() / 1000000000.0;

		positioner = new Positioner(this);

		// Log.println("\tLOST DEPENDENCIES: " + ); // TODO

		Log.println("Finished robot initialization.");
	}

	public void autonomousInit() {
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		commonPeriodic();
		autonomous.periodicUpdate(this);
	}

	public void teleopInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		commonPeriodic();
		teleop.periodicUpdate(this);
	}

	public void testInit() {
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		commonPeriodic();
		test.periodicUpdate(this);
	}

	public void commonPeriodic() {
		// Handle vision
		// vision.update(camera);

		// Handle time
		timeLast = timeCurrent;
		timeCurrent = System.nanoTime() / 1000000000.0;
		deltaTime = timeCurrent - timeLast;

		// Handle log
		sinceLastLogSave += deltaTime;
		if (sinceLastLogSave >= Global.LOG_AUTOSAVE_PERIOD) {
			sinceLastLogSave = 0;
			Log.saveLog();
			DataRecorder.saveDataLog();
		}

		positioner.update(this, deltaTime);

		// Data mining
		recordData();
	}

	/**
	 * Records a bunch of data
	 */
	private void recordData() {
		double startTime = System.nanoTime() / 1000000000.0;

		// Include ALL measurable values in here

		DataRecorder.record("time (s)", startTime);
		DataRecorder.record("voltage", DriverStation.getInstance().getBatteryVoltage());
		DataRecorder.record("gyroRX", positioner.gyroAngle.x);
		DataRecorder.record("gyroRY", positioner.gyroAngle.y);
		DataRecorder.record("gyroRZ", positioner.gyroAngle.z);
		DataRecorder.record("accelerationX", positioner.globalAcceleration.x);
		DataRecorder.record("accelerationY", positioner.globalAcceleration.y);
		DataRecorder.record("accelerationZ", positioner.globalAcceleration.z);
		DataRecorder.record("velocityX", positioner.velocity.x);
		DataRecorder.record("velocityY", positioner.velocity.y);
		DataRecorder.record("velocityZ", positioner.velocity.z);
		DataRecorder.record("positionX", positioner.position.x);
		DataRecorder.record("positionY", positioner.position.y);
		DataRecorder.record("positionZ", positioner.position.z);

		double endTime = System.nanoTime() / 1000000000.0;

		DataRecorder.record("gatherTimePeriod", endTime - startTime);
	}
}
