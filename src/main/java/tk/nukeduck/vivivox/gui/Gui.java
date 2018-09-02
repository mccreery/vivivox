package tk.nukeduck.vivivox.gui;

import java.util.ArrayList;

import tk.nukeduck.vivivox.VivivoxMain;

public class Gui {
	private ArrayList<GuiElement> guiElements;
	
	public Gui() {
		guiElements = new ArrayList<GuiElement>();
	}
	
	public void drawGUI() {
		for(GuiElement e : guiElements) {
			e.render(VivivoxMain.world, VivivoxMain.player);
		}
	}
	
	private boolean shouldPause = true;
	
	public boolean shouldPause() {
		return this.shouldPause;
	}
	
	public void addElement(GuiElement e) {
		guiElements.add(e);
	}
	
	public void removeElement(GuiElement e) {
		guiElements.remove(e);
	}
	
	public GuiElement getElementAt(int i) {
		return guiElements.get(i);
	}
	
	public void removeElementAt(int i) {
		guiElements.remove(i);
	}
	
	public int getIndexOfElement(GuiElement e) {
		return guiElements.indexOf(e);
	}
}