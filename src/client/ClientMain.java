package client;

import javax.swing.JOptionPane;

import client.net.Network;
import client.state.ClientState;
import client.ui.ViewController;

public class ClientMain {
    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog(null, "서버 IP", "127.0.0.1");
        if (host == null || host.trim().length() == 0) host = "127.0.0.1";

        int tcpPort = 5000;
        int udpPort = 5001;

        ClientState state = new ClientState();
        Network net = new Network();
        ViewController vc = new ViewController(state, net);

        try {
            vc.initUI(); //화면 구성
            vc.startNetwork(host, tcpPort, udpPort); //리스너 스레드 시작
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "네트워크 시작 실패: " + e.getMessage());
            System.exit(1);
        }
    }
}

//(1) 기본적으로는 서버 ip 내 컴퓨터로 설정해둠 //포트번호 tcp랑 udp 다르게