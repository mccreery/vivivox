package tk.nukeduck.vivivox.block;

import tk.nukeduck.vivivox.world.World;

public class BlockLight extends Block {
	public byte lightLevel;
	
	public BlockLight(int id) {
		super(id);
		lightLevel = 10;
	}
	
	public BlockLight(int id, int lightLevel) {
		super(id);
		this.lightLevel = (byte) lightLevel;
	}
	
	public void processLight(int x, int y, int z, World world) {
		world.setLightLevel(x, y, z, this.lightLevel);
		process(x, y, z, this.lightLevel, world);
	}
	
	public void process(int x, int y, int z, byte lightLevel, World world) {
		try {
			byte next = (byte) (lightLevel - 1);
			// X
			if(world.getLightLevel(x - 1, y, z) < next && !world.getBlock(x - 1, y, z).isOpaque()) {
				world.setLightLevel(x - 1, y, z, next);
				process(x - 1, y, z, next, world);
			}
			
			if(world.getLightLevel(x + 1, y, z) < next && !world.getBlock(x + 1, y, z).isOpaque()) {
				world.setLightLevel(x + 1, y, z, next);
				process(x + 1, y, z, next, world);
			}
			
			// Y
			if(world.getLightLevel(x, y - 1, z) < next && !world.getBlock(x, y - 1, z).isOpaque()) {
				world.setLightLevel(x, y - 1, z, next);
				process(x, y - 1, z, next, world);
			}
			
			if(world.getLightLevel(x, y + 1, z) < next && !world.getBlock(x, y + 1, z).isOpaque()) {
				world.setLightLevel(x, y + 1, z, next);
				process(x, y + 1, z, next, world);
			}
			
			// Z
			if(world.getLightLevel(x, y, z - 1) < next && !world.getBlock(x, y, z - 1).isOpaque()) {
				world.setLightLevel(x, y, z - 1, next);
				process(x, y, z - 1, next, world);
			}
			
			if(world.getLightLevel(x, y, z + 1) < next && !world.getBlock(x, y, z + 1).isOpaque()) {
				world.setLightLevel(x, y, z + 1, next);
				process(x, y, z + 1, next, world);
			}
		} catch(Exception e) {
			
		}
	}
}
