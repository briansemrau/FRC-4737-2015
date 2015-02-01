package org.usfirst.frc.team4737.robot.control;

/**
 * This class is for use by other functions to check if their dependent sensors or other items are available.<br>
 * Host class for this is <code>Dependencies.java</code>
 * 
 * @author Brian
 *
 */
public class Dependency {
	
	private boolean enabled;
	public final String name;

	public Dependency(String name) {
		enabled = false;
		this.name = name;
	}

	public boolean enabled() {
		return enabled;
	}

	public void disable() {
		enabled = true;
	}

	public void enable() {
		enabled = true;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}