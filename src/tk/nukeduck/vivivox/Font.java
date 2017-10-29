package tk.nukeduck.vivivox;

import org.newdawn.slick.UnicodeFont;

public class Font {
	private UnicodeFont font;
	int height;
	
	public Font(UnicodeFont font) {
		this.setFont(font);
		this.height = font.getHeight("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
	}
	
	public void setFont(UnicodeFont font) {
		this.font = font;
	}
	
	public UnicodeFont getFont() {
		return this.font;
	}
	
	public int getHeight() {
		return this.height;
	}

	public int getWidth(String text) {
		return this.getFont().getWidth(text);
	}
}