package miSnake;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class SnakePart {
	
	private int x;
	private int y;
	private int side;
	private Color color;
	
	public SnakePart(int x, int y, int side, Color color) {
		super();
		this.x = x;
		this.y = y;
		this.side = side;
		this.color = color;
	}
	
	@Override
	protected SnakePart clone() {
		int b = color.getBlue();
		int r = color.getRed() + 20;
		if (r > 255) {
			r = 255;
			b += 20;
			
			if (b > 255) {
				b = 255;
			}
		}
		
		int g = color.getGreen();
		
		Color newColor = new Color(r, g, b);
		System.out.println("Color: " + newColor);
	    return new SnakePart(x, y, side, newColor);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, side, side);
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getSide() {
		return side;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) { this.color = color; }
}
