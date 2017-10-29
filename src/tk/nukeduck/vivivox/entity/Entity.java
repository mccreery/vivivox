package tk.nukeduck.vivivox.entity;

import org.lwjgl.util.vector.Vector3f;

import tk.nukeduck.vivivox.world.World;

public class Entity {
	protected Vector3f position;
	protected Vector3f rotation;
	
	public Entity(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Vector3f getRotation() {
		return this.rotation;
	}
	
	public void render(World world) {
		
	}
	
	public void tick() {
		
	}
}