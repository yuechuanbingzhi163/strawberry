package org.xlet.strawberry.core.message.client;

import org.xlet.strawberry.core.message.MessageType;
import org.xlet.strawberry.core.message.Message;

/**
 * member status message.
 */
public class MemberStatusMessage extends Message implements ServerToClientMessage, ClientToServerMessage {

    private String memberId;
    private int status;

    /**
     * constructor.
     */
    public MemberStatusMessage() {
        super(MessageType.Status);
    }

    public MemberStatusMessage(String memberId, int status) {
        this();
        this.memberId = memberId;
        this.status = status;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
