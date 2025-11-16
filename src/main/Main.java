package main;

import controller.GameController;
import model.GameService;
import player.Player;
import player.Role;
import view.GameView;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameService service = new GameService();
		GameController controller = new GameController();
		GameView view = new GameView();
		
		Player p1 = new Player();
		Player p2 = new Player();
		int role = (int)((int)Math.random()*1000/100);
		service.setPlayerInfo(p1, role);
		service.setPlayerInfo(p2, role + 1);
		
		
	}

}
