package view;
//입력 담당 클래스 

import java.util.Scanner;

public interface GameView {
	
	private final Scanner scanner = new Scanner(System.in);
	
	//String input = ""; //일단은 입력값을 저장하지는 않고 getGuessInput() 메서드의 지역 변수로 처리함
	
	
	//getDrawingInput역할 : 그림 입력받는 중을 표현하는 메서드
	// ~> 지금은 syso로 구현
	void getDrawingInput()
	{
		//단순히 그림 그리는 중.. 같은 메세지만 출력. 
		//나중에 스프링과 프론트를 연결할때 이 곳은 그림을 받는 함수가 구현 될것임
		System.out.println("[그림 그리기 단계]");
//		System.out.println("제시어: ");     // 나중에 추가하기
		System.out.println("그림 그리는 중");
        System.out.println();
	}
	
	
	//getGuessInput() : 정답을 입력받는 메서드
	String getGuessInput()
	{
		//스캐너를 활용해 콘솔에서 문자열을 받아 리턴한다
		System.out.print("정답을 입력하세요: ");
        String input = scanner.nextLine();  
		
        return input;
	}
	
}
