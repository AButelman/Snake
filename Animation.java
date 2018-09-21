package miSnake;

public class Animation implements Runnable {

	public static final double ACCELERATION = 0.06;
	
	private boolean running = true;
	private Board board;
	private double speed = 0.8;
	private int delay;
	
	public Animation(Board board) {
		this.board = board;
		calculateDelay();
	}
	
	public void stop() { running = false; }

	private void calculateDelay() {
		delay = (int) (100 / speed);
		System.out.println("Delay: " + delay + " mS");
	}
	
	public void accelerate() {
		speed += ACCELERATION;
		calculateDelay();
	}
	
	@Override
	public void run() {
		
		ScreenRefresh sr = new ScreenRefresh();
		Thread t = new Thread(sr);
		t.start();
		
		while (running) {
			board.moveSnake();
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class ScreenRefresh implements Runnable {

		@Override
		public void run() {
			while (true) {
				board.repaint();
				
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
		}
		
	}
}
