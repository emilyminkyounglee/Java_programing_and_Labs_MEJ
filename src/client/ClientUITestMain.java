package client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import client.net.Network;
import client.state.ClientState;
import client.ui.ViewController;
import protocol.dto.GameOverSummary;
import protocol.dto.NextTaskData;
import protocol.dto.VoteList;

//UI 확인용

public class ClientUITestMain {

    public static void main(String[] args) {
        // 원래 ClientMain이랑 비슷하게 상태 + 네트워크 + VC 생성
        ClientState state = new ClientState();
        Network net = new Network();   // 실제 서버 연결은 안 할 거라 connect 안 부름
        final ViewController vc = new ViewController(state, net);

        // UI만 먼저 띄우기 (로그인 화면부터 보임)
        vc.initUI();

        // 서버 없이 UI 흐름만 순서대로 보여주는 테스트 스레드
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    // 1. 로그인 화면은 이미 떠 있음 -> 1초 후 로비로
                    Thread.sleep(1000);
                    vc.showLobby();

                    // 2. 2초 후 문장 입력 화면
                    Thread.sleep(2000);
                    vc.showInitSentence();

                    // 3. 2초 후 "다음 작업: 그림" (NEW_ROUND_DATA 역할)
                    Thread.sleep(2000);
                    UUID chainForDraw = UUID.randomUUID();
                    NextTaskData nextDraw = new NextTaskData(
                        chainForDraw,
                        NextTaskData.Kind.DRAWING,
                        "강아지가 산책하는 장면을 그려주세요"
                    );
                    vc.showNextTask(nextDraw);

                    // 4. 2초 후 "추리 차례!" (YOUR_TURN_TO_GUESS 역할)
                    Thread.sleep(2000);
                    UUID chainForGuess = UUID.randomUUID();
                    vc.showGuessTurn(chainForGuess, "dummy_image_id_or_url");

                    // 5. 2초 후 "투표 시작!" (VOTING_PHASE_STARTED 역할)
                    Thread.sleep(2000);
                    VoteList emptyVoteList = new VoteList(new ArrayList<VoteList.Item>());
                    vc.showVoting(emptyVoteList);

                    // 6. 2초 후 "게임 종료!" (GAME_OVER 역할)
                    Thread.sleep(2000);
                    List<GameOverSummary.Result> results =
                        new ArrayList<GameOverSummary.Result>();
                    GameOverSummary summary = new GameOverSummary(results, 30);
                    vc.showGameOver(summary);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }
}

