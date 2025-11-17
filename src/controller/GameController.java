package controller;

import java.util.*;

import model.GameService;
import player.Player;
import view.GameView;

//이 클래스는 게임의 전반적 흐름을 제어하는 클래스 입니다

public class GameController {
	
	private final GameView view;
	private final GameService service;
	private Player p1;
	private Player p2;
	int role = (int)(Math.random()*1000/100);
	public GameController(GameView view, GameService service, Player p1, Player p2) {
		this.view = view;
		this.service = service;
		this.p1 = p1;
		this.p2 = p2;
	}
	private void log(String msg) {
	    System.out.println("[LOG] " + msg);
	}
	
	// DB호출 임시 List<String> words 
	public void runGame() {
		log("game successfully started!");
		// 1. db에서 단어 목록 가져오기
		List<String> wordList = Arrays.asList( "사과", "바나나", "딸기", "배", "고양이", "강아지");
		log("단어 목록 성공적으로 가져옴!");
		// 2. 게임 시작 화면 표시
		view.showGameStart();
		log("showGameStart has been activated");
		
		// 3. 플레이어 이름 임시 설정, service에 게임 준비 요청
		service.setPlayerInfo(p1, role, "player1");
		log("set player1's info successful");
		service.setPlayerInfo(p2, role+1, "Player2");
		log("set player2's info successful");
		service.setupNewGame(wordList, p1, p2);
		log("set up new game successful");
		
		boolean correct = false;
		// 4. 메인 게임 루프
		while (!service.isGameOver()) {
			
			// 5. 새 라운드 시작
			service.newRound();
			log("start new round");
			
			String answerWord = wordList.get((int)(Math.random()*wordList.size()));
			service.setAnswer(answerWord);
			log("set answer for this round: " +  answerWord);
			
			// 6. view에 라운드 정보 표시
			view.showRoundStart(service.getCurrentRound(),service.getDrawerName(p1, p2), service.getGuesserName(p1, p2));
			log("show information of the round");
			
			// 7. drawer에게 단어 보여주기
			view.showWordToDrawer(service.getWordForDrawer());
			log("show the drawer the word");
			correct = false;
			
			// 8. 라운드 루프 (최대 5회)
			while (!service.isRoundOver(correct)) {
				log("round loop start (max 5 times)");
				
				// 9. 그림 그리기
				view.getDrawingInput();
				log("got drawing input");
				
				// 10. view에서 정답 입력받기
				String answer = view.getAnswerInput();
				log("got answer from guesser");
				
				// 11. service에 정답 확인 요청
				correct = service.checkAnswer(answer);
				log("checking the answer");
				
				// 12. view에 결과 표시
				view.showAnswerResult(correct, service.getTriesLeft());
				log("showing the results");
				
			}
			
			// 13. 라운드 종료
			boolean roundSuccess = service.getCurrentRoundScore() > 0;
			view.showRoundEnd(roundSuccess, service.getCurrentRoundScore(), service.getWordForDrawer());
			log("round is over");
			service.changeRoles(p1, p2);
			log("roles changed");
			
						
		}
		
		// 14. 최종 점수 표시
		view.showFinalScore(service.getScore());
		log("is showing the final score");
		
		
	}
}
