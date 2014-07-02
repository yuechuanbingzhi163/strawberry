package cn.w.im.persistent.mongo.message.forward;

import cn.w.im.domains.messages.forward.ForwardReadyMessage;
import cn.w.im.domains.mongo.forward.MongoForwardReadyMessage;
import cn.w.im.persistent.MessageDao;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Creator: JackieHan.
 * DateTime: 14-3-29 下午5:36.
 * Summary: MongoForwardReadyMessage Dao.
 */
@Component(value = "mongoForwardReadyMessageDao")
public class MongoForwardReadyMessageDao extends BasicDAO<MongoForwardReadyMessage, ObjectId> implements MessageDao<ForwardReadyMessage> {

    /**
     * constructor.
     *
     * @param ds data store.
     */
    @Autowired
    public MongoForwardReadyMessageDao(@Qualifier("dataStore")Datastore ds) {
        super(ds);
    }

    @Override
    public void save(ForwardReadyMessage message) {
        MongoForwardReadyMessage mongoMessage = new MongoForwardReadyMessage(message);
        this.save(mongoMessage);
    }
}
