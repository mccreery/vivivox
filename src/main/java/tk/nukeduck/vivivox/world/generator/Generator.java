package tk.nukeduck.vivivox.world.generator;

import tk.nukeduck.vivivox.world.World;

public abstract class Generator {
	public abstract void generate(World world, int x, int y, int z);
}