package tk.nukeduck.vivivox;

import org.newdawn.slick.SlickException;

import tk.nukeduck.vivivox.helper.FontTools;

public class Fonts {
	public static Font hudFont;

	public static void initFonts(){
		try {
			hudFont = new Font(FontTools.createFont("Laconic Regular.ttf", false, false, 20));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
