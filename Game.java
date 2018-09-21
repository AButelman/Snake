package miSnake;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Game {

	private static Game game = new Game();
	
	public static Game getGame() { return game; }
	
	private GraphicsEnvironment env;
	private GraphicsDevice vc;
	private Frame window;
	private CardLayout layout;
	private ArrayList<JPanel> menus = new ArrayList<JPanel>();
	private Board board;
	
	private Game() {
		createWindow();
		createMenus();
		removeCursor(window);
	}
	
	public void reset() {
		env = null;
		vc = null;
		window = null;
		layout = null;
		menus = new ArrayList<JPanel>();
		
		createWindow();
		createMenus();
		removeCursor(window);
	}
	
	public Board getBoard() { return board; }
	
	public GraphicsDevice getGraphicsDevice() { return vc; }
	
	public void start() {
		
		layout.show(window, "main");
		Menu jMenu = (Menu) menus.get(0);
		jMenu.setShowed(true);
		vc.setFullScreenWindow(window);
	}
	
	public void changeMenu(String menuToShow) {
		
		// Para que siempre empiece seleccionada la primera opción
		for (JPanel panel : menus) {
			if (!(panel instanceof Board)) {
				Menu menu = (Menu) panel;
				menu.setSelected(0);
			}
		}
		layout.show(window, menuToShow);
		
		System.out.println("Cambiando al menú: " + menuToShow);
		
		/* Como al cambiar la carta del layout el panel pierde foco,
		 * busco entre los menús el que se pasa para cambiar y le doy foco.
		 */
		
		
		for (JPanel menu : menus) {
			
			if (menu instanceof Menu) {
				Menu jMenu = (Menu) menu;
				if (jMenu.getName().equals(menuToShow)) {
					jMenu.setShowed(true);
					jMenu.requestFocus();
				} else {
					jMenu.setShowed(false);
				}
			} else {
				
				menu.requestFocus();
			}
		}
	}
	
	private void createWindow() {
		System.setProperty("sun.java2d.opengl", "true");
	    env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    vc = env.getDefaultScreenDevice();
	    
		window = new Frame();
		// window.setDefaultCloseOperation(JJFrame.EXIT_ON_CLOSE);
		window.setUndecorated(true);
		window.setResizable(false);
		
		layout = new CardLayout();
		
		window.setLayout(layout);
	}

	private void createMenus() {
		
		String[] mainMenuOptions = {"Start Game", "High Scores", "About", "Exit"};
		Menu mainMenu = new Menu("main", null, mainMenuOptions, Color.BLACK, new Color(30, 255, 30), Color.GRAY);
		mainMenu.addMenuListener(new MainMenuListener());
		
		String aboutMenuOptions[] = {"Back"};
		Menu aboutMenu = new Menu("about", "This is quite a long thing to put here, and I think it's being shown quite well", aboutMenuOptions, Color.BLACK, new Color(30, 255, 30), Color.GRAY);
		aboutMenu.addMenuListener(new MenuListener() {

			@Override
			public void enterPressed(int selectedOption) {
				
				changeMenu("main");
			}
		});
		
		String highScoresMenuOptions[] = {"To" , "Back"};
		String highScores = "Ivan = 41.000\nCony = 13.000\nAndres = 12.000";
		Menu highScoresMenu = new Menu("highScores", highScores, highScoresMenuOptions, Color.BLACK, new Color(30, 255, 30), Color.GRAY);
		highScoresMenu.addMenuListener(new MenuListener() {

			@Override
			public void enterPressed(int selectedOption) {
				
				if (selectedOption == 1) changeMenu("main");
			}
		});
		
		String message = "Game Over!\n\nYou wanna give it another try, \ndon't ya?";
		String[] options = {"Sure!", "Hell no"};
		Menu gameOverMenu = new Menu("gameOver", message, options, Color.BLACK, new Color(30, 255, 30), Color.GRAY);
		gameOverMenu.addMenuListener(new MenuListener() {
			
			@Override
			public void enterPressed(int selectedOption) {
				
				switch (selectedOption) {
						
					case(0):
						board.reset();
						board.startAnimation();
						changeMenu("game");
						break;
					case(1):
						changeMenu("main");
				}
			}
		});
		
		addMenu(mainMenu, "main");
		addMenu(aboutMenu, "about");
		addMenu(highScoresMenu, "highScores");
		addMenu(gameOverMenu, "gameOver");
		board = new Board();
		addMenu(board, "game");
	}
	
	public void addMenu(JPanel menu, String name) {
		window.add(menu, name);
		menus.add(menu);
	}

	public void removeCursor(Frame window) {
		// Remove the mouse cursor from the window
		BufferedImage blankImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankImg, new Point(0, 0), "");
		window.setCursor(blankCursor);
	}
	
	public Frame getWindow() { return window; }
}
