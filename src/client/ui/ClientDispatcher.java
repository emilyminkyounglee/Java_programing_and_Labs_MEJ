package client.ui;

import javax.swing.SwingUtilities;

import client.state.ClientState;
import protocol.MessageEnvelope;
import protocol.MessageType;
import protocol.dto.*;

import protocol.udp.Stroke;

public class ClientDispatcher {
    private final ViewController vc;
    private final ClientState state;
    private final DrawingPanel drawingPanel;

    public ClientDispatcher(ViewController vc, ClientState state, DrawingPanel drawingPanel) {
        this.vc = vc;
        this.state = state;
        this.drawingPanel = drawingPanel;
    }

    public void onTCP(final MessageEnvelope env) {
        // UI 스레드로 넘겨줌
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MessageType t = env.getType();
                Object body = env.getBody();

                if (t == MessageType.ERROR) {
                    ErrorInfo err = (ErrorInfo) body;
                    vc.showError(err.getCode(), err.getMessage());
                    return;
                }

                if (t.name().equals("LOGIN_SUCCESS")) { // A 계약: LOGIN_SUCCESS가 있을 수 있음
                    // AuthOKDTO 가정: userId, nickname (팀 내 합의된 DTO명 사용)
                    // 여기서는 간단히 JoinInfo를 재활용하거나 서버 계약에 맞춰 교체
                    // 데모용: body가 JoinInfo로 왔다고 가정
                    if (body instanceof JoinInfo) {
                        JoinInfo ji = (JoinInfo) body;
                        state.userId = ji.getUserId();
                        state.nickname = ji.getNickname();
                    }
                    vc.showLobby();
                    return;
                }

                if (t == MessageType.JOINED) {
                    LobbyInfo lobby = (LobbyInfo) body;
                    state.roomId = lobby.getRoomId();
                    vc.updateLobby(lobby.getPlayers(), lobby.getNeededPlayers());
                    return;
                }

                if (t == MessageType.GAME_STARTED) {
                    GameStartInfo gsi = (GameStartInfo) body;
                    vc.showGameStarting(gsi.getPlayers(), gsi.getNeededPlayers());
                    return;
                }

                if (t == MessageType.PHASE_CHANGED) {
                    PhaseUpdate p = (PhaseUpdate) body;
                    state.updatePhase(p.getPhase(), p.getSecondsLeft());
                    vc.onPhaseChanged(p.getPhase(), p.getSecondsLeft());
                    return;
                }

                if (t == MessageType.YOUR_TURN_TO_DRAW) {
                    DrawTurn d = (DrawTurn) body;
                    state.putSentence(d.getChainId(), d.getSentence());
                    vc.showDrawingTurn(d.getChainId(), d.getSentence());
                    return;
                }

                if (t == MessageType.YOUR_TURN_TO_GUESS) {
                    GuessTurn g = (GuessTurn) body;
                    state.putImageRef(g.getChainId(), g.getImageUrl());
                    vc.showGuessTurn(g.getChainId(), g.getImageUrl());
                    return;
                }

                if (t == MessageType.NEW_ROUND_DATA) {
                    NextTaskData n = (NextTaskData) body;
                    vc.showNextTask(n);
                    return;
                }

                if (t == MessageType.VOTING_PHASE_STARTED) {
                    VoteList list = (VoteList) body;
                    vc.showVoting(list);
                    return;
                }

                if (t == MessageType.GAME_OVER) {
                    GameOverSummary over = (GameOverSummary) body;
                    vc.showGameOver(over);
                    return;
                }
            }
        });
    }

    public void onUDP(final Stroke stroke) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (drawingPanel != null) {
                    drawingPanel.applyRemoteStroke(stroke);
                }
            }
        });
    }

    public void onTCPClosed(Exception e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                vc.showDisconnected("TCP 연결이 종료되었습니다.");
            }
        });
    }

    public void onUDPClosed(Exception e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                vc.showDisconnected("UDP 수신이 종료되었습니다.");
            }
        });
    }
}
