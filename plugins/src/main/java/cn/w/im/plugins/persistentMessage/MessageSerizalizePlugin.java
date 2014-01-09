package cn.w.im.plugins.persistentMessage;

import cn.w.im.domains.HandlerContext;
import cn.w.im.domains.messages.Message;
import cn.w.im.plugins.MessagePlugin;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creator: JackieHan.
 * DateTime: 13-12-31 下午3:04.
 * Summary: Mongo 序列化插件.
 */
public class MessageSerizalizePlugin extends MessagePlugin {

    /**
     * 日志.
     */
    private Log logger = LogFactory.getLog(this.getClass());

    /**
     * 构造函数.
     */
    public MessageSerizalizePlugin() {
        super("MessageSerizalizePlugin", "Mongo序列化!");
    }

    @Override
    public boolean isMatch(HandlerContext context) {
        return true;
    }

    @Override
    public void processMessage(Message message, HandlerContext context) {
        logger.info("开始保存!");
        ProcesserFactory.createProcesser(message).serialize(message);
        logger.info("保存成功!");
    }
}
