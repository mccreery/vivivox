package tk.nukeduck.vivivox.player;

import tk.nukeduck.vivivox.block.Block;

public class PlayerMovement {
	public static void walkForwards(float distance, Player player) {
		float xPositionMoved = distance * (float) Math.sin(Math.toRadians(player.getRotationYaw()));
		float zPositionMoved = 0 - distance * (float) Math.cos(Math.toRadians(player.getRotationYaw()));
		
		float clipToX = 0f;
		float clipToZ = 0f;
		
		if(((player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z).isOpaque()))
				&& player.position.x + xPositionMoved > (float)((int) player.position.x + 1) - player.radius) {
			clipToX = (float)((int) player.position.x + 1) - player.radius;
		} else if(((player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z).isOpaque()))
				&& player.position.x + xPositionMoved < (float)((int) player.position.x) + player.radius) {
			clipToX = (float)((int) player.position.x) + player.radius;
		}
		
		if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1).isOpaque()))
				&& player.position.z + zPositionMoved > (float)((int) player.position.z + 1) - player.radius) {
			clipToZ = (float)((int) player.position.z + 1) - player.radius;
		} else if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1).isOpaque()))
				&& player.position.z + zPositionMoved < (float)((int) player.position.z) + player.radius) {
			clipToZ = (float)((int) player.position.z) + player.radius;
		}
		
		if(clipToX != 0) {
			player.position.x = clipToX;
		} else {
			player.position.x += xPositionMoved;
		}
		
		if(clipToZ != 0) {
			player.position.z = clipToZ;
		} else {
			player.position.z += zPositionMoved;
		}
	}
	
	public static void walkBackwards(float distance, Player player) {
		float xPositionMoved = distance * (float) Math.sin(Math.toRadians(player.getRotationYaw() + 180));
		float zPositionMoved = 0 - distance * (float) Math.cos(Math.toRadians(player.getRotationYaw() + 180));
		
		float clipToX = 0f;
		float clipToZ = 0f;
		
		if(((player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z).isOpaque())) &&
				player.position.x + xPositionMoved > (float)((int) player.position.x + 1) - player.radius) clipToX = (float)((int) player.position.x + 1) - player.radius;
		else if(((player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z).isOpaque())) &&
				player.position.x + xPositionMoved < (float)((int) player.position.x) + player.radius) clipToX = (float)((int) player.position.x) + player.radius;
		
		if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1).isOpaque())) &&
				player.position.z + zPositionMoved > (float)((int) player.position.z + 1) - player.radius) clipToZ = (float)((int) player.position.z + 1) - player.radius;
		else if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1).isOpaque())) &&
				player.position.z + zPositionMoved < (float)((int) player.position.z) + player.radius) clipToZ = (float)((int) player.position.z) + player.radius;
		
		if(clipToX != 0) {
			player.position.x = clipToX;
		} else {
			player.position.x += xPositionMoved;
		}
		
		if(clipToZ != 0) {
			player.position.z = clipToZ;
		} else {
			player.position.z += zPositionMoved;
		}
	}
	
	public static void strafeLeft(float distance, Player player) {
		float xPositionMoved = distance * (float) Math.sin(Math.toRadians(player.getRotationYaw() - 90));
		float zPositionMoved = 0 - distance * (float) Math.cos(Math.toRadians(player.getRotationYaw() - 90));
		
		float clipToX = 0f;
		float clipToZ = 0f;
		
		if(((player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z).isOpaque())) &&
				player.position.x + xPositionMoved > (float)((int) player.position.x + 1) - player.radius) clipToX = (float)((int) player.position.x + 1) - player.radius;
		else if(((player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z).isOpaque())) &&
				player.position.x + xPositionMoved < (float)((int) player.position.x) + player.radius) clipToX = (float)((int) player.position.x) + player.radius;
		
		if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1).isOpaque())) &&
				player.position.z + zPositionMoved > (float)((int) player.position.z + 1) - player.radius) clipToZ = (float)((int) player.position.z + 1) - player.radius;
		else if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1).isOpaque())) &&
				player.position.z + zPositionMoved < (float)((int) player.position.z) + player.radius) clipToZ = (float)((int) player.position.z) + player.radius;
		
		if(clipToX != 0) {
			player.position.x = clipToX;
		} else {
			player.position.x += xPositionMoved;
		}
		
		if(clipToZ != 0) {
			player.position.z = clipToZ;
		} else {
			player.position.z += zPositionMoved;
		}
	}
	
	public static void strafeRight(float distance, Player player) {
		float xPositionMoved = distance * (float) Math.sin(Math.toRadians(player.rotationYaw + 90));
		float zPositionMoved = 0 - distance * (float) Math.cos(Math.toRadians(player.rotationYaw + 90));
		
		float clipToX = 0f;
		float clipToZ = 0f;
		
		if(((player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z).isOpaque())) &&
				player.position.x + xPositionMoved > (float)((int) player.position.x + 1) - player.radius) clipToX = (float)((int) player.position.x + 1) - player.radius;
		else if(((player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z).isOpaque())) &&
				player.position.x + xPositionMoved < (float)((int) player.position.x) + player.radius) clipToX = (float)((int) player.position.x) + player.radius;
		
		if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1).isOpaque())) &&
				player.position.z + zPositionMoved > (float)((int) player.position.z + 1) - player.radius) clipToZ = (float)((int) player.position.z + 1) - player.radius;
		else if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1).isOpaque())) &&
				player.position.z + zPositionMoved < (float)((int) player.position.z) + player.radius) clipToZ = (float)((int) player.position.z) + player.radius;
		
		if(clipToX != 0) {
			player.position.x = clipToX;
		} else {
			player.position.x += xPositionMoved;
		}
		
		if(clipToZ != 0) {
			player.position.z = clipToZ;
		} else {
			player.position.z += zPositionMoved;
		}
	}
	
	/*public static void updateYPosition(Player player) {
		float xPositionMoved = distance * (float) Math.sin(Math.toRadians(player.rotationYaw + 90));
		float zPositionMoved = 0 - distance * (float) Math.cos(Math.toRadians(player.rotationYaw + 90));
		
		float clipToX = 0f;
		float clipToZ = 0f;
		
		if(((player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x + 1, (int) player.position.y + 1, (int) player.position.z).isOpaque())) &&
				player.position.x + xPositionMoved > (float)((int) player.position.x + 1) - player.radius) clipToX = (float)((int) player.position.x + 1) - player.radius;
		else if(((player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y, (int) player.position.z).isOpaque()) ||
				(player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != Block.air && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z) != null && player.world.getBlock((int) player.position.x - 1, (int) player.position.y + 1, (int) player.position.z).isOpaque())) &&
				player.position.x + xPositionMoved < (float)((int) player.position.x) + player.radius) clipToX = (float)((int) player.position.x) + player.radius;
		
		if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z + 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z + 1).isOpaque())) &&
				player.position.z + zPositionMoved > (float)((int) player.position.z + 1) - player.radius) clipToZ = (float)((int) player.position.z + 1) - player.radius;
		else if(((player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y, (int) player.position.z - 1).isOpaque()) ||
				(player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != Block.air && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1) != null && player.world.getBlock((int) player.position.x, (int) player.position.y + 1, (int) player.position.z - 1).isOpaque())) &&
				player.position.z + zPositionMoved < (float)((int) player.position.z) + player.radius) clipToZ = (float)((int) player.position.z) + player.radius;
		
		if(clipToX != 0) {
			player.position.x = clipToX;
		} else {
			player.position.x += xPositionMoved;
		}
		
		if(clipToZ != 0) {
			player.position.z = clipToZ;
		} else {
			player.position.z += zPositionMoved;
		}
	}*/
}