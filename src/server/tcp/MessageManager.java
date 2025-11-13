package server.tcp;

import java.util.UUID;

import protocol.ErrorType;
import protocol.MessageEnvelope;
import protocol.MessageType;
import protocol.dto.ErrorInfo;
import protocol.dto.GameStartInfo;
import protocol.dto.JoinInfo;
import protocol.dto.LobbyInfo;
import server.Config;
import server.auth.ClientRegistry;
import server.room.GameRoom;
import server.room.RoomRegistry;

// TCP 메세지 총괄 담당 매니저 (이제 문자열 대신 MessageEnvelope 기준)
public class MessageManager {

    // 클라이언트 명부는 딱 하나만
    private static ClientRegistry registry = new ClientRegistry();

    // UDPManager 등에서 Registry에 접근할 수 있도록 getter 유지
    public static ClientRegistry getRegistry() {
        return registry;
    }

    public static void manageMessage(MessageEnvelope env, ClientManager client) {
        if (env == null || env.getType() == null) return;

        MessageType type = env.getType();
        System.out.println("[TCP 수신] type=" + type + " from=" + client.getUserId());

        try {
            switch (type) {
                case JOIN:
                    handleJoin(env, client);
                    break;

                case SUBMIT_SENTENCE:
                    // TODO: 게임 로직 연결
                    System.out.println("SUBMIT_SENTENCE 수신 (아직 서버 로직 미구현)");
                    break;

                case SUBMIT_GUESS:
                    // TODO
                    System.out.println("SUBMIT_GUESS 수신 (아직 서버 로직 미구현)");
                    break;

                case SUBMIT_VOTE:
                    // TODO
                    System.out.println("SUBMIT_VOTE 수신 (아직 서버 로직 미구현)");
                    break;

                default:
                    System.out.println("아직 서버에서 처리 안 하는 타입: " + type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(client, ErrorType.BAD_REQUEST, e.getMessage());
        }
    }

    // ===== JOIN 처리 =====
    private static void handleJoin(MessageEnvelope env, ClientManager client) {
        Object body = env.getBody();
        if (!(body instanceof JoinInfo)) {
            sendError(client, ErrorType.BAD_REQUEST, "JOIN body 형식이 잘못됨");
            return;
        }

        JoinInfo join = (JoinInfo) body;
        String nickname = join.getNickname();
        if (nickname == null || nickname.isEmpty()) {
            sendError(client, ErrorType.BAD_REQUEST, "닉네임을 입력해 주세요.");
            return;
        }

        // 유저 ID 부여
        UUID userId = UUID.randomUUID();
        join.setUserId(userId);

        client.setUserId(userId.toString());
        client.setNickname(nickname);

        // 세션 등록
        registry.addSession(client);

        // 임시로 "default" 방 하나에만 모두 입장
        String roomId = "default";
        client.setRoomId(roomId);
        RoomRegistry.joinRoom(roomId, client);
        GameRoom room = RoomRegistry.getRoom(roomId);

        if (room != null) {
            // 로비 정보 DTO 생성
            LobbyInfo lobby = new LobbyInfo(
                    UUID.nameUUIDFromBytes(roomId.getBytes()),
                    room.getPlayers().size(),
                    Config.REQUIRED_PLAYERS
            );

            MessageEnvelope joined = new MessageEnvelope(MessageType.JOINED, lobby);

            // 일단 본인에게는 확실히 보내기
            client.send(joined);

            // 방 전체에게 로비 정보 방송 (옵션)
            room.broadcast(joined);

            // 인원이 찼다면 GAME_STARTED 방송 (기본 골격만)
            if (room.getPlayers().size() >= Config.REQUIRED_PLAYERS) {
                GameStartInfo info = new GameStartInfo(
                        room.getPlayers().size(),
                        Config.REQUIRED_PLAYERS
                );
                MessageEnvelope started =
                        new MessageEnvelope(MessageType.GAME_STARTED, info);
                room.broadcast(started);
            }
        }
    }

    private static void sendError(ClientManager client, ErrorType type, String message) {
        ErrorInfo err = new ErrorInfo(type.name(), message);
        MessageEnvelope env = new MessageEnvelope(MessageType.ERROR, err);
        client.send(env);
    }

    // ===== 연결 종료 보고 =====
    public static void disconnected(ClientManager client) {
        System.out.println("[클라이언트 종료 보고] user=" + client.getUserId());
        registry.removeSession(client.getUserId());

        String roomId = client.getRoomId();
        if (roomId != null) {
            GameRoom room = RoomRegistry.getRoom(roomId);
            if (room != null) {
                room.removePlayer(client);
            }
        }
    }
}
