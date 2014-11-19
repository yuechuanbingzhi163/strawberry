package cn.w.im.persistent.mongo.dao.message.forward;

import cn.w.im.core.message.persistent.MessagePersistentProvider;
import cn.w.im.core.message.forward.ForwardResponseMessage;
import cn.w.im.persistent.mongo.domain.message.forward.MongoForwardResponseMessage;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-29 下午6:07.
 * Summary: MongoForwardResponseMessage dao.
 */
@Component(value = "mongoForwardResponseMessagePersistentProvider")
public class MongoForwardResponseMessageDao extends BasicDAO<MongoForwardResponseMessage, ObjectId>
        implements MessagePersistentProvider<ForwardResponseMessage> {
    /**
     * constructor.
     *
     * @param ds data store.
     */
    @Autowired
    public MongoForwardResponseMessageDao(@Qualifier("dataStore") Datastore ds) {
        super(ds);
    }

    @Override
    public void save(ForwardResponseMessage message) {
        MongoForwardResponseMessage mongoMessage = new MongoForwardResponseMessage(message);
        this.save(mongoMessage);
    }
}
