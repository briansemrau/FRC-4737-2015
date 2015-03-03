package org.usfirst.frc.team4737.robot;

import org.usfirst.frc.team4737.robot.control.AbstractController;
import org.usfirst.frc.team4737.robot.control.AutonomousController;
import org.usfirst.frc.team4737.robot.control.TeleopController;
import org.usfirst.frc.team4737.robot.control.TestController;
import org.usfirst.frc.team4737.robot.data.DataRecorder;
import org.usfirst.frc.team4737.robot.math.Positioner;
import org.usfirst.frc.team4737.robot.subAssemblies.Lift;
import org.usfirst.frc.team4737.robot.subAssemblies.MotorGroup;
import org.usfirst.frc.team4737.robot.vision.Vision;
import org.usfirst.frc.team4737.robot.wrappers.Dashboard;
import org.usfirst.frc.team4737.robot.wrappers.Joystick;
import org.usfirst.frc.team4737.robot.wrappers.Motor;

import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.RGBImage;
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
	private AbstractController current;

	public Joystick primaryJoystick;
	public Joystick secondaryJoystick;

	// Controller Assistants

	public Vision vision;
	private ColorImage cameraImage;

	// Sensors and Actuators

	public AxisCamera camera;
	private boolean cameraCreated = false;

	public Motor driveMotorFrontLeft;
	public Motor driveMotorRearLeft;
	public Motor driveMotorFrontRight;
	public Motor driveMotorRearRight;

	public Compressor compressor;

	public Lift lift;

	public Gyro gyroscope;
	public BuiltInAccelerometer accelerometer;

	public MotorGroup leftDriveMotors;
	public MotorGroup rightDriveMotors;

	// Values

	private double timeStart;
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
	private double sinceLastDataRecord;

	public Positioner positioner;

	/**
	 * This function is run when the robot is first started up and should be used for any initialization code.
	 */
	public void robotInit() {
		Log.init();

		timeStart = System.nanoTime() / 1000000000.0;
		timeLast = System.nanoTime() / 1000000000.0;
		timeCurrent = System.nanoTime() / 1000000000.0;

		Log.println("Robot Code Version " + Global.VERSION);
		Log.println("Initializing Robot...");

		Log.println("Initializing controllers...");

		primaryJoystick = new Joystick(Global.JOYSTICK_1_USB);
		secondaryJoystick = new Joystick(Global.JOYSTICK_2_USB);
		primaryJoystick.calibrate(Global.JOYSTICK_DEADBAND);
		secondaryJoystick.calibrate(Global.JOYSTICK_DEADBAND);

		teleop = new TeleopController(this);
		autonomous = new AutonomousController(this);
		test = new TestController(this);
		current = null;

		Global.USE_RAW_DRIVING = true;

		Log.println("Initializing sensors and actuators...");

		Log.println("\tRunning front left drive motor on CAN ID " + Global.DRIVEMOTOR_FL);
		driveMotorFrontLeft = new Motor(Global.DRIVEMOTOR_FL, false);
		Log.println("\tRunning rear left drive motor on CAN ID " + Global.DRIVEMOTOR_RL);
		driveMotorRearLeft = new Motor(Global.DRIVEMOTOR_RL, false);
		Log.println("\tRunning front right drive motor on CAN ID " + Global.DRIVEMOTOR_FR);
		driveMotorFrontRight = new Motor(Global.DRIVEMOTOR_FR, true);
		Log.println("\tRunning rear right drive motor on CAN ID " + Global.DRIVEMOTOR_RR);
		driveMotorRearRight = new Motor(Global.DRIVEMOTOR_RR, true);

		leftDriveMotors = new MotorGroup(driveMotorFrontLeft, driveMotorRearLeft);
		rightDriveMotors = new MotorGroup(driveMotorFrontRight, driveMotorRearRight);

		compressor = new Compressor();

		lift = new Lift(this);

		gyroscope = new Gyro(Global.GYROSCOPE);
		gyroscope.initGyro();
		Log.println("\tUsing gyro on analog " + Global.GYROSCOPE);

		accelerometer = new BuiltInAccelerometer();
		accelerometer.startLiveWindowMode();

		Log.println("\tUsing camera on port " + Global.CAMERA_IP);

		positioner = new Positioner(this);

		Log.println("Finished robot initialization.");

		// positioner.startCalibration();
	}

	public void autonomousInit() {
		Log.println("Enabling auton");
		if (current != null) {
			current.reset();
		}
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
		Log.println("Enabling teleop");
		if (current != null) {
			current.reset();
		}
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
		Log.println("Enabling test");
		if (current != null) {
			current.reset();
		}
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
		Log.println("Robot disabled");
		if (current != null) {
			current.reset();
		}
		current = null;
		
//		lift.closeArms();
		
		if (Dashboard.getButton(Dashboard.DB_BUTTON_0, false))
			DataRecorder.saveDataLog();
	}

	public void disabledPeriodic() {
		commonPeriodic();
	}

	public void commonPeriodic() {
		// Compressor
		if (compressor.getPressureSwitchValue()) {
			compressor.stop();
		} else {
			compressor.start();
		}

		// Handle vision
		if (Dashboard.getButton(Dashboard.DB_BUTTON_3, false) && timeCurrent - timeStart > 5) {
			// vision.update(camera);
			if (!cameraCreated) {
				try {
					camera = new AxisCamera(Global.CAMERA_IP);
					cameraCreated = true;
				} catch (VisionException e) {
				}
				vision = new Vision();
				try {
					cameraImage = new RGBImage();
				} catch (NIVisionException e1) {
					// e1.printStackTrace();
				} catch (VisionException e1) {
					// e1.printStackTrace();
				}
			}
			try {
				camera.getImage(cameraImage);
				if (cameraImage != null) {
					CameraServer.getInstance().setImage(cameraImage.image);
					Dashboard.setButton(Dashboard.DB_LED_3, true);
				}
			} catch (VisionException e) {
				e.printStackTrace();
			}
		} else {
			Dashboard.setButton(Dashboard.DB_LED_3, false);
		}

		// Handle time
		timeLast = timeCurrent;
		timeCurrent = System.nanoTime() / 1000000000.0;
		deltaTime = timeCurrent - timeLast;
		Dashboard.putString(Dashboard.DB_STRING_9, "Time: " + (int) ((timeCurrent - timeStart) * 100) / 100.0 + "s");

		// Handle log
		sinceLastLogSave += deltaTime;
		if (sinceLastLogSave >= Global.LOG_AUTOSAVE_PERIOD) {
			sinceLastLogSave = 0;
			Log.saveLog();
		}
		if (secondaryJoystick.getButton(7))
			DataRecorder.saveDataLog();

		if (primaryJoystick.getButton(Global.IO_RECALIBRATE_BUTTON)) {
			positioner.startCalibration();
			gyroscope.initGyro();
		}

		positioner.update(this, deltaTime);

		// Data mining
		sinceLastDataRecord += deltaTime;
		if (sinceLastDataRecord >= Global.DATA_RECORD_PERIOD) {
			sinceLastDataRecord = 0;
			recordData();
		}

		// Motion Controller data table saving

	}

	/**
	 * Records a bunch of data
	 */
	private void recordData() {
		double startTime = System.nanoTime() / 1000000000.0;

		// Include ALL measurable values in here

		DataRecorder.record("time (s)", startTime);
		DataRecorder.record("voltage", DriverStation.getInstance().getBatteryVoltage());
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
