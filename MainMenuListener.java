package miSnake;

public class MainMenuListener extends MenuListener {

	public MainMenuListener() {
		super();
	}
	
	@Override
	public void enterPressed(int selectedOption) {
		switch (selectedOption) {
			case (0):
				Game game = Game.getGame();
				game.changeMenu("game");
				game.getBoard().startAnimation();
				break;
			case (1):
				Game.getGame().changeMenu("highScores");
				break;
			case (2):
				Game.getGame().changeMenu("about");
				break;
			case(3):
				System.exit(0);
		}
	}
}