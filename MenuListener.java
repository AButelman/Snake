package miSnake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public abstract class MenuListener implements KeyListener {

	protected Menu menu;
	
	public MenuListener() {	}
	
	public void setMenu(Menu menu) { this.menu = menu; }
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_UP) {
		
			menu.setSelected(menu.getSelected() - 1);
			menu.repaint();
	
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
		
			menu.setSelected(menu.getSelected() + 1);
			menu.repaint();
			
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			
			enterPressed(menu.getSelected());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	public abstract void enterPressed(int selectedOption);
}
