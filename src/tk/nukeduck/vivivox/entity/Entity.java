package tk.nukeduck.vivivox.entity;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import tk.nukeduck.vivivox.VivivoxMain;
import tk.nukeduck.vivivox.world.World;
import tk.nukeduck.vivivox.entity.ai.*;

public class Entity {
	public Vector3f position;
	public Vector3f rotation;
	
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
	
	public void addAI(EntityAI a) {
		ai.add(a);
	}
	
	ArrayList<EntityAI> ai = new ArrayList<EntityAI>();
	
	public void tick(World world) {
		if(!VivivoxMain.world.getBlock((int) position.x, (int) position.y - 1, (int) position.z).isOpaque()) {
			position.y -= 1;
		}
		
		for(EntityAI a : ai) {
			a.tick(this, world);
		}
	}
}