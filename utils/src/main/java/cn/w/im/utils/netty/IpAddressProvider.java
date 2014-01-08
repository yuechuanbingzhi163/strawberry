package cn.w.im.utils.netty;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Creator: JackieHan.
 * DateTime: 13-12-31 上午9:31.
 * Summary: ip地址相关.
 */
public class IpAddressProvider {

    /**
     * 获取本地Ip地址.
     * @return 本地ip地址.
     * @throws UnknownHostException UnknownHost异常.
     */
    public static String getLocalIpAddress() throws UnknownHostException {
        InetAddress localAddr = InetAddress.getLocalHost();
        return localAddr.getHostAddress().toString();
    }

    /**
     * 获取远程ip地址.
     * @param ctx ChanncelHandlerContext.
     * @return ip地址.
     */
    public static String getRemoteIpAddress(ChannelHandlerContext ctx) {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return remoteAddress.getHostString();
    }

    /**
     * 获取远程端口号.
     * @param ctx ChannelHandlerContext.
     * @return 端口号.
     */
    public static int getRemotePort(ChannelHandlerContext ctx) {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return remoteAddress.getPort();
    }
}
