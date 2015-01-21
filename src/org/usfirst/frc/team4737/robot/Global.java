package org.usfirst.frc.team4737.robot;

/**
 * Despite the violations of object-oriented code, this is necessary to keep
 * things neat.
 * 
 * @author Brian
 *
 */
public class Global {
	
	public static final String VERSION = "0.1";

	// Fundamental constants

	public static final double GRAVITY = 9.80;

	// Other constants

	public static final double LOW_VOLTAGE = 11; // TODO: find the voltage when
													// the robot's maximum
													// velocity begins to
													// decrease
	public static final double DECELERATION_MOD = 0.1; // TODO calculate
	
	public static final double LOG_AUTOSAVE_PERIOD = 10;
	
	// Control tuning

	public static final double ARCADE_YAW_SENSITIVITY = 1; // TODO tune

	public static final double AUTOMOVE_kP = 0.1; // TODO tune
	public static final double AUTOMOVE_ANGULAR_kP = 0.01; // TODO tune
	public static final double AUTOMOVE_MAXSPEED = 1.0; // TODO tune
	
	public static final double LIFT_MIN_HEIGHT = 0.0;
	public static final double LIFT_MAX_HEIGHT = 1.8; // TODO tune
	public static final double LIFT_MAX_DIFFERENCE = 0.005; // TODO tune
	public static final double LIFT_kP = 0.1; // TODO tune
	public static final double LIFT_kI = 0.01; // TODO tune

	// Sensor/Actuator pins
	
	public static final String CAMERA_IP = "10.47.37.11";

	public static final int JOYSTICK_1_USB = 1;
	public static final int JOYSTICK_2_USB = 2;

	public static final int DRIVEMOTOR_FL = -1;
	public static final int DRIVEMOTOR_FR = -1;
	public static final int DRIVEMOTOR_RL = -1;
	public static final int DRIVEMOTOR_RR = -1;

	public static final int GYROSCOPE = -1;
	
	public static final int USD_DIGITAL_IN = -1;
	public static final int USD_DIGITAL_OUT = -1;
	
	public static final int LED_1 = -1;
	
	public static final int LIFT_MOTOR_LEFT = -1;
	public static final int LIFT_MOTOR_RIGHT = -1;
	public static final int LIFT_ENCODER_LEFTA = -1;
	public static final int LIFT_ENCODER_LEFTB = -1;
	public static final int LIFT_ENCODER_RIGHTA = -1;
	public static final int LIFT_ENCODER_RIGHTB = -1;
	public static final int LIFT_ENCODER_DPP = -1;

}
