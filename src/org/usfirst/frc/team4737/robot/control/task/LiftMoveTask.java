package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Robot;

public class LiftMoveTask extends AbstractRobotTask {

	public LiftMoveTask() {
		super("liftmovetask", 10);
	}
	
	public void periodicExecution(Robot robot) {
		Lift lift = robot.lift;
		
	}

}
