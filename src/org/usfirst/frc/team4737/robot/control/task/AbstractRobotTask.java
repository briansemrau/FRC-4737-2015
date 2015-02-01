package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.Dependency;

public abstract class AbstractRobotTask {

	/**
	 * The name of the task.
	 */
	private String name;
	/**
	 * The flag for whether or not the task is to be terminated.
	 */
	private boolean finished;
	/**
	 * The flag for whether or not this task will be automatically terminated.
	 */
	private boolean timeKill;
	/**
	 * The total time the task will be alive before automatic termination.
	 */
	private double timeToLive;
	/**
	 * How much time the task has left to run before automatic termination.
	 */
	private double timeLeftAlive;
	/**
	 * The time (in seconds) when the task was initialized.
	 */
	private double timeInit;
	/**
	 * The time (in seconds) since the last periodic update.
	 */
	private double timeLast;
	/**
	 * The current time (in seconds) measured most recently.
	 */
	private double timeCurrent;
	
	private Dependency[] dependencies;

	private AbstractRobotTask(String name, Dependency... dependencies) {
		this.name = name;
		timeInit = System.nanoTime() / 1000000000.0;
		timeLast = System.nanoTime() / 1000000000.0;
		timeCurrent = System.nanoTime() / 1000000000.0;
		this.dependencies = dependencies;
	}

	/**
	 * 
	 * The time the task remains alive is automatically set to 2 minutes and 30
	 * seconds.
	 * 
	 * @param name
	 *            - The name of the task.
	 * 
	 * @param timeKill
	 *            - An override for whether or not the task should ever be
	 *            automatically terminated. This is necessary for things such as
	 *            movement.
	 */
	public AbstractRobotTask(String name, boolean timeKill, Dependency... dependencies) {
		this(name);
		this.timeKill = timeKill;
		if (timeKill) {
			timeLeftAlive = 150;
			timeToLive = 150;
		} else {
			timeLeftAlive = Double.POSITIVE_INFINITY;
			timeToLive = Double.POSITIVE_INFINITY;
		}
		this.dependencies = dependencies;
	}

	/**
	 * @param name
	 *            - The name of the task.
	 * 
	 * @param timeAlive
	 *            - How long the task should execute before it gets
	 *            automatically terminated.<br>
	 *            This is to avoid potentially crippling tasks from taking
	 *            control of the robot for too long if they go wrong.<br>
	 *            The default value for this is 2 minutes and 30 seconds.<br>
	 * <br>
	 *            This should be implemented by the sub-class if necessary.
	 */
	public AbstractRobotTask(String name, double timeAlive) {
		this(name);
		timeKill = true;
		timeLeftAlive = timeAlive;
		timeToLive = timeAlive;
	}

	public void init(Robot robot) {
	}
	
	/**
	 * The periodic code for executing this task.<br>
	 * <br>
	 * NOTE: Sub-classes MUST run the super-implementation.<br>
	 * <code>
	 * super.periodicExecution(robot);
	 * </code>
	 */
	public void periodicExecution(Robot robot) {
		if (finished)
			return;
		timeLast = timeCurrent;
		timeCurrent = System.nanoTime() / 1000000000.0;
		if (timeKill) {
			if (timeLeftAlive <= 0)
				finish();
			timeLeftAlive -= deltaTime();
		}
		for (Dependency d : dependencies) {
			if (!d.enabled()) return;
		}
	}

	/**
	 * @return Returns the time remaining before automatic termination;
	 */
	protected double remainingTimeToLive() {
		return timeLeftAlive;
	}

	/**
	 * @return Returns the time the task is given to run since initialization.
	 */
	protected double totalTimeToLive() {
		return timeToLive;
	}

	/**
	 * @return Returns the time passed since the task was created.
	 */
	protected double timePassed() {
		return timeCurrent - timeInit;
	}

	/**
	 * @return Returns the time passed since the last periodic execution.
	 */
	protected double deltaTime() {
		return timeCurrent - timeLast;
	}

	/**
	 * Flag the task for termination.
	 */
	public void finish() {
		finished = true;
	}

	/**
	 * @return Returns whether or not this task should be removed from the task
	 *         list.
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * @return Returns the name given to the task.
	 */
	public String getName() {
		return name;
	}

}
