package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.math.Vector2d;

public class ExploreTask extends AbstractRobotTask {

	private AbstractRobotTask current;

	private int state;

	public ExploreTask() {
		super("explore", false);
	}

	public void init(Robot robot) {
		state = 1;
		
	}

	public void periodicExecution(Robot robot) {
		if (current.isFinished()) {
			state++;
		}
		switch (state) {
		case 1: {
			// Scan area
			
			break;
		}
		case 2: {
			// Explore
			/*if (movetask == null) {
				Vector2d goal = findUnexploredArea(robot);
				if (goal != null)
					movetask = new AutoMoveTask(goal, true);
				else
					state = 3;
			} else if (movetask.isFinished()) {
				movetask = null;
				
			}*/
			break;
		}
		default: {
			System.out.println("ExploreTask: Unable to explore any further.");
			finish();
			break;
		}
		}
	}

	private Vector2d findUnexploredArea(Robot robot) {
		Vector2d found = new Vector2d();

		return found;
	}

}
