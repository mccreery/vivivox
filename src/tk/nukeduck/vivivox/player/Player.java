package tk.nukeduck.vivivox.player;

import org.lwjgl.util.vector.Vector3f;

import tk.nukeduck.vivivox.world.World;

public class Player {
	public Vector3f position;
	
	public boolean isOnGround = false;
	
	public float rotationPitch;
	public float rotationYaw;
	
	public float radius = 0.2F;
	
	World world;
	
	public Player(World world) {
		this.position = new Vector3f(world.worldSize / 2 + 0.5F, 100F, world.worldSize / 2 + 0.5F);
		this.world = world;
	}
	
	public Player setRadius(float radius) {
		this.radius = radius;
		return this;
	}
	
	public float[] getRotation() {
		return new float[] {rotationPitch, rotationYaw};
	}
	
	public float getRotationPitch() {
		return this.rotationPitch;
	}
	
	public float getRotationYaw() {
		return this.rotationYaw;
	}
}