package client.ui;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import client.net.Network;
import protocol.udp.Stroke;

//그림판

public class DrawingPanel extends JPanel {
    private final Network network;
    private UUID currentChainId;

    // 렌더 버퍼
    private final List<Stroke> strokes;

    public DrawingPanel(Network network) {
        this.network = network;
        this.strokes = new ArrayList<Stroke>();
        setPreferredSize(new Dimension(640, 480));
        setBackground(Color.WHITE);

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Stroke s = new Stroke(e.getX(), e.getY(), true, 3.0, "#000000", currentChainId);
                try {
                    network.sendUDP(s);
                } catch (IOException ex) { /* 로그 */ }
                applyLocalStroke(s);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                Stroke s = new Stroke(e.getX(), e.getY(), false, 3.0, "#000000", currentChainId);
                try {
                    network.sendUDP(s);
                } catch (IOException ex) { /* 로그 */ }
                applyLocalStroke(s);
            }
        });
    }

    public synchronized void setChain(UUID chainId) {
        this.currentChainId = chainId;
    }

    public synchronized void clearCanvas() {
        this.strokes.clear();
        repaint();
    }

    public void applyLocalStroke(Stroke s) {
        synchronized (this) {
            this.strokes.add(s);
        }
        repaint();
    }

    public void applyRemoteStroke(Stroke s) {
        synchronized (this) {
            this.strokes.add(s);
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<Stroke> copy;
        synchronized (this) {
            copy = new ArrayList<Stroke>(this.strokes);
        }
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            int i;
            for (i = 0; i < copy.size(); i++) {
                Stroke s = copy.get(i);
                float width = (float) s.brushSize;
                g2.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                Color c = parseColor(s.color);
                g2.setColor(c);

                // 간단히 점/선으로 표현
                g2.drawLine((int) s.x, (int) s.y, (int) s.x, (int) s.y);
            }
        } finally {
            g2.dispose();
        }
    }

    private Color parseColor(String hex) {
        try {
            return Color.decode(hex);
        } catch (Exception e) {
            return Color.BLACK;
        }
    }
}

