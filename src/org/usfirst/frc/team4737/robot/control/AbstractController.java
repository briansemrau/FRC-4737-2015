package org.usfirst.frc.team4737.robot.control;

import org.usfirst.frc.team4737.robot.Robot;

/**
 * 
 * @author Brian
 *
 */
public abstract class AbstractController {

	public AbstractController(Robot robot) {
	}

	/**
	 * The periodic update for the current controller.<br>
	 * <br>
	 * NOTE: Sub-classes MUST run the super-implementation.<br>
	 * <code>
	 * super.periodicUpdate(robot);
	 * </code>
	 */
	public void periodicUpdate(Robot robot) {
	}
	
	public abstract void reset();

}
