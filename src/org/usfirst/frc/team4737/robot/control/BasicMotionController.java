package org.usfirst.frc.team4737.robot.control;

/**
 * A basic motion controller, not requiring any sensor input. Uses trapezoidal motion profiling.
 * 
 * @author Brian
 *
 */
public class BasicMotionController {

	public double maxDelta;
	public double maxValue;

	public double value;

	public BasicMotionController(double maxDelta, double maxValue) {
		this.maxDelta = maxDelta;
		this.maxValue = maxValue;
	}

	public double getValue(int direction, double deltaTime) {
		switch (direction) {
		case -1:
			value = Math.max(-maxValue, value - maxDelta * deltaTime);
			return value;
		case 0:
			if (value < 0) {
				value += maxDelta * deltaTime;
				if (value > 0)
					value = 0;
				return value;
			} else if (value > 0) {
				value -= maxDelta * deltaTime;
				if (value < 0)
					value = 0;
				return value;
			}
			return value;
		case 1:
			value = Math.min(maxValue, value + maxDelta * deltaTime);
			return value;
		}
		return 0;
	}
}
