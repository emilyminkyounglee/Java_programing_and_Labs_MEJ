package protocol.udp;

import java.io.Serializable;
import java.util.UUID;

//클라 -> 서버 -> 클라
//내용: x,y,brushSize,color,isDrawing,chainId
//네트워크: UDP로 서버에 보내고, 서버는 해당 체인에 배정된 대상에게만 중계

public class Stroke implements Serializable {
    private static final long serialVersionUID = 5L;

    public double x, y;
    public double brushSize;
    public String color;
    public boolean isDrawing; // true: drag 중, false: up
    public UUID chainId;      // 현재 체인

    public Stroke() {}
    public Stroke(double x, double y, boolean isDrawing, double brushSize, String color, UUID chainId) {
        this.x = x; this.y = y; this.isDrawing = isDrawing; this.brushSize = brushSize; this.color = color; this.chainId = chainId;
    }
}
