package tk.nukeduck.vivivox.helper;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Render
{
	private static Graphics g;
	
	public static void initGraphics(){
		g = new Graphics();
	}
	
    public static void renderStatusBar(int x, int y, int width, int height, int value, int maxValue, Color color)
    {
    	g.setColor(new Color(color.r - 0.5F, color.g - 0.5F, color.b - 0.5F, color.a - 0.5F));
    	g.drawRect(x + 4, y + 4, width, height);
    	g.fillRect(x + 4, y + 4, Math.round(value * width / maxValue), height);
    	
    	g.setColor(color);
    	g.drawRect(x, y, width, height);
    	g.fillRect(x, y, Math.round(value * width / maxValue), height);
    }
    
    public static void renderFilledStatusBar(int x, int y, int width, int height, int value, int maxValue, Color color, Color backColor)
    {
    	g.setColor(new Color(backColor.r - 0.5F, backColor.g - 0.5F, backColor.b - 0.5F, backColor.a - 0.5F));
    	g.fillRect(x + 4, y + 4, width, height);
    	
    	g.setColor(new Color(color.r - 0.5F, color.g - 0.5F, color.b - 0.5F, color.a - 0.5F));
    	g.fillRect(x + 4, y + 4, Math.round(value * width / maxValue), height);
    	
    	
    	g.setColor(backColor);
    	g.fillRect(x, y, width, height);
    	
    	g.setColor(color);
    	g.fillRect(x, y, Math.round(value * width / maxValue), height);
    }
}
