package org.usfirst.frc.team4737.robot.control;

import org.usfirst.frc.team4737.robot.math.Vector2d;

/**
 * Uses functionalities of PID motion controllers and TMPs (Trapezoidal Motion Profilers) to create an all-inclusive
 * motion controller.
 * 
 * @author Brian
 *
 */
public class MotionController {

	// Control values
	public double kP, kI, kD;
	public double accelLimit, velLimit, decelLimit;
	private DataTable accelTuning;
	
	private boolean mapping;
	private double lastValue;

	// Error calculation values
	private double goal, value;

	// Integral term handling
	private double integral;
	private double integralDecay;

	// Differential term handling
	private int recordLength;
	private Vector2d[] velocityRecord;
	private boolean validDiff = false;

	public MotionController(double kP, double kI, double kD, double accelLimit, double velLimit, double decelLimit,
			double expectedDeltaT, double integralDecay, int differentialRecordingLength, String accelTuningFile) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.accelLimit = accelLimit;
		this.velLimit = velLimit;
		this.decelLimit = decelLimit;

		if (accelTuningFile != null) {
			accelTuning = new DataTable(accelTuningFile);
		} else {
			accelTuning = new DataTable(.01, -1, 1, 0);
			mapping = true;
		}

		this.integralDecay = integralDecay;

		this.recordLength = differentialRecordingLength;
		velocityRecord = new Vector2d[recordLength];
	}

	public void setGoal(double goal) {
		this.goal = goal;
	}

	public double getCurrentGoal() {
		return goal;
	}

	public double getLastValue() {
		return value;
	}

	public double getPower(double value, double acceleration, double velocity, double deltaT) {
		// Things to think about:
		// - integral integration before or after power calculation?
		// - same for differential
		
		// Mapping
		// Note: this will not be very accurate due to time delay between updates.
		// This could be made more accurate by calculating jerk
		if (mapping) {
			accelTuning.mapValue(lastValue, acceleration, false);
		}

		// Error
		this.value = value;
		double error = goal - value;

		// Integral calculation
		integral += error * deltaT;

		// Differential calculation
		for (int n = recordLength - 2; n > 0; n--) {
			velocityRecord[n + 1] = velocityRecord[n];
		}
		velocityRecord[0] = new Vector2d(velocity, deltaT);
		double differential = 0;
		if (validDiff) {
			double totalTime = 0, totalValue = 0;
			for (int n = 0; n < recordLength; n++) {
				totalValue += velocityRecord[n].x;
				totalTime += velocityRecord[n].y;
			}
			differential = totalValue / totalTime;
		} else {
			// Checks if the record is full of values yet
			validDiff = velocityRecord[recordLength - 1] != null;
		}

		double pidPower = (kP * error) + (kI * integral) + (kD * differential);

		double limitedPower = pidPower;

		// TMP power limiting
		
		if (velocity > velLimit) {
			limitedPower = accelTuning.findClosestX((velLimit - velocity));
		}
		
		if (acceleration > 0) {
			if (acceleration > accelLimit || accelTuning.getY(pidPower) > accelLimit) {
				limitedPower = 0;
			}
		} else if (acceleration < 0) {
			if (-acceleration > decelLimit || -accelTuning.getY(pidPower) > decelLimit) {
				limitedPower = accelTuning.findClosestX(decelLimit - acceleration);
			}
		}

		// Integral decay
		integral -= integralDecay * deltaT;

		return limitedPower;
	}
	
}
