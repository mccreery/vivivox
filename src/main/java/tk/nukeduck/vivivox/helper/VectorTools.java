package tk.nukeduck.vivivox.helper;

import org.lwjgl.util.vector.Vector3f;

public class VectorTools {
	public static float getDistance(Vector3f a, Vector3f b)
	{
		float deltaX = b.x - a.x;
		float deltaY = b.y - a.y;
		float deltaZ = b.z - a.z;
		
		return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}
	
	public static float getDistance(Vector3f a, float bx, float by, float bz)
	{
		float deltaX = bx - a.x;
		float deltaY = by - a.y;
		float deltaZ = bz - a.z;
		
		return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}
	
	public static float getDistance(float ax, float ay, float az, Vector3f b)
	{
		float deltaX = b.x - ax;
		float deltaY = b.y - ay;
		float deltaZ = b.z - az;
		
		return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}
	
	public static float getDistance(float ax, float ay, float az, float bx, float by, float bz)
	{
		float deltaX = bx - ax;
		float deltaY = by - ay;
		float deltaZ = bz - az;
		
		return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}
	
	public static boolean isInRange(Vector3f a, Vector3f b, int range) {
		// Get the difference (delta) for each X, Y and Z
		float deltaX = b.x - a.x;
		float deltaY = b.y - a.y;
		float deltaZ = b.z - a.z;
		
		float val = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
		
		return val < range * range;
	}
	
	public static boolean isInRange(float ax, float ay, float az, float bx, float by, float bz, int range) {
		// Get the difference (delta) for each X, Y and Z
		float deltaX = bx - ax;
		float deltaY = by - ay;
		float deltaZ = bz - az;
		
		float val = (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);
		
		return val < range * range;
	}
}