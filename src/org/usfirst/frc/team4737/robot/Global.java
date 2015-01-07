package org.usfirst.frc.team4737.robot;

/**
 * Despite the violations of object-oriented code, this is necessary to keep
 * things neat.
 * 
 * @author Brian
 *
 */
public class Global {

	// Fundamental constants

	public static final double GRAVITY = 9.80;

	// Other constants

	public static final double LOW_VOLTAGE = 11; // TODO: find the voltage when
													// the robot's maximum
													// velocity begins to
													// decrease

	// Control tuning

	public static final double MAP_POINT_RADIUS = 0.05; // TODO test ultrasonic
														// error range
	public static final double ARCADE_YAW_SENSITIVITY = 1; // TODO tune

	public static final double AUTOMOVE_kP = 0.1; // TODO tune
	public static final double AUTOMOVE_ANGULAR_kP = 0.01; // TODO tune
	public static final double AUTOMOVE_MAXSPEED = 1.0; // TODO tune

	// Sensor/Actuator pins

	public static final int JOYSTICK_1_USB = 1; // TODO pick right ports
	public static final int JOYSTICK_2_USB = 2; // --

	public static final int DRIVEMOTOR_FL = -1; // TODO pick pins
	public static final int DRIVEMOTOR_FR = -1; // TODO pick pins
	public static final int DRIVEMOTOR_RL = -1; // TODO pick pins
	public static final int DRIVEMOTOR_RR = -1; // TODO pick pins

	public static final int GYROSCOPE = -1; // TODO pick pins
	public static final int USD_DIGITAL_IN = -1; // TODO pick pins
	public static final int USD_DIGITAL_OUT = -1; // TODO pick pins
	
	public static final int LED_1 = -1; // TODO pick pins

}
