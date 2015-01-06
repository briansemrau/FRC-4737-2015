package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Robot;

public class ExploreTask extends AbstractRobotTask {

	private RecordMapTask maptask;

	private int state;

	public ExploreTask() {
		super("explore", false);
	}

	public void init(Robot robot) {
		state = 1;
	}

	public void periodicExecution(Robot robot) {
		switch (state) {
		case 1: {
			// Scan area
			if (maptask == null) {
				maptask = new RecordMapTask();
				robot.current.startTask(maptask);
			} else if (maptask.isFinished()) {
				state = 2;
			}
			break;
		}
		case 2: {
			// Explore
			break;
		}
		default: {
			System.out.println("ExploreTask: Unable to explore any further.");
			finish();
			break;
		}
		}
	}

}
