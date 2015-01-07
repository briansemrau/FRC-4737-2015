package org.usfirst.frc.team4737.robot;

import org.usfirst.frc.team4737.robot.control.AbstractController;
import org.usfirst.frc.team4737.robot.control.AutonomousController;
import org.usfirst.frc.team4737.robot.control.TeleopController;
import org.usfirst.frc.team4737.robot.control.TestController;
import org.usfirst.frc.team4737.robot.data.DataRecorder;
import org.usfirst.frc.team4737.robot.data.Map2;
import org.usfirst.frc.team4737.robot.math.Vector3d;
import org.usfirst.frc.team4737.robot.subAssemblies.MotorGroup;
import org.usfirst.frc.team4737.robot.wrappers.Motor;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Ultrasonic;

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

	public Map2 map;

	// Sensors and Actuators

	public Motor driveMotorFrontLeft;
	public Motor driveMotorRearLeft;
	public Motor driveMotorFrontRight;
	public Motor driveMotorRearRight;

	public MotorGroup leftDriveMotors;
	public MotorGroup rightDriveMotors;

	public Gyro gyroscope;
	public BuiltInAccelerometer accelerometer;
	public Ultrasonic usd;

	public DigitalOutput led1;

	// Values

	/**
	 * The time (in seconds) since the last periodic update.
	 */
	private double timeLast;
	/**
	 * The current time (in seconds) measured most recently.
	 */
	private double timeCurrent;
	/**
	 * The time passed since the last periodic update.
	 */
	private double deltaTime;

	/**
	 * The measured acceleration of the robot.
	 */
	public Vector3d globalAcceleration;
	/**
	 * The calculated local-axis acceleration of the robot.
	 */
	public Vector3d localAcceleration;
	/**
	 * The measured value from the gyroscope(s).
	 */
	public Vector3d gyroAngle;
	/**
	 * The calculated velocity of the robot.
	 */
	public Vector3d velocity;
	/**
	 * The calculated position of the robot.
	 */
	public Vector3d position;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		teleop = new TeleopController(this);
		autonomous = new AutonomousController();
		test = new TestController();

		map = new Map2(500 * 1000);

		driveMotorFrontLeft = new Motor(Global.DRIVEMOTOR_FL, false);
		driveMotorRearLeft = new Motor(Global.DRIVEMOTOR_RL, false);
		driveMotorFrontRight = new Motor(Global.DRIVEMOTOR_FR, false);
		driveMotorRearRight = new Motor(Global.DRIVEMOTOR_RR, false);

		leftDriveMotors = new MotorGroup(driveMotorFrontLeft, driveMotorRearLeft);
		rightDriveMotors = new MotorGroup(driveMotorFrontRight, driveMotorRearRight);

		gyroscope = new Gyro(Global.GYROSCOPE);
		gyroscope.initGyro();
		accelerometer = new BuiltInAccelerometer();
		usd = new Ultrasonic(Global.USD_DIGITAL_IN, Global.USD_DIGITAL_OUT);

		led1 = new DigitalOutput(Global.LED_1);

		timeLast = System.nanoTime() / 1000000000.0;
		timeCurrent = System.nanoTime() / 1000000000.0;

		globalAcceleration = new Vector3d();
		localAcceleration = new Vector3d();
		velocity = new Vector3d();
		position = new Vector3d();
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

	public void commonPeriodic() {
		// Handle time
		timeLast = timeCurrent;
		timeCurrent = System.nanoTime() / 1000000000.0;
		deltaTime = timeCurrent - timeLast;

		// Measure acceleration. The accelerometer returns values in Gs
		localAcceleration.x = accelerometer.getX() / Global.GRAVITY;
		localAcceleration.y = accelerometer.getY() / Global.GRAVITY;
		localAcceleration.z = accelerometer.getZ() / Global.GRAVITY;

		// Measure angle using gyroscope
		gyroAngle.z = gyroscope.getAngle();

		// Calculate global acceleration
		globalAcceleration = localAcceleration.clone();
		globalAcceleration.rotate(gyroAngle.x, gyroAngle.y, gyroAngle.z);

		// Integrate velocity and position
		velocity.add(globalAcceleration.integral(deltaTime));
		position.add(velocity.integral(deltaTime));

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
		DataRecorder.record("gyroRX", gyroAngle.x);
		DataRecorder.record("gyroRY", gyroAngle.y);
		DataRecorder.record("gyroRZ", gyroAngle.z);
		DataRecorder.record("accelerationX (?)", globalAcceleration.x);
		DataRecorder.record("accelerationY (?)", globalAcceleration.y);
		DataRecorder.record("accelerationZ (?)", globalAcceleration.z);
		DataRecorder.record("velocityX", velocity.x);
		DataRecorder.record("velocityY", velocity.y);
		DataRecorder.record("velocityZ", velocity.z);
		DataRecorder.record("positionX", position.x);
		DataRecorder.record("positionY", position.y);
		DataRecorder.record("positionZ", position.z);

		double endTime = System.nanoTime() / 1000000000.0;

		DataRecorder.record("gatherTimePeriod", endTime - startTime);
	}
}
