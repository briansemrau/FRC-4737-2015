package org.usfirst.frc.team4737.robot.wrappers;

public class Joystick {
	
	private edu.wpi.first.wpilibj.Joystick joy;
	
	private double deadband;
	
	private double zeroX;
	private double zeroY;
	
	public Joystick(int usb) {
		joy = new edu.wpi.first.wpilibj.Joystick(usb);
	}
	
	public void calibrate(double deadband) {
		this.deadband = deadband;
		zeroX = getRawX();
		zeroY = getRawY();
	}
	
	public double getX() {
		double val = getRawX() - zeroX;
		return (val < 0 && val > -deadband) || (val > 0 && val < deadband) ? 0 : val;
	}
	
	public double getRawX() {
		return joy.getRawAxis(0);
	}
	
	public double getY() {
		double val = getRawY() - zeroY;
		return (val < 0 && val > -deadband) || (val > 0 && val < deadband) ? 0 : val;
	}
	
	public double getRawY() {
		return joy.getRawAxis(1);
	}
	
	public double getZ() {
		return joy.getRawAxis(2);
	}
	
	public boolean getButton(int button) {
		return joy.getRawButton(button);
	}

}
