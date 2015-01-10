package org.usfirst.frc.team4737.robot;

import org.usfirst.frc.team4737.robot.control.AbstractController;
import org.usfirst.frc.team4737.robot.control.AutonomousController;
import org.usfirst.frc.team4737.robot.control.TeleopController;
import org.usfirst.frc.team4737.robot.control.TestController;
import org.usfirst.frc.team4737.robot.data.DataRecorder;
import org.usfirst.frc.team4737.robot.math.Positioner;
import org.usfirst.frc.team4737.robot.subAssemblies.Lift;
import org.usfirst.frc.team4737.robot.subAssemblies.MotorGroup;
import org.usfirst.frc.team4737.robot.wrappers.Motor;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
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

	public AbstractController teleop;
	public AbstractController autonomous;
	public AbstractController test;
	
	public AbstractController current;

	// Controller Assistants

//	public Map2 map;

	// Sensors and Actuators

	public AxisCamera camera;
	
	public Motor driveMotorFrontLeft;
	public Motor driveMotorRearLeft;
	public Motor driveMotorFrontRight;
	public Motor driveMotorRearRight;
	
	public Gyro gyroscope;
	public BuiltInAccelerometer accelerometer;
	public Ultrasonic usd;

	public DigitalOutput led1;

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
	
	public Positioner position;

	private double sinceLastLogSave;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		Log.println("Initializing Robot...");
		
		Log.println("Initializing controllers...");
		teleop = new TeleopController(this);
		autonomous = new AutonomousController(this);
		test = new TestController(this);
		
//		map = new Map2(500 * 1000);
		
		camera = new AxisCamera(Global.CAMERA_IP);

		driveMotorFrontLeft = new Motor(Global.DRIVEMOTOR_FL, false);
		driveMotorRearLeft = new Motor(Global.DRIVEMOTOR_RL, false);
		driveMotorFrontRight = new Motor(Global.DRIVEMOTOR_FR, false);
		driveMotorRearRight = new Motor(Global.DRIVEMOTOR_RR, false);

		leftDriveMotors = new MotorGroup(driveMotorFrontLeft,
				driveMotorRearLeft);
		rightDriveMotors = new MotorGroup(driveMotorFrontRight,
				driveMotorRearRight);

		gyroscope = new Gyro(Global.GYROSCOPE);
		gyroscope.initGyro(); // TODO: add some button to do this maybe
		accelerometer = new BuiltInAccelerometer();
		usd = new Ultrasonic(Global.USD_DIGITAL_IN, Global.USD_DIGITAL_OUT);

		led1 = new DigitalOutput(Global.LED_1);

		timeLast = System.nanoTime() / 1000000000.0;
		timeCurrent = System.nanoTime() / 1000000000.0;

		position = new Positioner(this);
	}

	public void autonomousInit() {
		current = autonomous;
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		commonPeriodic();
		autonomous.periodicUpdate(this);
	}

	public void teleopInit() {
		current = teleop;
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		commonPeriodic();
		teleop.periodicUpdate(this);
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
//		current = disable;
	}

	/**
	 * This function is called periodically while disabled
	 */
	public void disabledPeriodic() {
		commonPeriodic();
		test.periodicUpdate(this);
	}

	public void commonPeriodic() {
		// Handle time
		timeLast = timeCurrent;
		timeCurrent = System.nanoTime() / 1000000000.0;
		deltaTime = timeCurrent - timeLast;
		
		// Handle log
		sinceLastLogSave += deltaTime;
		if (sinceLastLogSave >= Global.LOG_AUTOSAVE_PERIOD) {
			sinceLastLogSave = 0;
			Log.saveLog();
		}

		position.update(this, deltaTime);

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
		DataRecorder.record("voltage", DriverStation.getInstance()
				.getBatteryVoltage());
		DataRecorder.record("gyroRX", position.gyroAngle.x);
		DataRecorder.record("gyroRY", position.gyroAngle.y);
		DataRecorder.record("gyroRZ", position.gyroAngle.z);
		DataRecorder.record("accelerationX (?)", position.globalAcceleration.x);
		DataRecorder.record("accelerationY (?)", position.globalAcceleration.y);
		DataRecorder.record("accelerationZ (?)", position.globalAcceleration.z);
		DataRecorder.record("velocityX", position.velocity.x);
		DataRecorder.record("velocityY", position.velocity.y);
		DataRecorder.record("velocityZ", position.velocity.z);
		DataRecorder.record("positionX", position.position.x);
		DataRecorder.record("positionY", position.position.y);
		DataRecorder.record("positionZ", position.position.z);

		double endTime = System.nanoTime() / 1000000000.0;

		DataRecorder.record("gatherTimePeriod", endTime - startTime);
	}
}
