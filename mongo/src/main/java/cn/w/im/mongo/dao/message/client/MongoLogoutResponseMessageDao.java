package cn.w.im.mongo.dao.message.client;

import cn.w.im.domains.messages.client.LogoutResponseMessage;
import cn.w.im.domains.mongo.client.MongoLogoutResponseMessage;
import cn.w.im.mongo.dao.message.MessageDao;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-29 下午5:31.
 * Summary: MongoLogoutResponseMessage Dao.
 */
public class MongoLogoutResponseMessageDao extends BasicDAO<MongoLogoutResponseMessage, ObjectId> implements MessageDao<LogoutResponseMessage> {

    /**
     * constructor.
     *
     * @param ds data store.
     */
    protected MongoLogoutResponseMessageDao(Datastore ds) {
        super(ds);
    }

    @Override
    public void save(LogoutResponseMessage message) {
        MongoLogoutResponseMessage mongoMessage = new MongoLogoutResponseMessage(message);
        this.save(mongoMessage);
    }
}
