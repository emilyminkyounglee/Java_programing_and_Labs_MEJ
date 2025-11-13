package client.net;

import java.io.ObjectInputStream;
import client.ui.ClientDispatcher;
import protocol.MessageEnvelope;

//역할 : MessageEnvelope을 수신

public class TCPListenerThread implements Runnable {
    private final ObjectInputStream in;
    private final ClientDispatcher dispatcher;

    public TCPListenerThread(ObjectInputStream in, ClientDispatcher dispatcher) {
        this.in = in;
        this.dispatcher = dispatcher;
    }

    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof MessageEnvelope) {
                    MessageEnvelope env = (MessageEnvelope) obj;
                    dispatcher.onTCP(env);
                }
            }
        } catch (Exception e) {
            dispatcher.onTCPClosed(e);
        }
    }
}

