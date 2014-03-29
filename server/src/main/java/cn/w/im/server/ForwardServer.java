package cn.w.im.server;

import cn.w.im.domains.ServerBasic;
import cn.w.im.domains.messages.*;
import cn.w.im.domains.messages.forward.ForwardRequestMessage;
import cn.w.im.domains.messages.forward.ForwardResponseMessage;
import cn.w.im.domains.messages.server.ReadyMessage;
import cn.w.im.utils.netty.IpAddressProvider;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-27 下午3:07.
 * Summary: forward server.
 */
public class ForwardServer {

    private static ForwardServer currentForwardServer;

    /**
     * get singleton forwardServer.
     *
     * @return singleton forwardServer.
     */
    public synchronized static ForwardServer current() {
        if (currentForwardServer == null) {
            currentForwardServer = new ForwardServer();
        }
        return currentForwardServer;
    }

    private Log logger;

    private String busHost, serverHost;
    private int busPort, serverPort;

    private boolean init = false;

    private boolean connectedError = false;

    /**
     * key host:port.
     */
    private Map<String, ChannelHandlerContext> contextMap;
    /**
     * key host:port.
     */
    private Map<String, ServerBasic> connectedServerMap;

    private List<String> serverKeys;

    private ForwardServer() {
        this.serverKeys = new ArrayList<String>();
        this.contextMap = new ConcurrentHashMap<String, ChannelHandlerContext>();
        this.connectedServerMap = new ConcurrentHashMap<String, ServerBasic>();
        logger = LogFactory.getLog(this.getClass());
    }

    /**
     * initialize server.
     *
     * @param busHost    message bus host.
     * @param busPort    message bus port.
     * @param serverHost connect server host.
     * @param serverPort connect server port.
     * @return this.
     */
    public ForwardServer init(String busHost, int busPort, String serverHost, int serverPort) {
        if (!init) {
            this.busHost = busHost;
            this.busPort = busPort;
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.init = true;
            String busKey = busHost + ":" + busPort;
            String serverKey = serverHost + ":" + serverPort;
            this.serverKeys.add(busKey);
            this.serverKeys.add(serverKey);
        }
        logger.debug("server initialized.");
        return this;
    }

    /**
     * get connected is error.
     *
     * @return true:error.
     */
    public boolean isConnectedError() {
        return this.connectedError;
    }

    /**
     * connected error.
     */
    public void connectedError() {
        logger.debug("connected error.");
        this.connectedError = true;
    }

    /**
     * connected one server.
     *
     * @param ctx current ChannelHandlerContext.
     */
    public void connected(ChannelHandlerContext ctx) {
        String key = getKey(ctx);
        logger.debug("connected server[" + key + "]");
        if (isServerKey(key)) {
            this.contextMap.put(key, ctx);
        }
    }

    private boolean isServerKey(String key) {
        for (String serverKey : this.serverKeys) {
            if (serverKey.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * all server connected.
     *
     * @return true:allConnected.
     */
    public boolean allConnected() {

        if (this.serverKeys.size() != this.contextMap.size()) {
            return false;
        }

        for (String serverKey : this.serverKeys) {
            if (!this.contextMap.containsKey(serverKey)) {
                return false;
            }
        }
        return true;
    }

    /**
     * get bus host.
     *
     * @return bus host.
     */
    public String getBusHost() {
        return busHost;
    }

    /**
     * get server host.
     *
     * @return server host.
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * get bus port.
     *
     * @return bus port.
     */
    public int getBusPort() {
        return busPort;
    }

    /**
     * get server port.
     *
     * @return get server port.
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * get is initialize.
     *
     * @return true:initialized.
     */
    public boolean isInit() {
        return init;
    }

    /**
     * request connected server basic.
     *
     * @param ctx current ChannelHandlerContext.
     */
    public void requestServerBasic(ChannelHandlerContext ctx) {
        logger.debug("request connected server basic.");
        ForwardRequestMessage requestMessage = new ForwardRequestMessage();
        ctx.writeAndFlush(requestMessage);
    }


    /**
     * receive connected server response message.
     *
     * @param responseMessage response message.
     * @param ctx             current ChannelHandlerContext.
     */
    public void receivedResponse(ForwardResponseMessage responseMessage, ChannelHandlerContext ctx) {
        String key = getKey(ctx);
        ServerBasic connectedServerBasic = responseMessage.getFromServer();
        if (this.isServerKey(key)) {
            this.connectedServerMap.put(key, connectedServerBasic);
        }
    }

    private String getKey(ChannelHandlerContext ctx) {
        String host = IpAddressProvider.getRemoteIpAddress(ctx);
        int port = IpAddressProvider.getRemotePort(ctx);
        return host + ":" + port;
    }

    /**
     * forward message.
     *
     * @param message message.
     * @param ctx     current ChannelHandlerContext.
     */
    public void forwardMessage(Message message, ChannelHandlerContext ctx) {
        String currentKey = getKey(ctx);
        for (String key : this.contextMap.keySet()) {
            if (key != currentKey) {
                this.contextMap.get(key).writeAndFlush(message);
            }
        }
    }

    /**
     * server and bus are all responded.
     *
     * @return true:all responded.
     */
    public boolean allResponded() {
        if (this.serverKeys.size() != this.connectedServerMap.size()) {
            return false;
        }

        for (String serverKey : this.serverKeys) {
            if (!this.connectedServerMap.containsKey(serverKey)) {
                return false;
            }
        }
        return true;
    }

    /**
     * forward server ready.
     *
     * @param ctx current ChannelHandlerContext.
     */
    public void ready(ChannelHandlerContext ctx) {
        ReadyMessage readyMessage = new ReadyMessage();
        ctx.writeAndFlush(readyMessage);
    }

    /**
     * forward server crashed.
     * close all connected.
     */
    public void crashed() {
        System.exit(1);
    }

    /**
     * connected server stopped.
     */
    public void serverStopped() {
        for (ChannelHandlerContext context : this.contextMap.values()) {
            context.close();
        }
        System.exit(0);
    }

    /**
     * connected server crashed.
     */
    public void serverCrashed() {
        System.exit(1);
    }
}