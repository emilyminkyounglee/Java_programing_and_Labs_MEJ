package view;

import java.util.Scanner;

// 콘솔에서 출력/입력을 담당
public class GameView {

    private final Scanner scanner = new Scanner(System.in);

    //2. 게임시작 화면 표시
    public void showGameStart() {
        System.out.println("==================================");
        System.out.println("        캐치마인드 게임 시작 ");
        System.out.println("==================================");
        System.out.println();
    }

   //6. 라운드 정보 표시
    public void showRoundStart(int round, String drawerName, String guesserName) {
        System.out.println("---------- ROUND " + round + " ----------");
        System.out.println("그림 그리는 사람 : " + drawerName);
        System.out.println("맞추는 사람     : " + guesserName);
        System.out.println();
    }

    // 7.drawer에게 단어 보여주기
    public void showWordToDrawer(String word) {
        System.out.println("[제시어 안내] (DRAWER에게만 보이는 영역)");
        System.out.println("제시어: " + word);
        System.out.println();
    }

    //9. 그림 그리기
    public void getDrawingInput() {
        System.out.println("[그림 그리기 단계]");
    }

    //10. 정답 입력받기
    public String getAnswerInput() {
        System.out.print("정답을 입력하세요: ");
        String input = scanner.nextLine();
        System.out.println();
        return input;
    }

    // 12. 결과표시
    public void showAnswerResult(boolean correct, int triesLeft) {
        if (correct) {
            System.out.println("정답입니다!");
        } else {
            System.out.println("오답입니다...");
            System.out.println("남은 기회: " + triesLeft + "번");
        }
        System.out.println();
    }

    //13. 라운드 종료
    public void showRoundEnd(boolean roundSuccess, int roundScore, String answerWord) {
        System.out.println("===== 라운드 종료 =====");
        if (roundSuccess) {
            System.out.println("라운드 성공! ");
        } else {
            System.out.println("라운드 실패... ");
        }
        System.out.println("이번 라운드 정답: " + answerWord);
        System.out.println("이번 라운드 점수: " + roundScore);
        System.out.println("=======================");
        System.out.println();
    }

    //14. 최종 점수 표시
    public void showFinalScore(int totalScore) {
        System.out.println("##################################");
        System.out.println("          게임 종료 ");
        System.out.println("          최종 점수: " + totalScore);
        System.out.println("##################################");
    }
}