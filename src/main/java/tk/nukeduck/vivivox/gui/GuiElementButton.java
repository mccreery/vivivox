package tk.nukeduck.vivivox.gui;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import tk.nukeduck.vivivox.Fonts;
import tk.nukeduck.vivivox.helper.Render;
import tk.nukeduck.vivivox.player.Player;
import tk.nukeduck.vivivox.world.World;

public class GuiElementButton extends GuiElement {
	String text = "";
	int width = 128, height = 64;
	
	public GuiElementButton(int x, int y, int width, int height) {
		super(x, y);
		this.width = width;
		this.height = height;
		text = "Button";
	}
	
	public GuiElementButton(int x, int y, int width, int height, String textInput) {
		this(x, y, width, height);
		text = textInput;
	}
	
	@Override
	public void render(World world, Player player) {
		Render.drawBorderedRectangle(x, y, width, height, Color.gray, Color.black);
		
		// Left
		
		World.button.draw(x, y, x + 7, y + 15, 0, 0, 7, 15);
		World.button.draw(x, y + 15, x + 7, y + height - 8, 0, 15, 7, 15);
		World.button.draw(x, y + height - 8, x + 7, y + height, 0, 16, 7, 24);
		
		// Right
		
		World.button.draw(x + width - 7, y, x + width + 1, y + 15, 9, 0, 17, 15);
		World.button.draw(x + width - 7, y + 15, x + width + 1, y + height - 8, 9, 15, 17, 15);
		World.button.draw(x + width - 7, y + height - 8, x + width + 1, y + height, 9, 16, 17, 24);
		
		// Middle
		
		World.button.draw(x + 7, y, x + width - 7, y + 15, 7, 0, 8, 15);
		World.button.draw(x + 7, y + 15, x + width - 7, y + height - 8, 7, 15, 8, 15);
		World.button.draw(x + 7, y + height - 8, x + width - 7, y + height, 7, 16, 8, 24);
		
		Color textColor = Mouse.getX() >= x && Mouse.getX() <= x + width && Mouse.getY() >= y && Mouse.getY() <= y + height ? Color.gray : Color.white;
		Render.drawString(x + ((width - Fonts.hudFont.getWidth(text)) / 2), y + ((height - Fonts.hudFont.getHeight()) / 2), text, textColor, Fonts.hudFont.getFont());
	}
}