package org.xlet.strawberry.core.message.server;

import org.xlet.strawberry.core.allocate.ConnectToken;
import org.xlet.strawberry.core.message.MessageType;
import org.xlet.strawberry.core.server.ServerBasic;
import org.xlet.strawberry.core.message.Message;
import org.xlet.strawberry.core.message.MustRespondMessage;

import java.util.UUID;

/**
 * Creator: JackieHan.
 * DateTime: 14-1-16 下午4:17.
 * Summary: token消息. 用于通知消息服务token信息.
 */
public class TokenMessage extends Message implements MustRespondMessage, ServerToServerMessage {

    private ConnectToken token;

    private ServerBasic fromServer;

    private String respondKey;

    /**
     * 构造函数.
     */
    public TokenMessage() {
        super(MessageType.Token);
    }

    /**
     * 构造函数.
     *
     * @param token token信息.
     */
    public TokenMessage(ConnectToken token, ServerBasic fromServer) {
        this();
        this.token = token;
        this.fromServer = fromServer;
        this.respondKey = UUID.randomUUID().toString();
    }

    /**
     * 获取token.
     *
     * @return token.
     */
    public ConnectToken getToken() {
        return token;
    }

    /**
     * 设置token.
     *
     * @param token token.
     */
    public void setToken(ConnectToken token) {
        this.token = token;
    }

    @Override
    public ServerBasic getFromServer() {
        return this.fromServer;
    }

    /**
     * set from server basic.
     *
     * @param fromServer from server basic.
     */
    public void setFromServer(ServerBasic fromServer) {
        this.fromServer = fromServer;
    }

    @Override
    public String getRespondKey() {
        return respondKey;
    }

    /**
     * set respond key.
     *
     * @param respondKey respond key.
     */
    public void setRespondKey(String respondKey) {
        this.respondKey = respondKey;
    }
}
