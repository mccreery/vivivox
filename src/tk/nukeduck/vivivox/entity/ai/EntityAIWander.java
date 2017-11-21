package tk.nukeduck.vivivox.entity.ai;

import org.lwjgl.util.vector.Vector3f;

import tk.nukeduck.vivivox.VivivoxMain;
import tk.nukeduck.vivivox.entity.Entity;
import tk.nukeduck.vivivox.world.World;

public class EntityAIWander extends EntityAI {
	public String name = "Walk Around";
	
	public EntityAIWander() {
		wandering = false;
		wanderLocation = null;
	}
	
	private boolean wandering;
	private Vector3f wanderLocation;
	
	public void tick(Entity e, World world) {
		if(wandering) {
			e.rotation.y = 360 - (float) angleBetween(e.position.x + 0.5F, e.position.z + 0.5F, wanderLocation.x, wanderLocation.z);
			
			//System.out.println(e.rotation.y + ", " + Math.sin(e.rotation.y) + ", " + Math.cos(e.rotation.y));
			
			e.position.x += Math.sin(e.rotation.y) / 100.0;
			e.position.z += Math.cos(e.rotation.y) / 100.0;
			
			//e.position.z = e.position.x = 25;
			//e.position.y = 80;
			wandering = false;
		} else {
			wandering = true;
			wanderLocation = VivivoxMain.player.position;
		}
	}
	
	public static final double halfPi = Math.PI / 2.0;
	
	public static double angleBetween(float x1, float y1, float x2, float y2) {
		double angle = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1) + halfPi)/* + 90*/;// this will give you an angle from [0->270],[-180,0]
		if(angle < 0) angle += 360;
		
		return angle;
	}
}