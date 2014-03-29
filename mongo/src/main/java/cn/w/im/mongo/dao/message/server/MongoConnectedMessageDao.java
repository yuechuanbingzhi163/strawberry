package cn.w.im.mongo.dao.message.server;

import cn.w.im.domains.messages.server.ConnectedMessage;
import cn.w.im.domains.mongo.server.MongoConnectedMessage;
import cn.w.im.mongo.dao.message.MessageDao;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-29 下午6:08.
 * Summary: MongoConnectedMessage dao.
 */
public class MongoConnectedMessageDao extends BasicDAO<MongoConnectedMessage, ObjectId> implements MessageDao<ConnectedMessage> {
    /**
     * constructor.
     *
     * @param ds data store.
     */
    public MongoConnectedMessageDao(Datastore ds) {
        super(ds);
    }

    @Override
    public void save(ConnectedMessage message) {
        MongoConnectedMessage mongoMessage = new MongoConnectedMessage(message);
        this.save(mongoMessage);
    }
}
