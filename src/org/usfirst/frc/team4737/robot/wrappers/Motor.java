package org.usfirst.frc.team4737.robot.wrappers;

import edu.wpi.first.wpilibj.Talon;

/**
 * A wrapper for the Victor including the ability to invert the output.
 * 
 * @author Brian
 *
 */
public class Motor {

	private Talon talon;
	private boolean inverted;
	
	public Motor(int pin, boolean inverted) {
		talon = new Talon(pin);
		this.inverted = inverted;
	}
	
	public void set(double power) {
		talon.set(inverted ? -power : power);
	}
	
}
