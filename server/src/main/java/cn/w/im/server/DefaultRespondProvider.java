package cn.w.im.server;

import cn.w.im.domains.ServerBasic;
import cn.w.im.domains.messages.server.RespondMessage;
import cn.w.im.exceptions.NotRegisterRespondMessageException;
import cn.w.im.exceptions.NotRegisterRespondServerException;
import cn.w.im.exceptions.RegisteredRespondMessageException;
import cn.w.im.exceptions.RegisteredRespondServerException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-25 上午10:16.
 * Summary: implement ResponseProvider.
 * <p/>
 * this provider cached a map,the map save the waiting for reply message flag, the map value is a map who save server node id and the server has allResponded response message.
 */
public class DefaultRespondProvider implements RespondProvider {

    private Map<String, Map<String, Boolean>> responseMap;

    /**
     * constructor.
     */
    public DefaultRespondProvider() {
        responseMap = new ConcurrentHashMap<String, Map<String, Boolean>>();
    }

    @Override
    public boolean allResponded(String respondKey) throws NotRegisterRespondMessageException {
        if (!this.responseMap.containsKey(respondKey)) {
            throw new NotRegisterRespondMessageException(respondKey);
        }

        Map<String, Boolean> serverMap = this.responseMap.get(respondKey);
        for (String key : serverMap.keySet()) {
            if (!serverMap.get(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void receivedRespondedMessage(RespondMessage responseMessage) throws NotRegisterRespondMessageException, NotRegisterRespondServerException {
        String messageFlag = responseMessage.getRespondKey();
        if (!this.responseMap.containsKey(messageFlag)) {
            throw new NotRegisterRespondMessageException(messageFlag);
        }
        ServerBasic respondingServer = responseMessage.getFromServer();
        Map<String, Boolean> serverMap = this.responseMap.get(messageFlag);
        if (serverMap.containsKey(respondingServer.getNodeId())) {
            serverMap.remove(respondingServer.getNodeId());
            serverMap.put(respondingServer.getNodeId(), true);
        } else {
            throw new NotRegisterRespondServerException(respondingServer.getNodeId());
        }
    }

    @Override
    public void registerResponded(String respondKey, ServerBasic serverBasic) throws RegisteredRespondMessageException, RegisteredRespondServerException {
        if (this.responseMap.containsKey(respondKey)) {
            Map<String, Boolean> serverMap = this.responseMap.get(respondKey);
            if (!serverMap.containsKey(serverBasic.getNodeId())) {
                serverMap.put(serverBasic.getNodeId(), false);
            } else {
                throw new RegisteredRespondMessageException(respondKey);
            }
        } else {
            Map<String, Boolean> serverMap = new ConcurrentHashMap<String, Boolean>();
            if (serverMap.containsKey(serverBasic.getNodeId())) {
                throw new RegisteredRespondServerException(serverBasic.getNodeId());
            }
            serverMap.put(serverBasic.getNodeId(), false);
            this.responseMap.put(respondKey, serverMap);
        }
    }

    @Override
    public void interrupt(String respondKey) throws RespondInterruptException, NotRegisterRespondMessageException {
        if (this.responseMap.containsKey(respondKey)) {
            this.responseMap.remove(respondKey);
            throw new RespondInterruptException(respondKey);
        } else {
            throw new NotRegisterRespondMessageException(respondKey);
        }
    }
}