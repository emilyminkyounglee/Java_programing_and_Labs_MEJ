package protocol.dto;

import java.io.Serializable;
import java.util.UUID;

//메시지 : SUBMIT_VOTE   (클라 -> 서버)
//내용: chainId, agree (O/X)

//서버: VoteSession.submitVote(...)

public class VoteRequest implements Serializable {
    private static final long serialVersionUID = 12L;

    private UUID chainId;
    private boolean agree; // true=O, false=X

    public VoteRequest() {}
    public VoteRequest(UUID chainId, boolean agree) { this.chainId = chainId; this.agree = agree; }
    public UUID getChainId() { return chainId; }
    public boolean isAgree() { return agree; }
    public void setChainId(UUID chainId) { this.chainId = chainId; }
    public void setAgree(boolean agree) { this.agree = agree; }
}