package org.usfirst.frc.team4737.robot.wrappers;

public class PWM {

	private edu.wpi.first.wpilibj.PWM pwm;
	
	public PWM(int PWMpin) {
		pwm = new edu.wpi.first.wpilibj.PWM(PWMpin);
	}
	
	public edu.wpi.first.wpilibj.PWM getPWM() {
		return pwm;
	}
	
	public void set(double value) {
		pwm.setRaw((int) Math.min(255, Math.max(0, value * 255))); 
	}
	
}
