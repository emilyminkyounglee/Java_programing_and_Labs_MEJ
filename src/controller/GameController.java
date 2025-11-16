package controller;

import java.util.*;

//이 클래스는 게임의 전반적 흐름을 제어하는 클래스 입니다

public class GameController {
	//DB호출 임시 
	void runGame()
	{
		
		/*
		 *(임시) DB에서 단어 목록을 가져옵니다.
view.showGameStart() 호출
service.setupNewGame(...) 호출
while (!service.isGameOver()) : 메인 게임 루프 (6라운드)
(루프 내부) service.startNewRound() 호출
(루프 내부) view.showRoundStart(...) 등 service의 getter를 이용해 View에 정보 표시
(루프 내부) while (!service.isRoundOver()): 라운드 루프 (5회 시도)
(라운드 루프 내부) view.getDrawingInput() 호출
(라운드 루프 내부) String guess = view.getGuessInput() 호출
(라운드 루프 내부) boolean correct = service.checkAnswer(guess) 호출
(라운드 루프 내부) view.showGuessResult(correct, service.getTriesLeft()) 호출
(라운드 루프 종료 후) view.showRoundEnd(...) 호출
(메인 루프 종료 후) 
		
		 */
	}
}
