package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.math.Vector2d;

public class PathTask extends AbstractRobotTask {

	public static final int GLOBAL = 1;
	public static final int RELATIVE_TO_LAST = 2;
	public static final int RELATIVE_TO_PATH = 3;

	private Vector2d[] path;
	private int currentMove;
	// private int mode;
	private AutoMoveTask movetask;

	public PathTask(Vector2d[] globalpath/* , int mode */) {
		super("pathTask", false);
		this.path = globalpath;
		// this.mode = mode;
	}

	public void init(Robot robot) {
		currentMove = 0;
		movetask = new AutoMoveTask(path[currentMove], true, false);
	}

	public void periodicExecution(Robot robot) {
		if (movetask.isFinished()) {
			currentMove++;
			if (currentMove < path.length) {
				movetask = new AutoMoveTask(path[currentMove], true, false);

			} else {
				robot.leftDriveMotors.set(0);
				robot.rightDriveMotors.set(0);
				finish();
				return;
			}
		} else {
			movetask.periodicExecution(robot);
		}
	}

}
