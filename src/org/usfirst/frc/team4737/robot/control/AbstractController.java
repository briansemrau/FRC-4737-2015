package org.usfirst.frc.team4737.robot.control;

import java.util.ArrayList;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.control.task.AbstractRobotTask;

/**
 * 
 * @author Brian
 *
 */
public abstract class AbstractController {

	/**
	 * The list of currently running tasks.
	 */
	protected ArrayList<AbstractRobotTask> tasks;
	/**
	 * A list of tasks to add. This is in case tasks are created within other
	 * tasks and avoids a <code>ConcurrentModificationException</code>.
	 */
	private ArrayList<AbstractRobotTask> toAdd;

	public AbstractController() {
		tasks = new ArrayList<AbstractRobotTask>();
		toAdd = new ArrayList<AbstractRobotTask>();
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
		ArrayList<AbstractRobotTask> toRemove = new ArrayList<AbstractRobotTask>();
		for (AbstractRobotTask task : tasks) {
			if (task.isFinished())
				toRemove.add(task);
			else
				task.periodicExecution(robot);
		}
		tasks.removeAll(toRemove);
		tasks.addAll(toAdd);
		toAdd.clear();
	}

	/**
	 * Adds a task to the task list.
	 * 
	 * @param task
	 *            - The task to add
	 */
	public final void startTask(AbstractRobotTask task) {
		tasks.add(task);
	}

	/**
	 * Searches for a task with the given name and removes it from the task
	 * list.
	 * 
	 * @param taskName
	 *            - The name given to the task to remove.
	 */
	public final void killTask(String taskName) {
		for (AbstractRobotTask task : tasks) {
			if (task.getName().equals(taskName)) {
				task.finish();
				break;
			}
		}
	}

	/**
	 * Provides a list of the current tasks for display purposes.
	 * 
	 * @return Returns a <code>String[]</code> of all the names of the running
	 *         tasks.
	 */
	protected String[] getTaskNames() {
		String[] names = new String[tasks.size()];
		for (int i = 0; i < tasks.size(); i++) {
			names[i] = tasks.get(i).getName();
		}
		return names;
	}

}
