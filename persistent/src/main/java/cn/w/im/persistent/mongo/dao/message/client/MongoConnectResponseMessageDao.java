package cn.w.im.persistent.mongo.dao.message.client;

import cn.w.im.core.message.persistent.MessagePersistentProvider;
import cn.w.im.core.message.client.ConnectResponseMessage;
import cn.w.im.persistent.mongo.domain.message.client.MongoConnectResponseMessage;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-29 下午5:27.
 * Summary: MongoConnectResponseMessage dao.
 */
@Component(value = "mongoConnectResponseMessagePersistentProvider")
public class MongoConnectResponseMessageDao extends BasicDAO<MongoConnectResponseMessage, ObjectId>
        implements MessagePersistentProvider<ConnectResponseMessage> {
    /**
     * constructor.
     *
     * @param ds data store.
     */
    @Autowired
    public MongoConnectResponseMessageDao(@Qualifier("dataStore") Datastore ds) {
        super(ds);
    }

    @Override
    public void save(ConnectResponseMessage message) {
        MongoConnectResponseMessage mongoMessage = new MongoConnectResponseMessage(message);
        this.save(mongoMessage);
    }
}
