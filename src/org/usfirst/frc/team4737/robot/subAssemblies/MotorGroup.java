package org.usfirst.frc.team4737.robot.subAssemblies;

import org.usfirst.frc.team4737.robot.wrappers.Motor;

/**
 * A class that allows you to control multiple given motors at once.
 * 
 * @author Brian
 *
 */
public class MotorGroup {

	private Motor[] motors;

	public MotorGroup(Motor... motors) {
		this.motors = motors;
	}

	public Motor[] getMotors() {
		return motors;
	}

	public void set(double power) {
		for (Motor m : motors) {
			m.set(power);
		}
	}

}
