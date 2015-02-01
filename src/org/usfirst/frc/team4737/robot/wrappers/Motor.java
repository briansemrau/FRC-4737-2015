package org.usfirst.frc.team4737.robot.wrappers;

import edu.wpi.first.wpilibj.CANTalon;;

/**
 * A wrapper for the CanTalon including the ability to invert the output.
 * 
 * @author Brian
 *
 */
public class Motor {

	private CANTalon talon;
	private boolean inverted;
	private double setValue;
	
	public Motor(int pin, boolean inverted) {
		talon = new CANTalon(pin);
		this.inverted = inverted;
	}
	
	public void set(double power) {
		talon.set(inverted ? -power : power);
		setValue = power;
	}
	
	public double getSetValue() {
		return setValue;
	}
	
}
