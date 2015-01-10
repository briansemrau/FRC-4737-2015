package org.usfirst.frc.team4737.robot.control.task;

import org.usfirst.frc.team4737.robot.Robot;
import org.usfirst.frc.team4737.robot.data.Map2;
import org.usfirst.frc.team4737.robot.math.Vector2d;

public class ExploreTask extends AbstractRobotTask {

	private AbstractRobotTask current;

	private static enum State {
		MAPPING, TRAVELING,
	}

	private State state;

	public ExploreTask() {
		super("autonExploreTask", true);
	}

	public void init(Robot robot) {
		state = State.MAPPING;
	}

	public void periodicExecution(Robot robot) {
		if (true)
			return;
		if (current.isFinished()) {
			if (state == State.MAPPING) {
				state = State.TRAVELING;
				Vector2d[] path = null;// createPath(robot.map, robot);
				if (path == null) {
					finish();
					return;
				} else {
					current = new PathTask(path);
				}
			} else if (state == State.TRAVELING) {
				state = State.MAPPING;
				current = new RecordMapTask();
			}
		}
		current.periodicExecution(robot);
	}

	/*
	 * private Vector2d findUnexploredArea(Robot robot) { Vector2d found = new
	 * Vector2d();
	 * 
	 * Map2 map = robot.map;
	 * 
	 * // Algorithm: // Find measured emptiness near a blob of unmeasured space
	 * // OR // Find any unmeasured pixel
	 * 
	 * int blobSize = 10; double percentReq = .5; for (int x = map.minX; x <=
	 * map.maxX; x += blobSize) { for (int y = map.minY; y <= map.maxY; y +=
	 * blobSize) { int total = blobSize * blobSize; int empty = 0; for (int bx =
	 * x; bx < x + blobSize; bx++) { for (int by = y; by < y + blobSize; by++) {
	 * if (map.getPoint(bx, by) == map.UNMEASURED) {
	 * 
	 * } } }
	 * 
	 * } }
	 * 
	 * return found; }
	 */

	private Vector2d[] createPath(Map2 map, Robot robot) {
		// TODO
		return null;
	}

}
