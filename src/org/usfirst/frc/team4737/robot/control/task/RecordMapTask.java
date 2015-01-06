package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.math.Line2D;
import org.usfirst.frc.team4737.robot.math.Vector2d;

public class RecordMapTask extends AbstractRobotTask {

	private static double maxSpeed = 0.25;
	
	private double angleOrig;
	private double angleGoal;
	
	public RecordMapTask() {
		super("record_map", 5);
	}

	public void init(Robot robot) {
		angleOrig = robot.gyroAngle.z;
		angleGoal = angleOrig + 360;
	}

	public void periodicExecution(Robot robot) {
		double angleCurrent = robot.gyroAngle.z;
		
		if (angleCurrent < angleGoal) {
			robot.leftDriveMotors.set(maxSpeed);
			robot.rightDriveMotors.set(-maxSpeed);
			Vector2d position = new Vector2d(robot.position.x, robot.position.y);
			robot.map.mapLine(new Line2D(position, new Vector2d(angleCurrent, robot.usd.getRangeMM() * 1000.0, position)));
		} else {
			finish();
		}
	}

}
