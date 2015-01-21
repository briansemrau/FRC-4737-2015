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
		angleOrig = robot.position.gyroAngle.z;
		angleGoal = angleOrig + 360;
	}

	public void periodicExecution(Robot robot) {
		if (true)
			return;

		double angleCurrent = robot.position.gyroAngle.z;

		if (angleCurrent < angleGoal) {
			robot.leftDriveMotors.set(maxSpeed);
			robot.rightDriveMotors.set(-maxSpeed);
			Vector2d position = new Vector2d(robot.position.position.x,
					robot.position.position.y);
			Line2D line = new Line2D(position, new Vector2d(angleCurrent,
					robot.usd.getRangeMM() * 1000.0, position));
			// robot.map.mapLine(10, (int) (line.a.x * 1000),
			// (int) (line.a.y * 1000), (int) (line.b.x * 1000),
			// (int) (line.b.y * 1000));
		} else {
			finish();
		}
	}
}
