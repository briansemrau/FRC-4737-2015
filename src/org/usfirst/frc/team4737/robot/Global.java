package org.usfirst.frc.team4737.robot;

/**
 * Despite the violations of object-oriented code, this is necessary to keep
 * things neat.
 * 
 * @author Brian
 *
 */
public class Global {
	
	public static final int V_COMPETITION = 0; // The first value is the code version after each competition
	public static final int V_MAJOR = 1; // The major value is for major code additions
	public static final int V_MINOR = 0; // The minor value is for minor code edits
	public static final String VERSION = "" + V_COMPETITION + "." + V_MAJOR + "." + V_MINOR + " indev";
	
	// Fundamental constants

	public static final double GRAVITY = 9.81;

	// Other constants

	public static final double LOG_AUTOSAVE_PERIOD = 10;
	
	public static final double LOW_VOLTAGE = 12; // TODO: find the voltage when
													// the robot's maximum
													// velocity begins to
													// decrease
	
	public static final int DATATABLE_SAVE_BUTTON = 6; // TODO pick
	
	// Control tuning

	public static final double JOYSTICK_DEADBAND = 0.03;
	
	public static final double ARCADE_YAW_SENSITIVITY = 1; // TODO tune

	public static final double AUTOMOVE_YAW_SENTITIVITY = 1.0; // TODO tune
	public static final double AUTOMOVE_MAXSPEED = 1.0; // TODO tune
	public static final double AUTOMOVE_ANGULAR_kP = 0.1; // TODO tune
	public static final double AUTOMOVE_kP = 0.1; // TODO tune
	
	public static final double LIFT_kP = 0.1;
	public static final double LIFT_kI = 0.0001;
	public static final double LIFT_IRANGE = 0.1;
	public static final double LIFT_MAX_ACCEL = 0.3;
	public static final double LIFT_MAX_DECEL = 0.3;
	public static final double LIFT_MAX_VEL = 0.3;
	public static final double LIFT_MIN_HEIGHT = 0.0;
	public static final double LIFT_MAX_HEIGHT = 1.8; // TODO tune
	public static final double LIFT_MAX_DIFFERENCE = 0.005; // TODO tune

	// Sensor/Actuator pins
	
	public static final String CAMERA_IP = "10.47.37.11";

	public static final int JOYSTICK_1_USB = 0;
	public static final int JOYSTICK_2_USB = 1;

	public static final int DRIVEMOTOR_FL = 24; // 25, 24, 23, 22
	public static final int DRIVEMOTOR_FR = 25;
	public static final int DRIVEMOTOR_RL = 22;
	public static final int DRIVEMOTOR_RR = 23;

	public static final int GYROSCOPE = 0;
	
	public static final int LIFT_MOTOR_LEFT = 20; // 21, 20
	public static final int LIFT_MOTOR_RIGHT = 21;
	
	public static final int LIFT_ENCODER_LEFTA = -1;
	public static final int LIFT_ENCODER_LEFTB = -1;
	public static final int LIFT_ENCODER_RIGHTA = -1;
	public static final int LIFT_ENCODER_RIGHTB = -1;
	public static final int LIFT_ENCODER_DPP = -99;
	
	public static final int LIFT_BUMPER_TOP = -1;
	public static final int LIFT_BUMPER_BOTTOM = -1;

}
