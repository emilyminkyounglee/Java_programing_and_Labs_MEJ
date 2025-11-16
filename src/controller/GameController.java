package controller;
//이 클래스는 게임의 전반적 흐름을 제어하는 클래스 입니다

public class GameController {
	
	private final GameView view;
	private final GameService service;
	
	public GameController(GameView view, GameService service) {
		this.view = view;
		this.service = service;
	}
	
	// DB호출 임시 List<String> words 
	public void runGame() {
		
		// 1. db에서 단어 목록 가져오기
		List<String> wordList = Arrays.asList("단어 목록...");
		
		// 2. 게임 시작 화면 표시
		view.showGameStart();
		
		// 3. 플레이어 이름 임시 설정, service에 게임 준비 요청
		String player1 = "플레이어1";
		String player2 = "플레이어2";
		service.setupNewGame(wordList, player1, player2);
		
		// 4. 메인 게임 루프
		while (!service.isGameOver()) {
			
			// 5. 새 라운드 시작
			service.startNewRound();
			
			// 6. view에 라운드 정보 표시
			view.showRoundStart(service.getCurrentRound(), service.getDrawerName(), service.getGuesserName);
			
			// 7. drawer에게 단어 보여주기
			view.showWordToDrawer(service.getWordForDrawer());
			
			// 8. 라운드 루프 (최대 5회)
			while (!service.isRoundOver()) {
				
				// 9. 그림 그리기
				view.getDrawingInput();
				
				// 10. view에서 정답 입력받기
				String answer = view.getAnswerInput();
				
				// 11. service에 정답 확인 요청
				boolean correct = service.checkAnswer(answer);
				
				// 12. view에 결과 표시
				view.showAnswerResult(correct, service.getTriesLeft());
				
			}
			
			// 13. 라운드 종료
			boolean roundSuccess = service.getCurrentRoundScore() > 0;
			view.showRoundEnd(roundSuccess, service.getCurrentRoundScore(), sevice.getWordForDrawer());
						
		}
		
		// 14. 최종 점수 표시
		view.showFinalScore(service.getTotalScore());
		
		
	}
}
