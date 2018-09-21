package miSnake;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel implements KeyListener {

	public static final int PREFERRED_SIDE = 20;

	public static final int INITIAL_PARTS = 1;
	
	public static Dimension screenDimension;
	
	private int side;
	private Frame window;
	private Toolkit tk;
	private SnakePart apple;
	private ArrayList<SnakePart> snakeParts;
	private Animation anim;
	private Thread t;
	
	private int panelHeight;
	private int panelWidth;
	private int xGrid;
	private int yGrid;
	private int randomXGrid;
	private int randomYGrid;
	private int randomXPos;
	private int randomYPos;
	
	private int points;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean pause;
	
	public Board() {
		getBoardSize();
		setSideSize(PREFERRED_SIDE);
		
		snakeParts = new ArrayList<SnakePart>();
		initialize();
		
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setBackground(Color.BLACK);
	}
	
	public void reset() {
		snakeParts = new ArrayList<SnakePart>();
		initialize();
	}
	
	public void setWindow(Frame window) { this.window = window; }
	
	private void setSideSize(int sideLength) {
		
		side = sideLength;
		
		while ((panelWidth % side != 0) && (panelHeight % side != 0)) {
			side++;
		}
	}

	private void getBoardSize() {
		tk = Toolkit.getDefaultToolkit();
		screenDimension = tk.getScreenSize();
		panelWidth = screenDimension.width;
		panelHeight = screenDimension.height;
	}
	
	public void initialize() {
		pause = false;
		
		randomizeDirection();
		
		points = 0;
		xGrid = panelWidth / side;
		yGrid = panelHeight / side;

		apple = randomApple();
		
		randomHeadStart();
		
		SnakePart head = new SnakePart(randomXPos, randomYPos, side, Color.RED);
		snakeParts.add(head);
		//snakeParts.add(new SnakePart(window.getWidth() / 2 - SIDE / 2, window.getHeight() / 2 - SIDE / 2, SIDE, Color.RED));
		
		for (int i = 1; i <= INITIAL_PARTS - 1; i++) {
			snakeParts.add(new SnakePart(randomXPos, randomYPos + side * i, side, Color.GREEN));	
		}
	}
	
	private void randomizeDirection() {
		up = false;
		down = false;
		left = false;
		right = false;
		
		int dice = (int) (Math.random() * 4);
		
		switch (dice) {
			case(0):
				up = true;
				break;
			case(1):
				down = true;
				break;
			case(2):
				left = true;
				break;
			case(3):
				right = true;
				break;
		}
	}

	public void startAnimation() {
	
		anim = new Animation(this);
		t = new Thread(anim);
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}
	
	public void stopAnimation() {
		anim.stop();
		t = null;
		anim = null;
	}
	
	public void accelerate() { anim.accelerate(); }

	private void randomHeadStart() {
		randomXGrid = (int) ((Math.random() * (xGrid - 5)) + 5);	// Leave a margin

		// POR AHORA LAS PARTES INICIALES VAN SIEMPRE ABAJO, CON LO CUAL EL BICHO SE MUEVE SIEMPRE PARA ARRIBA AL PRINCIPIO
		// ARREGLAR!
		randomYGrid = (int) ((Math.random() * (yGrid - INITIAL_PARTS - 5)) + 5); 
		randomXPos = randomXGrid * side;
		randomYPos = randomYGrid * side;
	}
	
	private SnakePart randomApple() {
		SnakePart newApple;
		boolean posOnEmptySpace = true;
		
		// Check that the new apple doesn't collide with the snake
		do {
			randomGridPos();
			newApple = new SnakePart(randomXPos, randomYPos, side, Color.CYAN);
			
			for (SnakePart snakePart : snakeParts) {
				if (newApple.getBounds().intersects(snakePart.getBounds())) {
					posOnEmptySpace = false;
				}
			} 
		} while (!posOnEmptySpace);
		
		return newApple;
	}

	private void randomGridPos() {
		randomXGrid = (int) (Math.random() * xGrid);
		randomYGrid = (int) (Math.random() * yGrid);
		randomXPos = randomXGrid * side;
		randomYPos = randomYGrid * side;
	}

	public ArrayList<SnakePart> getSnakeParts() { return snakeParts; }
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		 
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		 rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		 g2d.setRenderingHints(rh);
		 
		for (SnakePart snake : snakeParts) {
			g2d.setColor(snake.getColor());
			g2d.fill3DRect(snake.getX(), snake.getY(), snake.getSide(), snake.getSide(), true);
			g2d.draw3DRect(snake.getX(), snake.getY(), snake.getSide(), snake.getSide(), true);
		}
		
		g2d.setColor(apple.getColor());
		g2d.fill3DRect(apple.getX(), apple.getY(), apple.getSide(), apple.getSide(), true);
		g2d.draw3DRect(apple.getX(), apple.getY(), apple.getSide(), apple.getSide(), true);
		 
		
		g2d.setColor(Color.WHITE);
		String stringPoints = Integer.toString(points);
		g2d.setFont(new Font("Verdana", Font.PLAIN, 80));
		g2d.drawString(stringPoints, 40, 90);
	}

	public void moveSnake() {
		
		if (!pause) {
			// Move the snake parts
			for (int i = snakeParts.size() - 1; i > 0; i--) {
				snakeParts.get(i).setX(snakeParts.get(i - 1).getX());
				snakeParts.get(i).setY(snakeParts.get(i - 1).getY());
			}
			
			// Move the head
			SnakePart head = snakeParts.get(0);
			if (up) {
				head.setY(head.getY() - side);
			} else if (down) {
				head.setY(head.getY() + side);
			} else if (left) {
				head.setX(head.getX() - side);
			} else if (right) {
				head.setX(head.getX() + side);
			}
			
			checkCollisions();
		}
	}

	private void checkCollisions() {
		SnakePart head = snakeParts.get(0);
		int headX = head.getX();
		int headY = head.getY();
		
		// Borders
		if (headX < 0 || headX + side > this.getWidth() || headY < 0 || headY + side > this.getHeight()) {
			gameOver();
		}
		
		// Snake vs Snake
		for (int i = 1; i < snakeParts.size(); i++) {
			if (head.getBounds().intersects(snakeParts.get(i).getBounds())) {
				gameOver();
			}
		}
		
		// Snake vs Apple
		if (head.getBounds().intersects(apple.getBounds())) {
			apple = randomApple();
			newSnakePart();
			accelerate();
			points++;
		}
	}

	private void newSnakePart() {
		SnakePart lastSnakePart = snakeParts.get(snakeParts.size() - 1);
		
		SnakePart newSnakePart =  lastSnakePart.clone();
		
		if (snakeParts.size() > 1) {	
			snakeParts.add(newSnakePart);
		} else {
			newSnakePart.setColor(Color.GREEN);
			snakeParts.add(newSnakePart);
		}
	}

	private void gameOver() {

		stopAnimation();
		reset();
		Game.getGame().changeMenu("gameOver");
	}
	
	void hideCursor() {
		// Remove the mouse cursor from the window
		BufferedImage blankImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankImg, new Point(0, 0), "");
		window.setCursor(blankCursor);
	}
	
	private void showCursor() {
		window.setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || (e.getKeyCode() == KeyEvent.VK_LEFT) 
				|| e.getKeyCode() == KeyEvent.VK_RIGHT) && !pause) {
		
			up = false;
			down = false;
			left = false;
			right = false;
			
			switch (e.getKeyCode()) {
				case(KeyEvent.VK_UP):
					up = true;
					break;
				case(KeyEvent.VK_DOWN):
					down = true;
					break;
				case(KeyEvent.VK_LEFT):
					left = true;
					break;
				case(KeyEvent.VK_RIGHT):
					right = true;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (!pause) pause = true; else pause = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			pause = true;
			showCursor();
			
			int option = JOptionPane.showOptionDialog(window, "Are you sure you want to quit?", "Where do ya think you're going?", JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE, null, null, null);
			
			if (option == 0) {
				System.exit(0);
			} else {
				hideCursor();
				pause = false;
			}
		}
	}
}
