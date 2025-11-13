package client.ui;

import client.net.Network;
import client.net.TCPListenerThread;
import client.net.UDPListenerThread;
import client.state.ClientState;

import protocol.MessageEnvelope;
import protocol.MessageType;
import protocol.dto.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.UUID;

public class ViewController {
    private final ClientState state;
    private final Network net;

    private JFrame frame;
    private CardLayout cards;
    private JPanel root;

    // 화면
    private JPanel loginView;
    private JPanel lobbyView;
    private JPanel initSentenceView;
    private DrawingPanel drawingPanel;
    private JPanel guessView;
    private JPanel voteView;
    private JPanel resultView;

    // 간단 위젯
    private JTextField tfNickname;
    private JPasswordField tfPassword;
    private JTextArea taSentence;
    private JTextField tfGuess;

    private JLabel lblLobbyInfo;
    private JLabel lblPhase;

    private ClientDispatcher dispatcher;

    public ViewController(ClientState state, Network net) {
        this.state = state;
        this.net = net;
    }

    public void initUI() {
        frame = new JFrame("CatMind Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cards = new CardLayout();
        root = new JPanel(cards);

        buildLoginView();
        buildLobbyView();
        buildInitSentenceView();
        buildDrawingView();
        buildGuessView();
        buildVoteView();
        buildResultView();

        frame.setContentPane(root);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        showLogin();
    }

    public void startNetwork(String host, int tcpPort, int udpPort) throws Exception {
        net.connectTCP(host, tcpPort);
        net.connectUDP(host, udpPort);

        dispatcher = new ClientDispatcher(this, state, drawingPanel);

        TCPListenerThread t = new TCPListenerThread(net.tcpIn(), dispatcher);
        UDPListenerThread u = new UDPListenerThread(net.udpSocket(), dispatcher);
        net.startListeners(t, u);
    }

    // ====== 화면 구축 ======
    private void buildLoginView() {
        loginView = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0,2,6,6));

        tfNickname = new JTextField();
        tfPassword = new JPasswordField();

        form.add(new JLabel("Nickname:"));
        form.add(tfNickname);
        form.add(new JLabel("Password:"));
        form.add(tfPassword);

        JButton btnLogin = new JButton("로그인/참가");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String nick = tfNickname.getText();
                    String pw = new String(tfPassword.getPassword());
                    // 계약 A가 LOGIN_REQUEST를 쓰는 경우:
                    // var body = new AuthDTO(nick, pw);  // 팀 내 DTO 명칭에 맞추세요
                    // MessageEnvelope env = new MessageEnvelope(MessageType.valueOf("LOGIN_REQUEST"), body);

                    // 여기서는 A의 JoinInfo + JOIN 사용 예시
                    JoinInfo req = new JoinInfo(nick, pw);
                    MessageEnvelope env = new MessageEnvelope(MessageType.JOIN, req);
                    net.sendTCP(env);
                } catch (IOException ex) {
                    showError("NETWORK", ex.getMessage());
                }
            }
        });

        loginView.add(form, BorderLayout.CENTER);
        loginView.add(btnLogin, BorderLayout.SOUTH);
        root.add(loginView, "login");
    }

    private void buildLobbyView() {
        lobbyView = new JPanel(new BorderLayout());
        lblLobbyInfo = new JLabel("로비: 인원 대기중");
        lobbyView.add(lblLobbyInfo, BorderLayout.NORTH);
        root.add(lobbyView, "lobby");
    }

    private void buildInitSentenceView() {
        initSentenceView = new JPanel(new BorderLayout());
        lblPhase = new JLabel("INIT_SENTENCE");
        taSentence = new JTextArea(6, 40);

        JButton btnSubmit = new JButton("문장 제출");
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = taSentence.getText();
                    SentenceRequest req = new SentenceRequest(text);
                    MessageEnvelope env = new MessageEnvelope(MessageType.SUBMIT_SENTENCE, req);
                    net.sendTCP(env);
                    // 버튼 잠금 등은 실제 서버 응답/페이즈 전환에서 처리
                } catch (IOException ex) {
                    showError("NETWORK", ex.getMessage());
                }
            }
        });

        initSentenceView.add(lblPhase, BorderLayout.NORTH);
        initSentenceView.add(new JScrollPane(taSentence), BorderLayout.CENTER);
        initSentenceView.add(btnSubmit, BorderLayout.SOUTH);
        root.add(initSentenceView, "initSentence");
    }

    private void buildDrawingView() {
        drawingPanel = new DrawingPanel(net);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.add(drawingPanel, BorderLayout.CENTER);
        root.add(wrap, "draw");
    }

    private void buildGuessView() {
        guessView = new JPanel(new BorderLayout());
        tfGuess = new JTextField();
        JButton btnSubmitGuess = new JButton("추리 제출");
        btnSubmitGuess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // 실제 chainId는 showGuessTurn에서 세팅/보관 후 사용
                    String text = tfGuess.getText();
                    // 데모: 마지막으로 렌더된 체인 ID를 DrawingPanel에서 가져오지 않고
                    // VC가 보관하도록 개선해도 됨
                    // 여기서는 간단화를 위해 임시로 사용(실전에서는 별도 필드 저장 권장)
                    // 예시: vc.currentGuessChainId
                    // 이 스켈레톤에선 입력 직전 서버에서 받은 chainId를 캐시해두고 사용하도록
                    // showGuessTurn()에서 필드에 저장
                    // 아래는 안전하게 null 체크하도록 설계 권장
                    UUID chainId = currentGuessChainId;
                    GuessRequest req = new GuessRequest(chainId, text);
                    MessageEnvelope env = new MessageEnvelope(MessageType.SUBMIT_GUESS, req);
                    net.sendTCP(env);
                } catch (IOException ex) {
                    showError("NETWORK", ex.getMessage());
                }
            }
        });
        guessView.add(new JLabel("추리 입력:"), BorderLayout.NORTH);
        guessView.add(tfGuess, BorderLayout.CENTER);
        guessView.add(btnSubmitGuess, BorderLayout.SOUTH);
        root.add(guessView, "guess");
    }

    private void buildVoteView() {
        voteView = new JPanel(new BorderLayout());
        voteView.add(new JLabel("투표 화면 (서버에서 받은 리스트를 렌더)"), BorderLayout.NORTH);
        // 실제 구현: VoteList 렌더 → 항목별 O/X 버튼 → SUBMIT_VOTE
        root.add(voteView, "vote");
    }

    private void buildResultView() {
        resultView = new JPanel(new BorderLayout());
        resultView.add(new JLabel("게임 결과"), BorderLayout.NORTH);
        root.add(resultView, "result");
    }

    // ====== 화면 전환 헬퍼 ======
    public void showLogin() { cards.show(root, "login"); }
    public void showLobby() { cards.show(root, "lobby"); }
    public void showInitSentence() { cards.show(root, "initSentence"); }
    public void showDraw() { cards.show(root, "draw"); }
    public void showGuess() { cards.show(root, "guess"); }
    public void showVote() { cards.show(root, "vote"); }
    public void showResult() { cards.show(root, "result"); }

    // ====== 서버 이벤트에 따른 뷰 제어 ======
    public void updateLobby(int players, int needed) {
        lblLobbyInfo.setText("로비: 현재 " + players + " / 시작 필요 " + needed);
        showLobby();
    }

    public void showGameStarting(int players, int needed) {
        JOptionPane.showMessageDialog(frame, "게임 시작! (" + players + "/" + needed + ")");
    }

    public void onPhaseChanged(String phase, int secondsLeft) {
        if ("INIT_SENTENCE".equals(phase)) {
            lblPhase.setText("문장 입력 (" + secondsLeft + "s)");
            showInitSentence();
        } else if ("DRAW_PHASE".equals(phase)) {
            showDraw();
        } else if ("GUESS_PHASE".equals(phase)) {
            showGuess();
        } else if ("VOTE_PHASE".equals(phase)) {
            showVote();
        } else if ("END".equals(phase)) {
            showResult();
        }
    }

    public void showDrawingTurn(UUID chainId, String sentence) {
        drawingPanel.setChain(chainId);
        JOptionPane.showMessageDialog(frame, "당신의 그림 차례!\n문장: " + sentence);
        showDraw();
    }

    private UUID currentGuessChainId;

    public void showGuessTurn(UUID chainId, String imageUrl) {
        currentGuessChainId = chainId;
        // imageUrl 렌더는 실제 구현에서 이미지 패널/컴포넌트 사용
        JOptionPane.showMessageDialog(frame, "추리 차례! 이미지 참조: " + imageUrl);
        showGuess();
    }

    public void showNextTask(NextTaskData data) {
        if (data.getKind() == NextTaskData.Kind.DRAWING) {
            drawingPanel.setChain(data.getChainId());
            JOptionPane.showMessageDialog(frame, "다음 작업: 그림 (문장/값: " + data.getValue() + ")");
            showDraw();
        } else {
            currentGuessChainId = data.getChainId();
            JOptionPane.showMessageDialog(frame, "다음 작업: 문장 (" + data.getValue() + ")");
            showGuess();
        }
    }

    public void showVoting(VoteList list) {
        // 간단 안내(실제 UI 테이블/리스트 렌더 권장)
        JOptionPane.showMessageDialog(frame, "투표 시작! 항목 수: " + (list.getItems() == null ? 0 : list.getItems().size()));
        showVote();
    }

    public void showGameOver(GameOverSummary over) {
        JOptionPane.showMessageDialog(frame, "게임 종료! 당신의 총점: " + over.getYourTotalScore());
        showResult();
    }

    // ====== 송신 헬퍼 ======
    public void submitVote(UUID chainId, boolean agree) {
        try {
            VoteRequest req = new VoteRequest(chainId, agree);
            MessageEnvelope env = new MessageEnvelope(MessageType.SUBMIT_VOTE, req);
            net.sendTCP(env);
        } catch (IOException ex) {
            showError("NETWORK", ex.getMessage());
        }
    }

    public void showError(String code, String message) {
        JOptionPane.showMessageDialog(frame, "[" + code + "] " + message);
    }

    public void showDisconnected(String message) {
        JOptionPane.showMessageDialog(frame, message);
        showLogin();
    }
}
