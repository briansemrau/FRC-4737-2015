package org.usfirst.frc.team4737.robot.wrappers;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class DoubleSolenoid {

	private edu.wpi.first.wpilibj.DoubleSolenoid solenoid;
	
	public DoubleSolenoid(int forwardChannel, int backwardChannel) {
		solenoid = new edu.wpi.first.wpilibj.DoubleSolenoid(forwardChannel, backwardChannel);
	}
	
	public void set(int value) {
		switch (value) {
		case -1:
			solenoid.set(Value.kReverse);
			break;
		case 0:
			solenoid.set(Value.kOff);
			break;
		case 1:
			solenoid.set(Value.kForward);
			break;
		}
	}
	
}
