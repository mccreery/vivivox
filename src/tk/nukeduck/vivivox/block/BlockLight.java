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
		// X
		if(world.getLightLevel(x - 1, y, z) < lightLevel - 1/* && !world.getBlock(x - 1, y, z).isOpaque()*/) {
			world.setLightLevel(x - 1, y, z, (byte) (lightLevel - 1));
			process(x - 1, y, z, (byte) (lightLevel - 1), world);
		}
		
		if(world.getLightLevel(x + 1, y, z) < lightLevel - 1/* && !world.getBlock(x + 1, y, z).isOpaque()*/) {
			world.setLightLevel(x + 1, y, z, (byte) (lightLevel - 1));
			process(x + 1, y, z, (byte) (lightLevel - 1), world);
		}
		
		// Y
		if(world.getLightLevel(x, y - 1, z) < lightLevel - 1/* && !world.getBlock(x, y - 1, z).isOpaque()*/) {
			world.setLightLevel(x, y - 1, z, (byte) (lightLevel - 1));
			process(x, y - 1, z, (byte) (lightLevel - 1), world);
		}
		
		if(world.getLightLevel(x, y + 1, z) < lightLevel - 1/* && !world.getBlock(x, y + 1, z).isOpaque()*/) {
			world.setLightLevel(x, y + 1, z, (byte) (lightLevel - 1));
			process(x, y + 1, z, (byte) (lightLevel - 1), world);
		}
		
		// Z
		if(world.getLightLevel(x, y, z - 1) < lightLevel - 1/* && !world.getBlock(x, y, z - 1).isOpaque()*/) {
			world.setLightLevel(x, y, z - 1, (byte) (lightLevel - 1));
			process(x, y, z - 1, (byte) (lightLevel - 1), world);
		}
		
		if(world.getLightLevel(x, y, z + 1) < lightLevel - 1/* && !world.getBlock(x, y, z + 1).isOpaque()*/) {
			world.setLightLevel(x, y, z + 1, (byte) (lightLevel - 1));
			process(x, y, z + 1, (byte) (lightLevel - 1), world);
		}
		} catch(Exception e) {
			
		}
	}
}
