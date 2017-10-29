package tk.nukeduck.vivivox.world.generator;

import org.lwjgl.util.vector.Vector3f;

import tk.nukeduck.vivivox.Main;
import tk.nukeduck.vivivox.block.Block;
import tk.nukeduck.vivivox.helper.VectorTools;
import tk.nukeduck.vivivox.world.World;

public class GeneratorTree extends Generator {
	Block log = Block.log;
	Block leaves = Block.leaves;
	
	int height;
	int extensionHeight;
	
	int boughAmount;
	int boughSize;
	
	int boughSpreadX, boughSpreadY, boughSpreadZ;
	
	int branchVariation;
	
	float resetRate;
	float resetToLogRate;
	
	public GeneratorTree(int height, int extensionHeight, int boughAmount, int boughSize, int boughSpread, int branchVariation, float resetRate, float resetToLogRate) {
		this.height = height;
		this.extensionHeight = extensionHeight;
		this.boughAmount = boughAmount;
		this.boughSize = boughSize;
		this.boughSpreadX = boughSpread;
		this.boughSpreadY = boughSpread;
		this.boughSpreadZ = boughSpread;
		this.branchVariation = branchVariation;
		this.resetRate = resetRate;
		this.resetToLogRate = resetToLogRate;
	}
	
	public GeneratorTree(int height, int extensionHeight, int boughAmount, int boughSize, int boughSpread, int branchVariation, float resetRate, float resetToLogRate, Block log, Block leaves) {
		this(height, extensionHeight, boughAmount, boughSize, boughSpread, branchVariation, resetRate, resetToLogRate);
		this.log = log;
		this.leaves = leaves;
	}
	
	public GeneratorTree(int height, int extensionHeight, int boughAmount, int boughSize, int boughSpreadX, int boughSpreadY, int boughSpreadZ, int branchVariation, float resetRate, float resetToLogRate) {
		this.height = height;
		this.extensionHeight = extensionHeight;
		this.boughAmount = boughAmount;
		this.boughSize = boughSize;
		this.boughSpreadX = boughSpreadX;
		this.boughSpreadY = boughSpreadY;
		this.boughSpreadZ = boughSpreadZ;
		this.branchVariation = branchVariation;
		this.resetRate = resetRate;
		this.resetToLogRate = resetToLogRate;
	}
	
	public GeneratorTree(int height, int extensionHeight, int boughAmount, int boughSize, int boughSpreadX, int boughSpreadY, int boughSpreadZ, int branchVariation, float resetRate, float resetToLogRate, Block log, Block leaves) {
		this(height, extensionHeight, boughAmount, boughSize, boughSpreadX, boughSpreadY, boughSpreadZ, branchVariation, resetRate, resetToLogRate);
		this.log = log;
		this.leaves = leaves;
	}
	
	@Override
	public void generate(int x, int y, int z, World world) {
		int treeHeight = Main.random.nextInt(height / 2) + (height / 2);
		
		int logExtension = Main.random.nextInt(extensionHeight / 2) + (extensionHeight / 2);
		
		for(int i = 0; i < treeHeight + logExtension; i++) {
			world.setBlock(x, y + i, z, Block.log);
		}
		
		Vector3f boughStart = new Vector3f(x + 0.5F, y + treeHeight + 0.5F + Main.random.nextInt(branchVariation) - (branchVariation / 2), z + 0.5F);
		
		// Generate a random amount of boughs
		for(int i = 0; i < Main.random.nextInt(boughAmount) + 2; i++) {
			Vector3f boughLocation = new Vector3f(boughStart.x, boughStart.y + logExtension, boughStart.z);
			
			boughLocation.x += Main.random.nextInt(boughSpreadX) - (boughSpreadX / 2);
			boughLocation.y += Main.random.nextInt(boughSpreadY) - (boughSpreadY / 2);
			boughLocation.z += Main.random.nextInt(boughSpreadZ) - (boughSpreadZ / 2);
			
			int boughRadius = Main.random.nextInt(boughSize) + boughSize / 2;
			
			for(int x2 = (int) (boughLocation.x - boughRadius); x2 < boughLocation.x + boughRadius; x2++) {
				for(int y2 = (int) (boughLocation.y - boughRadius); y2 < boughLocation.y + boughRadius; y2++) {
					for(int z2 = (int) (boughLocation.z - boughRadius); z2 < boughLocation.z + boughRadius; z2++) {
						if(x2 > 0 && y2 > 0 && z2 > 0 && 
								x2 < Main.world.worldSize && 
								y2 < Main.world.worldHeight && 
								z2 < Main.world.worldSize && 
								VectorTools.getDistance(boughLocation, x2, y2, z2) < boughRadius/* + perlinNoise(((double)x2 + worldOffset) / 10, ((double)z2 + worldOffset) / 10, 1) / 100*/) {
							if(world.getBlock(x2, y2, z2) == Block.air) world.setBlock(x2, y2, z2, leaves);
						}
					}
				}
			}
			
			// Draw branch
			
			Vector3f branch = new Vector3f(boughStart.x, boughStart.y, boughStart.z);
			
			// Continue string of branches
			if(Main.random.nextFloat() <= resetRate) {
				if(Main.random.nextFloat() <= resetToLogRate) boughStart = new Vector3f(x + 0.5F, y + treeHeight + 0.5F + Main.random.nextInt(branchVariation) - (branchVariation / 2), z + 0.5F);
				else boughStart = branch;
			}
			
			while(VectorTools.getDistance(branch, boughLocation) > 1) {
				if(branch.x < boughLocation.x) branch.x ++;
				if(branch.y < boughLocation.y) branch.y ++;
				if(branch.z < boughLocation.z) branch.z ++;
				
				if(branch.x > boughLocation.x) branch.x --;
				if(branch.y > boughLocation.y) branch.y --;
				if(branch.z > boughLocation.z) branch.z --;
				
				if(branch.x > 0 && branch.y > 0 && branch.z > 0 && branch.x < Main.world.worldSize && branch.y < Main.world.worldHeight && branch.z < Main.world.worldSize) world.setBlock((int) branch.x, (int) branch.y, (int) branch.z, log);
			}
		}
	}
}