package view;
//입력 담당 클래스 
public interface GameView {
	String input = "";
	
	String getGuessInput()
	{
		//스캐너를 활용해 콘솔에서 문자열을 받아 리턴한다
	}
	void getDrawingInput()
	{
		//단순히 그림 그리는 중.. 같은 메세지만 출력. 
		//나중에 스프링과 프론트를 연결할때 이 곳은 그림을 받는 함수가 구현 될것임 
	}
	
}
