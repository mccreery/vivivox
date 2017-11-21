package tk.nukeduck.vivivox.gui;

import tk.nukeduck.vivivox.player.Player;
import tk.nukeduck.vivivox.world.World;

public class GuiElement {
	protected int x = 0, y = 0;
	
	public GuiElement(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void render(World world, Player player) {
		// This is where you should be rendering your thing.
	}
	
	public boolean onClick(int button) {
		// Handle mouse clicks (0 = left mouse, 1 = right, 2 = Middle)
		return false;
	}
	
	public boolean onKeyPress(int key) {
		// Handle keyboard keys (Keyboard.KEY_X)
		return false;
	}
	
	public void update() {
		// Used only when updating after changing values
	}
}