package cn.w.im.forwardServer;

import cn.w.im.server.ForwardServer;
import cn.w.im.utils.ConfigHelper;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Properties;


/**
 * Creator: JackieHan.
 * DateTime: 14-1-8 下午2:52.
 * Summary: 转发服务启动入口.
 */
public class Bootstrap {

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(Bootstrap.class);

    private static Bootstrap daemon = null;

    /**
     * 启动主函数.
     *
     * @param args 启动参数.
     */
    public static void main(String[] args) {
        if (daemon == null) {
            daemon = new Bootstrap();
        }

        try {
            String command = "start";

            if (args.length > 0) {
                command = args[0];
            }

            if (command.endsWith("start")) {
                if (args.length == 5) {
                    String serverHost = args[1];
                    int serverPort = Integer.parseInt(args[2]);
                    String busHost = args[3];
                    int busPort = Integer.parseInt(args[4]);
                    ForwardServer.current().init(serverHost, serverPort, busHost, busPort);
                } else {
                    daemon.loadConfig();
                }
                daemon.startServer();
            } else if (command.equals("stop")) {
                daemon.stopServer();
            } else {
                logger.warn("Bootstrap: command [" + command + "] does not exist.");
            }
        } catch (Throwable t) {
            logger.error("unknown error!", t);
            System.exit(1);
        }
    }

    private final EventLoopGroup messageBusClientGroup = new NioEventLoopGroup();
    private final EventLoopGroup serverClientGroup = new NioEventLoopGroup();
    private boolean connectingServer = false;
    private boolean connectingBus = false;

    /**
     * 启动服务器.
     */
    private void loadConfig() throws Exception {
        logger.debug("loading config.");

        Properties properties = ConfigHelper.getConfig(this.getClass(), "conf/server.conf");

        String serverHost = properties.getProperty("server.host");
        int serverPort = Integer.parseInt(properties.getProperty("server.port"));
        String busHost = properties.getProperty("bus.host");
        int busPort = Integer.parseInt(properties.getProperty("bus.port"));

        ForwardServer.current().init(busHost, busPort, serverHost, serverPort);

        logger.debug("read configuration: server[" + serverHost + ":" + serverPort + "],messageBus[" + busHost + ":" + busPort + "]");
        logger.debug("loaded config.");
    }

    private void startServer() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectingServer = true;
                    connectServer();
                } catch (Exception ex) {
                    ForwardServer.current().connectedError();
                    logger.error("start server error.", ex);
                    connectingServer = false;
                    stopServer();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectingBus = true;
                    connectMessageBus();
                } catch (Exception ex) {
                    ForwardServer.current().connectedError();
                    logger.error("register to message bus error.", ex);
                    connectingBus = false;
                    stopServer();
                }
            }
        }).start();
    }

    private void connectServer() throws InterruptedException {
        logger.debug("connect message bus server starting.");

        io.netty.bootstrap.Bootstrap bootstrap = new io.netty.bootstrap.Bootstrap();
        bootstrap.group(this.serverClientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ServerInitializer());

        ChannelFuture connectFuture = bootstrap.connect(ForwardServer.current().getServerHost(), ForwardServer.current().getServerPort()).sync();
        connectFuture.addListener(connectionServerFutureListener);
        connectFuture.channel().closeFuture().sync();
    }

    private ChannelFutureListener connectionServerFutureListener = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            connectingServer = false;
            logger.debug("connect server completed.");
        }
    };


    private void connectMessageBus() throws InterruptedException {
        logger.debug("connect message bus server starting.");

        io.netty.bootstrap.Bootstrap bootstrap = new io.netty.bootstrap.Bootstrap();
        bootstrap.group(messageBusClientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ServerInitializer());

        ChannelFuture connectFuture = bootstrap.connect(ForwardServer.current().getBusHost(), ForwardServer.current().getBusPort()).sync();
        connectFuture.addListener(connectionFutureListener);
        connectFuture.channel().closeFuture().sync();
    }

    private ChannelFutureListener connectionFutureListener = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            connectingBus = false;
            logger.debug("connect message bus server completed.");
        }
    };

    private synchronized void stopServer() {
        try {
            logger.debug("stopping.");
            if (ForwardServer.current().isConnectedError()) {
                waitStarted();
            }

            this.messageBusClientGroup.shutdownGracefully();
            messageBusClientGroup.shutdownGracefully();
            if (ForwardServer.current().isConnectedError()) {
                logger.debug("start error.stopped.");
                System.exit(1);
            } else {
                logger.debug("manual stopped.");
                System.exit(0);
            }
        } catch (Exception ex) {
            logger.error("stop error.", ex);
            logger.debug("error stopped.");
            System.exit(1);
        }
    }

    private synchronized void waitStarted() throws Exception {
        long waitStartDate = new Date().getTime();
        while (true && waitStartDate + 20000 >= new Date().getTime()) {
            logger.debug("wait starting is done.");
            this.wait(500);
            if (!connectingServer && !connectingBus) {
                break;
            }
        }
    }
}