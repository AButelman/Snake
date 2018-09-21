package miSnake;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Menu extends JPanel {

	private String message;
	private String[] options;
	private String name;
	private boolean showed;
	private int selected = 0;
	private Font font;
	private Font boldFont;
	private FontMetrics fmRegular;
	private FontMetrics fmBold;
	private Color backgroundColor;
	private Color selectedColor;
	private Color nonSelectedColor;
	
	public Menu(String name, String message, String[] options, Color backgroundColor, Color selectedColor, Color nonSelectedColor) {
		this.name = name;
		this.message = message;
		this.options = options;
		this.backgroundColor = backgroundColor;
		this.selectedColor = selectedColor;
		this.nonSelectedColor = nonSelectedColor;
		
		this.showed = false;
		
		/* File fontFile = null;
		fontFile = new File("manaspc.ttf");
		
		try {
			Font originalFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			font = originalFont.deriveFont(Font.PLAIN,80);
			boldFont = originalFont.deriveFont(Font.BOLD, 80);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font); */
        
		font = new Font("manaspace", Font.PLAIN, 80);
		boldFont = font.deriveFont(Font.BOLD, 80);
		
		this.setBackground(backgroundColor);
		this.setFocusable(true);
	}
	
	public String getName() { return name; }
	public void setShowed(boolean showed) { this.showed = showed; }
	public boolean isShowed() { return showed; }
	public String[] getOptions() { return options; }
	public void setOptions(String[] options) { this.options = options; }
	public int getSelected() { return selected; }
	public void setSelected(int selected) { 
		if (selected < 0) selected = options.length - 1;
		if (selected > options.length - 1) selected = 0;
		
		this.selected = selected; 
	}
	public Font getFont() { return font; }
	public void setFont(Font font) { this.font = font; }
	public Font getBoldFont() { return boldFont; }
	public void setBoldFont(Font boldFont) { this.boldFont = boldFont; }
	public Color getBackgroundColor() { return backgroundColor; }
	public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }
	public Color getSelectedColor() { return selectedColor; }
	public void setSelectedColor(Color selectedColor) {	this.selectedColor = selectedColor;	}
	public Color getNonSelectedColor() { return nonSelectedColor; }
	public void setNonSelectedColor(Color nonSelectedColor) { this.nonSelectedColor = nonSelectedColor; }

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		
		fmRegular = g2d.getFontMetrics(font);
		fmBold = g2d.getFontMetrics(boldFont);
		
		if (message == null || message.equals("")) {
			drawVerticalOptions(g2d);
		} else {
			drawMessageHorizontalOptions(g2d);
		}
	}

	private void drawMessageHorizontalOptions(Graphics2D g2d) {
		
		validateMessageLength();
		
		g2d.setFont(font);
		g2d.setColor(nonSelectedColor);
		
		int messageWidth = fmRegular.stringWidth(message);
		int margin = fmRegular.stringWidth("  ");
		int maxWidth = this.getWidth() - margin * 2;
		
		int x = 0; 
		int y = this.getHeight() / 2 - calculateMessageHeight();
		
		if (messageWidth < maxWidth) {
			x = this.getWidth() / 2 - messageWidth / 2;
			g2d.drawString(message, x, y);
			
		} else {
			
			drawMultilineMessage(g2d, maxWidth, y);	
		}
		
		drawHorizontalMenu(g2d, calculateMenuWidth());
	}

	private void drawMultilineMessage(Graphics2D g2d, int maxWidth, int y) {
		int x;
		String[] lines = message.split("\n");
		
		for (int i = 0; i < lines.length; i++) {
			
			String[] words = lines[i].split(" ");
			
			String line;
			int lineWidth;
			int j = 0;
			
			while (j < words.length) {
			
				lineWidth = 0;
				line = "";
				do {
					
					line += words[j] + " ";
					j++;
					
					lineWidth = fmRegular.stringWidth(line);
				} while (lineWidth < maxWidth && j < words.length);
				
				x = this.getWidth() / 2 - lineWidth / 2;
				
				g2d.drawString(line, x, y);
				
				y += fmRegular.getHeight();
			}
		}
	}
	
	private void validateMessageLength() {
		if (message.length() > maxLettersOnHalfScreen()) {
       
           	System.err.println("Message too long for the desired font.");
           	System.exit(ERROR);
        }
	}
	
	private int maxLettersOnHalfScreen() {
		
		int lettersInCol = this.getHeight() / fmRegular.getHeight();
		int maxLetters = lettersInLine() * (lettersInCol / 2);	// Only top half of the screen
		
		return maxLetters;
	}

	private int lettersInLine() {
		
		return this.getWidth() / fmRegular.stringWidth("A");
	}
	
	private int calculateMessageHeight() {
		int messageHeight;
		
		int lines = message.length() / lettersInLine();
		// if (message.length() % lettersInLine() != 0 ) lines++;
		
		char character;
		for (int i = 0; i < message.length(); i++) {
			character = message.charAt(i);
			if (character == '\n') lines++;
		}

		messageHeight = lines * fmRegular.getHeight();
		
		return messageHeight;
	}

	private void drawHorizontalMenu(Graphics2D g2d, int menuWidth) {
		
		int y = this.getHeight() * 2/3;
		int x = this.getWidth() / 2 - menuWidth / 2;
		
		String option;
		for (int i = 0; i < options.length; i++) {
		
			option = options[i];
			if (option != null) {
				
				if (i == selected) {
					g2d.setFont(boldFont);
					g2d.setColor(selectedColor);
					option = "> " + option + " <";
				} else {
					g2d.setFont(font);
					g2d.setColor(nonSelectedColor);
					option = "  " + option + "  ";
				}
				
				g2d.drawString(option, x, y);
				
				x += fmBold.stringWidth(option);
			}
		}
	}

	private int calculateMenuWidth() {
		int totalMenuWidth = 0;
		String option;
		for (int i = 0; i < options.length; i++) {
			option = options[i];
			
			if (option != null) {
				totalMenuWidth += fmBold.stringWidth(option);
			}
		}
		
		totalMenuWidth += fmBold.stringWidth(">  <") * options.length;
		
		return totalMenuWidth;
	}

	private void drawVerticalOptions(Graphics2D g2d) {
	
		// FALTA ARREGLAR EL ESPACIADO VERTICAL
		int totalMenuHeight = fmBold.getHeight() * (options.length * 2 - 2);
		int menuStart = this.getHeight() / 2 - totalMenuHeight / 2;
		int y = menuStart;
		int x;
		
		String option;
		for (int i = 0; i < options.length; i++) {
			if (i == selected) {
				g2d.setFont(boldFont);
				g2d.setColor(selectedColor);
				option = "> " + options[i] + " <";
				x = this.getWidth() / 2 - fmBold.stringWidth(option) / 2;
			} else {
				g2d.setFont(font);
				g2d.setColor(nonSelectedColor);
				option = options[i];
				x = this.getWidth() / 2 - fmRegular.stringWidth(option) / 2;
			}
			
			g2d.drawString(option, x, y);
			
			if (i == selected) {
				y += fmBold.getHeight() * 2;
			} else {
				y += fmRegular.getHeight() * 2;
			}
		}
	}
	
	public void addMenuListener(MenuListener listener) {
		listener.setMenu(this);
		this.addKeyListener(listener);
	}
}
