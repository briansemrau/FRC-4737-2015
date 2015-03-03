package org.usfirst.frc.team4737.robot.math;

/**
 * A class to represent a 3D coordinate or a set of 3 decimal values.<br>
 * <br>
 * Y values represent depth and Z values represents height.
 * 
 * @author Brian
 *
 */
public class Vector3d {

	public double x;
	public double y;
	public double z;

	public Vector3d() {
	}

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d plus(Vector3d b) {
		return new Vector3d(x + b.x, y + b.y, z + b.z);
	}

	public Vector3d minus(Vector3d b) {
		return new Vector3d(x - b.x, y - b.y, z - b.z);
	}

	public Vector3d scaled(double scale) {
		return new Vector3d(x * scale, y * scale, z * scale);
	}

	public void add(Vector3d b) {
		x += b.x;
		y += b.y;
		z += b.z;
	}

	public void subtract(Vector3d b) {
		x -= b.x;
		y -= b.y;
		z -= b.z;
	}

	public Vector3d average(Vector3d b) {
		return new Vector3d((x + b.x) / 2.0, (y + b.y) / 2.0, (z + b.z) / 2.0);
	}

	/**
	 * Rotates the vector about the X axis. Generally not to be used when rotating any other axes simultaneously.
	 * 
	 * @param angleDeg
	 *            - The angle to rotate in degrees
	 */
	public void rotateX(double angleDeg) {
		double currentAngle = Math.atan2(z, y);
		double magnitude = Math.hypot(y, z);
		y = Math.cos(currentAngle + Math.toRadians(angleDeg)) * magnitude;
		z = Math.sin(currentAngle + Math.toRadians(angleDeg)) * magnitude;
	}

	/**
	 * Rotates the vector about the Y axis. Generally not to be used when rotating any other axes simultaneously.
	 * 
	 * @param angleDeg
	 *            - The angle to rotate in degrees
	 */
	public void rotateY(double angleDeg) {
		double currentAngle = Math.atan2(z, -x);
		double magnitude = Math.hypot(-x, z);
		x = -Math.cos(currentAngle + Math.toRadians(angleDeg)) * magnitude;
		z = Math.sin(currentAngle + Math.toRadians(angleDeg)) * magnitude;
	}

	/**
	 * Rotates the vector about the Z axis. Generally not to be used when rotating any other axes simultaneously.
	 * 
	 * @param angleDeg
	 *            - The angle to rotate in degrees
	 */
	public void rotateZ(double angleDeg) {
		double currentAngle = Math.atan2(y, x);
		double magnitude = Math.hypot(x, y);
		x = Math.cos(currentAngle + Math.toRadians(angleDeg)) * magnitude;
		y = Math.sin(currentAngle + Math.toRadians(angleDeg)) * magnitude;
	}

	/**
	 * Does a matrix rotation of the vector.
	 * 
	 * @param rx
	 *            - Rotation about the X axis
	 * @param ry
	 *            - Rotation about the Y axis
	 * @param rz
	 *            - Rotation about the Z axis
	 */
	public void rotate(double rx, double ry, double rz) {
		double rxRad = Math.toRadians(rx);
		double ryRad = Math.toRadians(ry);
		double rzRad = Math.toRadians(rz);
		double sx = Math.sin(rxRad);
		double sy = Math.sin(ryRad);
		double sz = Math.sin(rzRad);
		double cx = Math.cos(rxRad);
		double cy = Math.cos(ryRad);
		double cz = Math.cos(rzRad);

		double newX = cy * (sz * y + cz * x) - sy * z;
		double newY = sx * (cy * z + sy * (sz * y + cz * x)) + cx * (cz * y - sz * x);
		double newZ = cx * (cy * z + sy * (sz * y + cz * x)) - sx * (cz * y - sz * x);

		x = newX;
		y = newY;
		z = newZ;
	}

	/**
	 * Returns the indefinite integral with respect to the change in time.
	 * 
	 * @param d
	 *            - The derivative value
	 * @param deltaTime
	 *            - The change in time
	 * @return Returns <i>S(d•dt)</i>
	 */
	public Vector3d integral(double deltaTime) {
		return scaled(deltaTime);
	}

	public void deadband(double deadband) {
		if (Math.abs(x) < deadband)
			x = 0;
		if (Math.abs(y) < deadband)
			y = 0;
		if (Math.abs(z) < deadband)
			z = 0;
	}

	public double magnitude() {
		return Math.sqrt(Math.sqrt(x * x + y * y) + z * z);
	}

	public Vector3d clone() {
		return new Vector3d(x, y, z);
	}

}
