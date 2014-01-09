package cn.w.im.plugins.persistentMessage.mongo;

import cn.w.im.domains.messages.LogoutMessage;
import cn.w.im.domains.messages.Message;
import cn.w.im.domains.mongo.MongoLogoutMessage;
import cn.w.im.mongo.dao.message.MongoLogoutMessageDao;
import cn.w.im.plugins.persistentMessage.ProcessProvider;

/**
 * Creator: JackieHan.
 * DateTime: 14-1-6 上午10:52.
 * Summary: 登陆消息Mongo序列化处理实现.
 */
public class MongoLogoutMessageProviderImpl implements ProcessProvider {

    /**
     * MongoLogoutMessageDao.
     */
    private MongoLogoutMessageDao dao;

    /**
     * 构造函数.
     * @param dao MongoLogoutMessageDao.
     */
    public MongoLogoutMessageProviderImpl(MongoLogoutMessageDao dao) {
        this.dao = dao;
    }

    @Override
    public void serialize(Message message) {
        LogoutMessage logoutMessage = (LogoutMessage) message;
        MongoLogoutMessage mongologoutMessage = new MongoLogoutMessage(logoutMessage);
        dao.save(mongologoutMessage);
    }
}
