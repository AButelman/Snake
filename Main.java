package miSnake;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {

		if (!fontExist("manaspace")) {
			registerFont("manaspc.ttf");
		}
		
		Game.getGame().start();
	}
	
	public static boolean fontExist(String fontName) {
		boolean exist = false;
		
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fonts = g.getAvailableFontFamilyNames();
        
		int i = 0;
		
		while (!exist && i < fonts.length) {
			
			if (fonts[i].equals(fontName)) {
				exist = true;
			} else {
				i++;
			}
		}
		
		return exist;
	}
	
	public static void registerFont(String fontName) {
		
		File fontFile = new File(fontName);
		Font originalFont, font = null;
		
		try {
			originalFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			font = originalFont.deriveFont(Font.PLAIN,80);
			// boldFont = originalFont.deriveFont(Font.BOLD, 80);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
	}
}
