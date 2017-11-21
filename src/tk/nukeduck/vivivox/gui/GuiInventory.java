package tk.nukeduck.vivivox.gui;

import org.lwjgl.opengl.Display;

public class GuiInventory extends Gui {
	public GuiInventory() {
		GuiElementButton close = new GuiElementButton(Display.getWidth() - 48, 16, 32, 32);
		close.text = "X";
		addElement(close);
	}
}
