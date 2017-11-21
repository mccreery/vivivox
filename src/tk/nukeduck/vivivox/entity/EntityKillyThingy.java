package tk.nukeduck.vivivox.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import tk.nukeduck.vivivox.entity.ai.EntityAIWander;
import tk.nukeduck.vivivox.item.Item;
import tk.nukeduck.vivivox.world.World;

public class EntityKillyThingy extends Entity {
	public EntityKillyThingy(Vector3f position, Vector3f rotation) {
		super(position, rotation);
		this.addAI(new EntityAIWander());
	}
	
	public static float r, g, b;
	
	@Override
	public void render(World world) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(rotation.y, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Item.head.render();
		GL11.glPopMatrix();
	}
}