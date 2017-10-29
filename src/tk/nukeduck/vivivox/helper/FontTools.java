package tk.nukeduck.vivivox.helper;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import tk.nukeduck.vivivox.Font;

public class FontTools {
	@SuppressWarnings("unchecked")
	public static UnicodeFont createFont(String resSource, boolean bold, boolean italic) throws SlickException {
		UnicodeFont font; {
			font = new UnicodeFont(resSource, 18, bold, italic);
			font.addAsciiGlyphs();
			
			ColorEffect e = new ColorEffect();
			e.setColor(java.awt.Color.white);
			
			font.getEffects().add(e);
			
			font.loadGlyphs();
		}
		return font;
	}
	
	public static void renderText(int x, int y, String text, Color color, Font font) {
		font.getFont().drawString(x, y, text, color);
	}
	
	public static void renderTextWithShadow(int x, int y, String text, Color color, Font font) {
		// Render shadow
		font.getFont().drawString(x + 4, y + 4, text, new Color(color.r - 0.5F, color.g - 0.5F, color.b - 0.5F, color.a - 0.5F));
		// Render text
		font.getFont().drawString(x, y, text, color);
	}
}