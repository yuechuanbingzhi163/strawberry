package cn.w.im.plugins.persistentMessage;

import cn.w.im.domains.messages.LoginMessage;
import cn.w.im.domains.messages.LogoutMessage;
import cn.w.im.domains.messages.Message;
import cn.w.im.domains.messages.NormalMessage;
import cn.w.im.exceptions.NotSupportMessageTypeException;
import cn.w.im.utils.spring.SpringContext;

/**
 * Creator: JackieHan.
 * DateTime: 14-1-6 上午10:41.
 * Summary: mongo 消息序列化处理程序创建工厂.
 */
public class ProcesserFactory {

    /**
     * 创建处理程序.
     *
     * @param message 消息.
     * @return 处理程序实例.
     */
    public static ProcessProvider createProcesser(Message message) {
        if (message instanceof LoginMessage) {
            return (ProcessProvider) SpringContext.context().getBean("mongoLoginMessageSerializer");
        } else if (message instanceof LogoutMessage) {
            return (ProcessProvider) SpringContext.context().getBean("mongoLogoutMessageSerializer");
        } else if (message instanceof NormalMessage) {
            return (ProcessProvider) SpringContext.context().getBean("mongoNormalMessageSerializer");
        } else {
            throw new NotSupportMessageTypeException(message);
        }
    }
}
