package tk.nukeduck.vivivox.world.generator;

import tk.nukeduck.vivivox.world.World;

public abstract class Generator {
	public abstract void generate(int x, int y, int z, World world);
}