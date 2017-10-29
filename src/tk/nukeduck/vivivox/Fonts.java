package tk.nukeduck.vivivox;

import org.newdawn.slick.SlickException;

import tk.nukeduck.vivivox.helper.FontTools;

public class Fonts {
	public static Font hudFont;
	
	public static void initFonts(){
		try {
			hudFont = new Font(FontTools.createFont("src/Laconic Regular.ttf", false, false));
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}