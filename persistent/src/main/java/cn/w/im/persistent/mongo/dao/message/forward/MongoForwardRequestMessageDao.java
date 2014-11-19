package cn.w.im.persistent.mongo.dao.message.forward;

import cn.w.im.core.message.persistent.MessagePersistentProvider;
import cn.w.im.core.message.forward.ForwardRequestMessage;
import cn.w.im.persistent.mongo.domain.message.forward.MongoForwardRequestMessage;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-29 下午6:05.
 * Summary: MongoForwardRequestMessage dao.
 */
@Component(value = "mongoForwardRequestMessagePersistentProvider")
public class MongoForwardRequestMessageDao extends BasicDAO<MongoForwardRequestMessage, ObjectId>
        implements MessagePersistentProvider<ForwardRequestMessage> {
    /**
     * constructor.
     *
     * @param ds data store.
     */
    @Autowired
    public MongoForwardRequestMessageDao(@Qualifier("dataStore") Datastore ds) {
        super(ds);
    }

    @Override
    public void save(ForwardRequestMessage message) {
        MongoForwardRequestMessage mongoMessage = new MongoForwardRequestMessage(message);
        this.save(mongoMessage);
    }
}
